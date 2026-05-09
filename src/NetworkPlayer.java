import java.awt.Color;

public class NetworkPlayer {
    private int playerId;
    private double x, y, width, height;
    private Color color;
    private double velocityX, velocityY;
    private int score;
    private boolean dead;
    private boolean leftPressed;
    private boolean rightPressed;

    public NetworkPlayer(int playerId, double x, double y, double width, double height, Color color) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.velocityX = 0;
        this.velocityY = 0;
        this.score = 0;
        this.dead = false;
        this.leftPressed = false;
        this.rightPressed = false;
    }

 public int getPlayerId() { return playerId; }
 public double getX() { return x; }
 public double getY() { return y; }
 public double getWidth() { return width; }
 public double getHeight() { return height; }
 public Color getColor() { return color; }
 public double getVelocityX() { return velocityX; }
 public double getVelocityY() { return velocityY; }
 public int getScore() { return score; }
 public boolean isDead() { return dead; }
 public boolean isLeftPressed() { return leftPressed; }
 public boolean isRightPressed() { return rightPressed; }


public void setX(double x) { this.x = x; }
public void setY(double y) { this.y = y; }
public void setVelocityX(double velocityX) { this.velocityX = velocityX; }
public void setVelocityY(double velocityY) { this.velocityY = velocityY; }
public void setDead(boolean dead) { this.dead = dead; }
public void setLeftPressed(boolean leftPressed) { this.leftPressed = leftPressed; }
 public void setRightPressed(boolean rightPressed) { this.rightPressed = rightPressed; }

public void addScore(int points) {
  this.score += points;
    }
}
