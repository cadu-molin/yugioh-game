package eg.edu.guc.yugioh.gui.listener;

import eg.edu.guc.yugioh.board.WinnerListener;
import eg.edu.guc.yugioh.board.player.Player;
import eg.edu.guc.yugioh.gui.GUI;
import eg.edu.guc.yugioh.gui.otherframes.WinnerFrame;

public class WinnerGUIListener implements WinnerListener {
    @Override
    public void onWinnerDeclared(Player winner) {
        WinnerFrame x = new WinnerFrame();
        GUI.setWinnerFrame(x);
    }
}
