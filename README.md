# Doodle Jump Game

A classic **Doodle Jump** game implementation in Java with support for both **Single Player** and **Multiplayer** modes. The game features platform jumping mechanics, increasing difficulty levels, sound effects, and network-based multiplayer gameplay.

## Features

### Single Player Mode
- Jump across platforms to increase your score
- Dodge enemies as difficulty increases
- Dynamic platform generation
- Progressive difficulty levels
- Sound effects for jumps and platforms
- Game over detection when player falls off screen

### Multiplayer Mode
- Play against another player in real-time
- Host-Client architecture using socket networking
- Separate scoreboard for each player
- Synchronized game state between players
- Support for up to 2 players per game session

### Core Gameplay
- **Physics Engine**: Realistic gravity and velocity-based movement
- **Platform System**: Randomly generated platforms with increasing difficulty
- **Score Tracking**: Real-time score updates
- **Collision Detection**: Platform-player and boundary collision handling
- **Audio Effects**: Jump and platform landing sounds

## Project Structure

```
Doodle Jump/
├── src/
│   ├── Main.java                    # Entry point with game mode selection
│   ├── Game.java                    # Single player game logic (223 lines)
│   ├── MultiplayerGame.java         # Multiplayer game logic (335 lines)
│   ├── Entity.java                  # Generic entity class for objects (62 lines)
│   ├── Platform.java                # Platform data structure (18 lines)
│   ├── GameServer.java              # Multiplayer server logic (178 lines)
│   ├── GameState.java               # Game state management (246 lines)
│   ├── NetworkPlayer.java           # Network player representation (53 lines)
│   ├── MultiplayerMain.java         # Multiplayer entry point (30 lines)
│   ├── SoundPlayer.java             # Audio playback utility (30 lines)
│   └── Sounds/
│       ├── jump.wav                 # Jump sound effect
│       └── pada.wav                 # Platform landing sound
├── dashboard.html                   # Game dashboard/UI webpage
├── Doodle Jump.iml                  # IntelliJ IDEA project file
└── README.md                        # This file

```

## Game Constants & Configuration

### Window
- **Width**: 400 pixels
- **Height**: 600 pixels

### Gameplay
- **Gravity**: 0.4
- **Jump Force**: -10
- **Platform Count**: 7 active platforms
- **Platform Size**: 100×20 pixels
- **Frame Rate**: 60 FPS (1000/60 ms per frame)

### Networking
- **Server Port**: 12345
- **Max Players**: 2
- **Network Protocol**: Socket-based TCP

## Architecture

### Core Classes

#### `Main.java`
- Game mode selection dialog (Single Player vs Multiplayer)
- Multiplayer role selection (Host vs Join)
- Frame initialization and setup

#### `Game.java`
- Single player game engine
- Platform generation algorithm
- Player movement and collision detection
- Scoring system
- Game loop implementation

#### `MultiplayerGame.java`
- Network-enabled game variant
- Synchronizes game state with opponent
- Handles both host and client roles
- Dual player rendering

#### `Entity.java`
- Base class for all game objects (players, platforms)
- Position and velocity tracking
- Rendering support

#### `GameServer.java`
- Accepts client connections
- Manages game state for multiplayer sessions
- Broadcasts game updates to all connected clients
- Handles player disconnections

#### `GameState.java`
- Encapsulates complete game state
- Serialization for network transmission
- Position, score, and difficulty data

#### `SoundPlayer.java`
- Audio playback utility
- Loads and plays WAV files

## How to Play

### Single Player
1. Run the application
2. Select **"Single Player"** from the mode selection dialog
3. Use **LEFT** and **RIGHT** arrow keys to move
4. Jump on platforms to increase your score
5. Don't fall off the bottom of the screen!

### Multiplayer
1. Run the application twice on different machines/terminals
2. First instance: Select **"Multiplayer"** → **"Host Game"**
   - Server will display: ` oodle Jump Server started on port 12345`
3. Second instance: Select **"Multiplayer"** → **"Join Game"**
   - Enter the host machine's IP address when prompted
4. Both players control their character independently
5. First to fall loses!

## Building & Running

### Prerequisites
- **Java Development Kit (JDK)** 8 or higher
- **IDE**: IntelliJ IDEA (project file included: `Doodle Jump.iml`)

### Compilation
```bash
javac src/*.java
```

### Running Single Player
```bash
java -cp src Main
```

### Running Multiplayer (Host)
```bash
java -cp src Main
# Select Multiplayer → Host Game
```

### Running Multiplayer (Client)
```bash
java -cp src Main
# Select Multiplayer → Join Game
# Enter host IP address when prompted
```

## Rendering

### Player
- Color: Light Blue (RGB: 100, 100, 200)
- Size: 30×40 pixels

### Platforms
- Color: Green (RGB: 50, 150, 50)
- Size: 100×20 pixels

### Background
- Color: White

## Audio

The game includes sound effects for:
- **jump.wav** - Player jump sound
- **pada.wav** - Platform landing sound

Sounds are stored in the `src/Sounds/` directory.

## Networking Details

### Server (Host)
- Listens on port 12345
- Waits for exactly 2 player connections
- Once connected, initiates game for both players
- Continuously broadcasts game state updates

### Client (Joiner)
- Connects to server via IP address and port 12345
- Receives game state from server
- Sends local player input to server
- Renders opponent's position from server updates

## Difficulty System

- Starts at level 1
- Increases as score progresses
- Affects platform generation and enemy behavior
- Gradually increases challenge throughout the game

## Known Limitations

- Multiplayer requires manual IP address entry
- Network synchronization is real-time (no lag compensation)
- Maximum 2 players per session
- No persistent score database

## Future Enhancements

- Add more than 2 players support
- Implement power-ups and special platforms
- Add enemy AI
- Leaderboard system
- Game pause functionality
- Improved graphics and animations
- Mobile app version
- Automated server discovery (mDNS/Bonjour)


## Author

**MD Kawsar** (mdkawsar-dev)

---
