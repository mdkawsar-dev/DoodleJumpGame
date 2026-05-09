import java.awt.Color;
import java.util.*;

public class GameState {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final int PLATFORM_COUNT = 7;
    private static final double GRAVITY = 0.4;
    private static final double JUMP_VELOCITY = -10;
    private static final int PLATFORM_WIDTH = 100;
    private static final int PLATFORM_HEIGHT = 20;

    private List<Platform> platforms;
    private List<NetworkPlayer> players;
    private Random random;
    private boolean gameOver;
    private int gameHeight;

    public GameState() {
        platforms = new ArrayList<>();
        players = new ArrayList<>();
        random = new Random();
        gameOver = false;
        gameHeight = 0;
    }

    public void initializeGame() {
        platforms.clear();
        players.clear();
        gameOver = false;
        gameHeight = 0;


        int startX = WIDTH / 2 - PLATFORM_WIDTH / 2;
        int startY = HEIGHT - 150;
        platforms.add(new Platform(startX, startY, PLATFORM_WIDTH, PLATFORM_HEIGHT));


  players.add(new NetworkPlayer(0, startX + 20, startY - 40, 30, 40, new Color(100, 100, 200)));
  players.add(new NetworkPlayer(1, startX + 50, startY - 40, 30, 40, new Color(200, 100, 100)));


 for (int i = 0; i < PLATFORM_COUNT; i++) {  //generate platform
    int y = HEIGHT - 200 - (i * 80);
     createPlatform(y);
        }
    }

    private void createPlatform(double y) {
        int x = random.nextInt(WIDTH - PLATFORM_WIDTH);
        platforms.add(new Platform(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT));
    }

    public void update() {
        if (gameOver) return;

        for (NetworkPlayer player : players) { //update player
            updatePlayer(player);
        }

     NetworkPlayer highestPlayer = getHighestPlayer();
        if (highestPlayer != null && highestPlayer.getY() < HEIGHT / 2) {
            scrollWorld(highestPlayer);
        }


        checkGameOver();
    }

    private void updatePlayer(NetworkPlayer player) {

   player.setVelocityY(player.getVelocityY() + GRAVITY);


if (!player.isLeftPressed() && !player.isRightPressed()) {
    if (player.getVelocityX() > 0.5) {
      player.setVelocityX(player.getVelocityX() - 0.5);
      }
    else if (player.getVelocityX() < -0.5) {
     player.setVelocityX(player.getVelocityX() + 0.5);
      } else
      {
     player.setVelocityX(0);
      }
        }


 player.setX(player.getX() + player.getVelocityX());
  player.setY(player.getY() + player.getVelocityY());


  if (player.getX() + player.getWidth() < 0) {
    player.setX(WIDTH);
     }
  else if (player.getX() > WIDTH) {
    player.setX(-player.getWidth());
        }

if (player.getVelocityY() > 0) {
  for (Platform platform : platforms) {
      if (isPlayerOnPlatform(player, platform)) {
       player.setY(platform.getY() - player.getHeight());
        player.setVelocityY(JUMP_VELOCITY);
        player.addScore(10);
       break;
         }
     }
   }
    }

    private boolean isPlayerOnPlatform(NetworkPlayer player, Platform platform) {
        return player.getX() + 10 < platform.getX() + platform.getWidth() &&
                player.getX() + player.getWidth() - 10 > platform.getX() &&
                player.getY() + player.getHeight() > platform.getY() &&
                player.getY() + player.getHeight() < platform.getY() + platform.getHeight() &&
                player.getY() < platform.getY();
    }

    private NetworkPlayer getHighestPlayer() {
        NetworkPlayer highest = null;
        for (NetworkPlayer player : players) {
            if (!player.isDead() && (highest == null || player.getY() < highest.getY())) {
                highest = player;
            }
        }
        return highest;
    }

    private void scrollWorld(NetworkPlayer highestPlayer) {
        int offset = HEIGHT / 2 - (int) highestPlayer.getY();
        gameHeight += offset;

        // Move all players
        for (NetworkPlayer player : players) {
            player.setY(player.getY() + offset);
        }


  Iterator<Platform> iterator = platforms.iterator();
     while (iterator.hasNext()) {
       Platform platform = iterator.next();
       platform.setY(platform.getY() + offset);

 if (platform.getY() > HEIGHT) {
      iterator.remove();
            }
        }

        // Add new platforms at the top
    while (platforms.size() < PLATFORM_COUNT + 3) {
      double minY = -50;
    if (!platforms.isEmpty()) {
     double highestY = HEIGHT;
      for (Platform p : platforms) {
       if (p.getY() < highestY) {
       highestY = p.getY();
           }
          }
   minY = highestY - random.nextInt(30) - 80;
     }
     createPlatform(minY);
        }
    }

    private void checkGameOver() {
    boolean allDead = true;
     for (NetworkPlayer player : players) {
      if (player.getY() > HEIGHT + 100) {
        player.setDead(true);
      }
   if (!player.isDead()) {
      allDead = false;
            }
        }
      gameOver = allDead;
    }

    public void updatePlayerInput(int playerId, String input) {
        if (playerId < players.size()) {
            NetworkPlayer player = players.get(playerId);
            String[] parts = input.split(",");

            boolean leftPressed = Boolean.parseBoolean(parts[0]);
            boolean rightPressed = Boolean.parseBoolean(parts[1]);

            player.setLeftPressed(leftPressed);
            player.setRightPressed(rightPressed);

            double moveSpeed = 4 + (gameHeight / 1000) * 0.5;
            if (leftPressed) {
                player.setVelocityX(-moveSpeed);
            } else if (rightPressed) {
                player.setVelocityX(moveSpeed);
            }
        }
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();

        // Serialize players
        sb.append("PLAYERS:");
        for (int i = 0; i < players.size(); i++) {
            NetworkPlayer player = players.get(i);
            if (i > 0) sb.append(";");
            sb.append(player.getX()).append(",")
                    .append(player.getY()).append(",")
                    .append(player.getScore()).append(",")
                    .append(player.isDead());
        }

        sb.append("|PLATFORMS:");
        for (int i = 0; i < platforms.size(); i++) {
            Platform platform = platforms.get(i);
            if (i > 0) sb.append(";");
            sb.append(platform.getX()).append(",")
                    .append(platform.getY());
        }

        sb.append("|GAME_HEIGHT:").append(gameHeight);

        return sb.toString();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinner() {
        NetworkPlayer winner = null;
        int highestScore = -1;

        for (NetworkPlayer player : players) {
            if (player.getScore() > highestScore) {
                highestScore = player.getScore();
                winner = player;
            }
        }

        return winner != null ? "Player " + (winner.getPlayerId() + 1) : "No winner";
    }

    public void reset() {
        initializeGame();
    }
}
