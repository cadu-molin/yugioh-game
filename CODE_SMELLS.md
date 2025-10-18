# Relatório de Code Smells

Este documento identifica dois code smells encontrados no projeto, explica como foram identificados, quais problemas causam e sugere refatorações para corrigi‑los.

## 1) Duplicação de código e validações repetidas em Player (overloads semelhantes)

- Onde: `eg.edu.guc.yugioh.board.player.Player`
  - Métodos sobrecarregados e muito semelhantes:
    - `summonMonster(MonsterCard)` (linhas ~56–80)
    - `summonMonster(MonsterCard, ArrayList<MonsterCard>)` (linhas ~82–107)
    - `setMonster(MonsterCard)` (linhas ~109–133)
    - `setMonster(MonsterCard, ArrayList<MonsterCard>)` (linhas ~135–160)
  - Em todos eles há os mesmos “guard clauses” e blocos lógicos repetidos:
    - Verificações repetidas de fim de jogo: `Card.getBoard().isGameOver()`
    - Verificação do jogador ativo: `this != Card.getBoard().getActivePlayer()`
    - Verificação de adição de monstro no turno: `addedMonsterThisTurn`
    - Chamada a `field.addMonsterToField(...)` com variações mínimas (modo e sacrifícios)

1. Como foi identificado
   - Analisando os métodos citados, nota‑se a repetição quase literal dos mesmos trechos (mesmas condições, mesma sequência de retornos e flags), mudando apenas o modo (`Mode.ATTACK` ou `Mode.DEFENSE`) e a presença ou não do parâmetro `sacrifices`.

2. Problema causado
   - A duplicação aumenta o custo de manutenção: qualquer alteração em uma regra comum (p.ex., uma nova condição de validação) precisa ser replicada em 4 locais.
   - Risco de inconsistência e bugs: pequenos ajustes podem ficar desincronizados entre overloads.
   - Dificulta testes e leitura: lógica essencial fica espalhada e redundante.

3. Refatorações recomendadas
   - Extract Method (Extrair Método): extrair as validações comuns para um método privado, por exemplo `canPerformAction()` ou `validateMonsterPlacement(...)`.
   - Parameterize Method (Parametrizar Método) ou Consolidate Duplicate Conditional Fragments: unificar overloads parecidos em um método central com parâmetros explícitos, por exemplo:
     - `placeMonster(MonsterCard monster, List<MonsterCard> sacrifices, Mode mode, boolean isSet)`
     - As sobrecargas públicas poderiam delegar para esse método central, reduzindo duplicação.
   - Introduce Assertion/Guard Clauses centralizadas (como parte do Extract Method) para tornar as pré‑condições claras e reutilizáveis.


## 2) Método longo, números mágicos e lógica de UI concentrada em BoardFrame.openAnimationPanel

- Onde: `eg.edu.guc.yugioh.gui.boardframe.BoardFrame`
  - Classe com 300+ linhas; o método `openAnimationPanel(AnimationGIF, Runnable)` ocupa ~100 linhas (linhas ~175–281).
  - O método mistura várias responsabilidades: gestão de overlay modal, listeners de mouse/teclado, criação de painel, cronômetro, política de opacidade/desenho, e callback.
  - Presença de número mágico: `int animationDuration = 3000;` (linha ~250), sem constante nomeada.
  - Repetição de código de “consumo” de eventos em múltiplos listeners anônimos.

1. Como foi identificado
   - Pela contagem de linhas (método extenso) e pela quantidade de blocos anônimos (listeners) dentro do mesmo método, além da responsabilidade múltipla (criar UI, bloquear interações, temporizar, restaurar estado e chamar callback).

2. Problema causado
   - Baixa coesão e alta complexidade ciclomática: difícil de entender, testar e evoluir.
   - Duplicação implícita: lógica de consumir eventos espalhada em vários handlers anônimos, aumentando chances de inconsistência.
   - Manutenção difícil: mudanças de política de bloqueio/tempo/opacidade exigem navegar num método grande.

3. Refatorações recomendadas
   - Extract Method (Extrair Método): quebrar `openAnimationPanel(...)` em métodos privados, como `createModalOverlay(...)`, `installEventBlockers(...)`, `centerAnimationPanel(...)`, `scheduleRemoval(...)`.
   - Introduce Constant (Introduzir Constante): extrair `3000` para algo como `private static final int DEFAULT_ANIMATION_DURATION_MS = 3000;`.
   - Extract Class (Extrair Classe) ou Introduce Parameter Object: mover a lógica de overlay/animação para uma classe dedicada (ex.: `AnimationOverlay`), reduzindo o tamanho e responsabilidades de `BoardFrame`.
   - Substituir classes anônimas repetidas por lambdas (quando possível) ou métodos utilitários para consumo de eventos, diminuindo repetição.

---

Observação: Foram escolhidos dois pontos com alto potencial de ganho de manutenção e legibilidade sem alterar o comportamento atual do jogo. As refatorações propostas podem ser aplicadas incrementalmente, com cobertura de testes/regressão após cada etapa para garantir que o comportamento original seja preservado.