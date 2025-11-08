package eg.edu.guc.yugioh.gui.boardframe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import eg.edu.guc.yugioh.cards.MonsterCard;
import eg.edu.guc.yugioh.cards.spells.SpellCard;
import eg.edu.guc.yugioh.configsGlobais.GameConstants;
import eg.edu.guc.yugioh.configsGlobais.Logger;
import eg.edu.guc.yugioh.gui.Main;
import eg.edu.guc.yugioh.gui.otherframes.AnimationPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class BoardFrame extends JFrame implements ActionListener {
    private FieldPanel fieldPanel;
    private HandPanel opponentHandPanel;
    private HandPanel activeHandPanel;
    private WestImagesPanel westImagesPanel;
    private EastButtonsPanel eastButtonsPanel;
    private CardInfoPanel cardInfoPanel;

    private MonsterCard attackingMonster;
    private boolean toSwitch = false;
    private SpellCard spellToActivate;

    private SummonSacrificeRitualManager summonSacrificeRitualManager;

    public BoardFrame() {
        super("Yu-Gi-Oh!");
        setFramePrefrences();
        initializeAttributes();
        addPanels();
        validate();
    }

    private void setFramePrefrences() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        // Use full screen to ensure all panels fit, but allow user to resize
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeAttributes() {

        fieldPanel = new FieldPanel();
        activeHandPanel = new HandPanel(true);
        opponentHandPanel = new HandPanel(false);
        opponentHandPanel.setPreferredSize(new Dimension(activeHandPanel.getPreferredSize().width, 15));
        westImagesPanel = new WestImagesPanel();
        eastButtonsPanel = new EastButtonsPanel();
        cardInfoPanel = new CardInfoPanel();
        eastButtonsPanel.getRestartGameButton().addActionListener(e -> {
            dispose();
            Main.startNewGame();
        });

        summonSacrificeRitualManager = new SummonSacrificeRitualManager();
    }

    private void addPanels() {

        this.addDefaultBackground();

        ((JComponent) getContentPane()).setLayout(new BorderLayout());

        getContentPane().add(this.createMainDataPanel(), BorderLayout.CENTER);
    }

    public FieldPanel getFieldPanel() {
        return fieldPanel;
    }

    public void setFieldPanel(FieldPanel fieldPanel) {
        this.fieldPanel = fieldPanel;
    }

    public HandPanel getOpponentHandPanel() {
        return opponentHandPanel;
    }

    public void setOpponentHandPanel(HandPanel opponentHandPanel) {
        this.opponentHandPanel = opponentHandPanel;
    }

    public HandPanel getActiveHandPanel() {
        return activeHandPanel;
    }

    public void setActiveHandPanel(HandPanel activeHandPanel) {
        this.activeHandPanel = activeHandPanel;
    }

    public WestImagesPanel getWestImagesPanel() {
        return westImagesPanel;
    }

    public void setWestImagesPanel(WestImagesPanel westImagesPanel) {
        this.westImagesPanel = westImagesPanel;
    }

    public EastButtonsPanel getEastButtonsPanel() {
        return eastButtonsPanel;
    }

    public void setEastButtonsPanel(EastButtonsPanel eastButtonsPanel) {
        this.eastButtonsPanel = eastButtonsPanel;
    }

    public CardInfoPanel getCardInfoPanel() {
        return cardInfoPanel;
    }

    public void updateBoardFrame() {

        Logger.logs().info("BoardFrame - updateBoardFrame");

        activeHandPanel.updateHand();
        opponentHandPanel.updateHand();
        fieldPanel.updateAllPanels();
        repaint();
        validate();
    }

    public boolean isToSwitch() {
        return toSwitch;
    }

    public void setToSwitch(boolean toSwitch) {
        this.toSwitch = toSwitch;
    }

    public SpellCard getSpellToActivate() {
        return spellToActivate;
    }

    public void setSpellToActivate(SpellCard spellToActivate) {
        this.spellToActivate = spellToActivate;
    }

    public MonsterCard getAttackingMonster() {
        return attackingMonster;
    }

    public void setAttackingMonster(MonsterCard attackingMonster) {
        this.attackingMonster = attackingMonster;
    }

    public void resetHandlers() {

        Logger.logs().info("BoardFrame - resetHandlers");

        attackingMonster = null;
        toSwitch = false;
        spellToActivate = null;

        this.summonSacrificeRitualManager.reset();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        this.toBack();
    }

    public void openAnimationPanel(AnimationGIF animation) {
        // Delegate to the overload without any callback
        openAnimationPanel(animation, null);
    }

    public void openAnimationPanel(AnimationGIF animation, Runnable onClose) {
        // Hide the glass pane temporarily
        this.getGlassPane().setVisible(false);

        // Create the animation panel
        AnimationPanel animationPanel = new AnimationPanel(animation);

        // Create a modal overlay panel with white background that covers the entire frame
        JPanel modalOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Paint a semi-transparent white background
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        
        modalOverlay.setLayout(new BorderLayout());
        modalOverlay.setOpaque(false);
        modalOverlay.setBounds(0, 0, this.getWidth(), this.getHeight());
        
        // Add mouse listeners to block all interactions with underlying components
        modalOverlay.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Consume the event to prevent it from reaching underlying components
                e.consume();
            }
            
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                e.consume();
            }
            
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                e.consume();
            }
        });
        
        modalOverlay.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                e.consume();
            }
            
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                e.consume();
            }
        });

        // Center the animation panel within the modal overlay
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(animationPanel);

        modalOverlay.add(centerPanel, BorderLayout.CENTER);

        // Add the modal overlay to the layered pane at the highest layer to block all interactions
        this.getLayeredPane().add(modalOverlay, JLayeredPane.MODAL_LAYER);

        // Make the animation panel visible
        animationPanel.setVisible(true);
        modalOverlay.setVisible(true);

        // Repaint to ensure the overlay is displayed
        this.repaint();

        // Set up a timer to automatically remove the panel after the GIF duration
        // Using 3 seconds as a reasonable default for GIF animations
        int animationDuration = GameConstants.ANIMATION_DURATION_MS; // 3 seconds in milliseconds

        Timer removalTimer = new Timer(animationDuration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remove the modal overlay
                BoardFrame.this.getLayeredPane().remove(modalOverlay);

                // Restore the glass pane if needed
                BoardFrame.this.getGlassPane().setVisible(true);

                // Repaint to update the display
                BoardFrame.this.repaint();

                // Stop the timer
                ((Timer) e.getSource()).stop();

                // Invoke callback if provided
                if (onClose != null) {
                    try {
                        onClose.run();
                    } catch (Throwable t) {
                        // Swallow to avoid breaking EDT
                    }
                }
            }
        });

        // Start the timer (non-repeating)
        removalTimer.setRepeats(false);
        removalTimer.start();
    }

    private void addDefaultBackground() {
        JLabel background = new JLabel(new ImageIcon("images/background.jpg"));
        setContentPane(background);
    }

    private JPanel createMainDataPanel() {
        JPanel dataPanel = new JPanel(new BorderLayout());
        dataPanel.setOpaque(false);

        dataPanel.add(opponentHandPanel, BorderLayout.NORTH);
        dataPanel.add(fieldPanel, BorderLayout.CENTER);

        dataPanel.add(this.createSidePanel(), BorderLayout.SOUTH);
        dataPanel.add(eastButtonsPanel, BorderLayout.EAST);
        dataPanel.add(cardInfoPanel, BorderLayout.WEST);

        return dataPanel;
    }

    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel(new BorderLayout());

        sidePanel.add(westImagesPanel, BorderLayout.WEST);
        sidePanel.add(activeHandPanel, BorderLayout.CENTER);
        sidePanel.setOpaque(false);
        sidePanel.setBorder(new EmptyBorder(0, 10, 0, 0));

        return sidePanel;
    }

    public void startRitualSummon(MonsterCard monsterToSummon, boolean isToSummonInAttackMode) throws Exception {
        this.summonSacrificeRitualManager.startRitual(monsterToSummon, isToSummonInAttackMode);
    }

    public void sacrificeMonster(MonsterCard monsterToSacrifice) {
        this.summonSacrificeRitualManager.sacrificeMonster(monsterToSacrifice);
    }

    public boolean isSummoningSacrificeMonster(){
        return this.summonSacrificeRitualManager.isSummoningSacrificeMonster();
    }

    public void resetMonsterToSummon(){
        this.summonSacrificeRitualManager.setMonsterToSummon(null);
    }
}