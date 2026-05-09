import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.awt.Color;

public class GameServer {
    private static final int PORT = 12345;
    private static final int MAX_PLAYERS = 2;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private GameState gameState;
    private Timer gameTimer;
    private boolean gameRunning;

    public GameServer() {
        clients = new ArrayList<>();
        gameState = new GameState();
        gameRunning = false;
    }

 public void start() {
  try {
     serverSocket = new ServerSocket(PORT);
     System.out.println("🎮 Doodle Jump Server started on port " + PORT);
     System.out.println("⏳ Waiting for players to connect...");

while (clients.size() < MAX_PLAYERS) {
   Socket clientSocket = serverSocket.accept();
    ClientHandler client = new ClientHandler(clientSocket, clients.size());
     clients.add(client);
     new Thread(client).start();

     System.out.println("✅ Player " + clients.size() + " connected!");

     if (clients.size() == MAX_PLAYERS) {
      startGame();
        }
      }
  }
  catch (IOException e) {
    System.err.println("❌ Server error: " + e.getMessage());
        }
    }

    private void startGame() {
        gameRunning = true;
        gameState.initializeGame();


 broadcastGameState();


  gameTimer = new Timer();
  gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
    if (gameRunning) {
      gameState.update();
      broadcastGameState();

  if (gameState.isGameOver()) {
     endGame();
         }
   }
            }
        }, 0, 1000 / 60); // 60 FPS

  System.out.println("🚀 Game started with " + clients.size() + " players!");
    }

private void broadcastGameState() {
   String stateData = gameState.serialize();
     for (ClientHandler client : clients) {
            client.sendMessage("GAME_STATE:" + stateData);
        }
    }

    private void endGame() {
        gameRunning = false;
        if (gameTimer != null) {
            gameTimer.cancel();
        }

        String winner = gameState.getWinner();
        for (ClientHandler client : clients) {
            client.sendMessage("GAME_OVER:" + winner);
        }

        System.out.println("🏆 Game ended. Winner: " + winner);
    }

    public void handlePlayerInput(int playerId, String input) {
        if (gameRunning) {
            gameState.updatePlayerInput(playerId, input);
        }
    }

    public void restartGame() {
        gameState.reset();
        gameRunning = true;
        broadcastGameState();

        if (gameTimer != null) {
            gameTimer.cancel();
        }

        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
           if (gameRunning) {
            gameState.update();
            broadcastGameState();

          if (gameState.isGameOver()) {
             endGame();
               }
           }
            }
        }, 0, 1000 / 60);
    }

    class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private int playerId;

        public ClientHandler(Socket socket, int playerId) {
            this.socket = socket;
            this.playerId = playerId;
        }

      @Override
      public void run() {
         try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

                // Send player ID to client
       out.println("PLAYER_ID:" + playerId);

       String message;
      while ((message = in.readLine()) != null) {
        if (message.startsWith("INPUT:")) {
           String input = message.substring(6);
           handlePlayerInput(playerId, input);
            }
        else if (message.equals("RESTART"))
        {
     restartGame();
         }
            }
            }
         catch (IOException e) {
            System.err.println("❌ Client handler error: " + e.getMessage());
            } finally {
         try {
               socket.close();
                }
         catch (IOException e) {
           System.err.println("❌ Error closing socket: " + e.getMessage());
                }
            }
        }

        public void sendMessage(String message) {
            if (out != null) {
                out.println(message);
            }
        }
    }

    public static void main(String[] args) {
        new GameServer().start();
    }
}
