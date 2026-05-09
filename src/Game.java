import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final int PLATFORM_C = 7;
    private static final double GRAV = 0.4;
    private static final double JUMP = -10;
    private static final int PLATFORM_WIDTH = 100;
    private static final int PLATFORM_HEIGHT = 20;
    private Timer timer;
    private Random random;
    private ArrayList<Entity> platforms;
    private Entity player;
    private int score;
    private boolean gameOver;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean gameStarted;
    private int difficultyLevel = 1;

    public Game() {
     setPreferredSize(new Dimension(WIDTH, HEIGHT));
      setBackground(Color.WHITE);
      setFocusable(true);
      addKeyListener(this);
      random = new Random();
      timer = new Timer(1000 / 60, this);
      startGame();
      timer.start();
    }

 private void startGame() {
   platforms = new ArrayList<>();
   int startX = WIDTH / 2 - PLATFORM_WIDTH / 2;
   int startY = HEIGHT - 150;
   platforms.add(new Entity(startX, startY, PLATFORM_WIDTH, PLATFORM_HEIGHT, new Color(50, 150, 50)));
   player = new Entity(startX + PLATFORM_WIDTH / 2 - 15, startY - 40, 30, 40, new Color(100, 100, 200));
   player.setVelocityY(0);
   player.setVelocityX(0);
  for (int i = 0; i < PLATFORM_C; i++)
  {
  int y = HEIGHT - 200 - (i * 80);
  createPlatform(y);
    }
        score = 0;
        gameOver = false;
        leftPressed = false;
        rightPressed = false;
        gameStarted = true;
        difficultyLevel = 1;
    }

    private void createPlatform(double y) {
        int x = random.nextInt(WIDTH - PLATFORM_WIDTH);
        platforms.add(new Entity(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT, new Color(120, 200, 80)));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 250, 255));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        for (Entity platform : platforms) {
            platform.draw(g);
        }
        player.draw(g);

        g.setColor(Color.BLACK);
        g.fillOval((int) player.getX() + 7, (int) player.getY() + 8, 6, 6);
        g.fillOval((int) player.getX() + 17, (int) player.getY() + 8, 6, 6);
        g.drawArc((int) player.getX() + 8, (int) player.getY() + 20, 14, 8, 0, -180);

        g.setColor(Color.BLACK); //score
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 25);
        g.drawString("Level: " + difficultyLevel, 10, 50);
        if (!gameStarted || gameOver) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            if (gameOver) {
                String gameOverText = "Game Over";
                int textWidth = g.getFontMetrics().stringWidth(gameOverText);
                g.drawString(gameOverText, WIDTH / 2 - textWidth / 2, HEIGHT / 2 - 50);
                g.setFont(new Font("Arial", Font.BOLD, 24));
                String scoreText = "Score: " + score;
                textWidth = g.getFontMetrics().stringWidth(scoreText);
                g.drawString(scoreText, WIDTH / 2 - textWidth / 2, HEIGHT / 2);
            } else {
                String titleText = "Doodle Jump";
                int textWidth = g.getFontMetrics().stringWidth(titleText);
                g.drawString(titleText, WIDTH / 2 - textWidth / 2, HEIGHT / 2 - 50);
            }
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            String instructionText = "Press SPACE to " + (gameOver ? "restart" : "start");
            int textWidth = g.getFontMetrics().stringWidth(instructionText);
            g.drawString(instructionText, WIDTH / 2 - textWidth / 2, HEIGHT / 2 + 50);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            String controlsText = "Use LEFT/RIGHT arrows to move";
            textWidth = g.getFontMetrics().stringWidth(controlsText);
            g.drawString(controlsText, WIDTH / 2 - textWidth / 2, HEIGHT / 2 + 80);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted && !gameOver) {
          updateGame();
        }
        repaint();
    }

    private void updateGame() {
        if (score / 100 + 1 > difficultyLevel) {
            difficultyLevel++;
        }

        double moveSpeed = 4 + difficultyLevel * 0.5; //move speed
        if (leftPressed) {
            player.setVelocityX(-moveSpeed);
        } else if (rightPressed) {
            player.setVelocityX(moveSpeed);
        } else {
            if (player.getVelocityX() > 0.5) {
                player.setVelocityX(player.getVelocityX() - 0.5);
            } else if (player.getVelocityX() < -0.5) {
                player.setVelocityX(player.getVelocityX() + 0.5);
            } else {
                player.setVelocityX(0);
            }
        }
        player.setVelocityY(player.getVelocityY() + GRAV);
        player.setX(player.getX() + player.getVelocityX());
        player.setY(player.getY() + player.getVelocityY());
        if (player.getX() + player.getWidth() < 0) {
            player.setX(WIDTH);
        } else if (player.getX() > WIDTH) {
            player.setX(-player.getWidth());
        }
     if (player.getVelocityY() > 0) {
        for (Entity platform : platforms) {
          if (player.getX() > platform.getX() &&
            player.getX() + player.getWidth() < platform.getX() + platform.getWidth() &&
            player.getY() + player.getHeight() > platform.getY() &&
            player.getY() + player.getHeight() < platform.getY() + platform.getHeight() &&
            player.getY() < platform.getY()) {
            player.setY(platform.getY() - player.getHeight());
            player.setVelocityY(JUMP);

   SoundPlayer.playSound("jump.wav");
            }
        }
   }
if (player.getY() < HEIGHT / 2) {
   int offset = HEIGHT / 2 - (int) player.getY();
    player.setY(HEIGHT / 2);
     for (int i = 0; i < platforms.size(); i++) {
      Entity platform = platforms.get(i);
       platform.setY(platform.getY() + offset);
         if (platform.getY() > HEIGHT) {

          platforms.remove(i);
           i--;
            int minY = -50;
              if (!platforms.isEmpty()) {
              double highestY = HEIGHT;
              for (Entity p : platforms) {
              if (p.getY() < highestY) {
               highestY = p.getY();
                      }
                  }
       minY = (int) highestY - random.nextInt(30) - 80;
          }
            int x = random.nextInt(WIDTH - PLATFORM_WIDTH);
             platforms.add(new Entity(x, minY, PLATFORM_WIDTH, PLATFORM_HEIGHT, new Color(120, 200, 80)));
             score += 10;
            }
        }
   }
 if (player.getY() > HEIGHT) {
    if (!gameOver) {
        SoundPlayer.playSound("pada.wav");
      }
   gameOver = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (key == KeyEvent.VK_SPACE) {
            if (gameOver || !gameStarted) {
                startGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
