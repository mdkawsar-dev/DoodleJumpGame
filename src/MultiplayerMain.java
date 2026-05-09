import javax.swing.*;

public class MultiplayerMain {
 public static void main(String[] args) {
   SwingUtilities.invokeLater(() -> {

    String[] options = {"Host Game (Player 1)", "Join Game (Player 2)"};
    int choice = JOptionPane.showOptionDialog(
     null,
     "Choose your role:",
     "Multiplayer Doodle Jump",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE,
   null,
   options,
    options[0]
            );

boolean isServer = (choice == 0);

 JFrame frame = new JFrame("Multiplayer Doodle Jump - " +
     (isServer ? "Player 1 (Host)" : "Player 2 (Client)"));
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.add(new MultiplayerGame(isServer));
  frame.pack();
  frame.setLocationRelativeTo(null);
  frame.setVisible(true);
        });
    }
}
