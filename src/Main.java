import javax.swing.*;

public class Main {
public static void main(String[] args) {
  SwingUtilities.invokeLater(() -> {
            // Show game mode selection
   String[] options = {"Single Player", "Multiplayer"};
   int choice = JOptionPane.showOptionDialog(
     null,
    "Choose Game Mode:",
    "Doodle Jump",
     JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE,
       null,
       options,
       options[0]
    );

if (choice == 0) {
                // Single Player
JFrame frame = new JFrame("Doodle Jump - Single Player");
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.add(new Game());
  frame.pack();
 frame.setLocationRelativeTo(null);
 frame.setVisible(true);
    }
else if (choice == 1) {
                // Multiplayer - ask if host or join
  String[] multiOptions = {"Host Game", "Join Game"};
   int multiChoice = JOptionPane.showOptionDialog(
    null,
   "Multiplayer Mode:",
  "Doodle Jump - Multiplayer",
 JOptionPane.YES_NO_OPTION,
 JOptionPane.QUESTION_MESSAGE,
   null,
     multiOptions,
     multiOptions[0]
     );

  if (multiChoice != -1) {
   boolean isHost = (multiChoice == 0);
   JFrame frame = new JFrame("Doodle Jump - Multiplayer " + (isHost ? "(Host)" : "(Client)"));
   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new MultiplayerGame(isHost));
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
       }
    }
  });
    }
}
