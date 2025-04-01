# Lab-07C-BattleShip
 ```mermaid
classDiagram
    class Game {
        -board : Board
        -player1 : Player
        -player2 : Player
        +startGame()
        +checkWinCondition()
        +manageTurns()
        +initializeGame()
    }

    class Player {
        -name : String
        -ships : List<Ship>
        +placeShips()
        +fireShot()
        +getName()
    }

    class Board {
        -grid : Cell[][]
        +placeShip(ship : Ship, location : Coordinate)
        +fireShot(coordinate : Coordinate) : Cell
        +isCellOccupied(coordinate : Coordinate) : boolean
        +getBoardSize() : int
    }

    class Ship {
        -type : String
        -size : int
        -orientation : String
        -health : int
        +isSunk() : boolean
        +getHealth() : int
        +hit()
    }

    class Cell {
        -state : String //e.g., "water", "ship", "hit", "miss"
        +getState() : String
        +setState(state : String)
    }

    class InputHandler {
        +getShipPlacement() : Coordinate[]
        +getShotCoordinate() : Coordinate
    }

    class OutputHandler {
        +displayBoard(board : Board)
        +displayMessage(message : String)
        +displayWinner(player : Player)
    }

    class AI {
        +placeShips(board : Board)
        +chooseShot(board : Board) : Coordinate
    }

    Game "1" -- "*" Player : has
    Game "1" -- "1" Board : uses
    Game "1" -- "1" InputHandler : uses
    Game "1" -- "1" OutputHandler : uses
    Player "1" -- "*" Ship : has
    Board "1" -- "*" Cell : contains
    Player "1" -- "1" AI : may have (optional)


```