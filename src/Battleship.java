import java.io.File;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Battleship {
    // Welcome aboard, Captain!
    // Your job is to commandeer our battleship, and take out our enemies!
    // All you need to do is tell us where to fire. Leave the rest to us!

    // To enter test mode, select game mode 1 during program start up.
    // The following is only true in test mode:
    // On the game board, the number 1 is a ship, while the number 2 is a mine.
    // If you hit a ship, the number 7 will replace the location of the hit.
    // If your cannon misses, the number 8 will replace of the location that you chose.
    // If you hit a mine, the number 5 will replace the location of the hit.

    int rowCount;
    int columnCount;
    int[][] gameBoard;
    int gameMode;
    int score;
    int tempScore = 0;
    static int highScore;
    int turn = 0;
    int remainingShips;
    boolean sunkMyBattleShips = false;
    int tempRow;
    int tempColumn;
    boolean outOfBounds = false;

    public void init() {
        Scanner in = new Scanner(System.in);
        System.out.println("The top left corner of the game board is 0,0");
        System.out.println("Enter game mode (0 to play, 1 to test): "); //set play/test mode
        int input = in.nextInt();
        if (input > 1) {
            gameMode = 1;
            System.out.println("Number entered was greater than 1, reducing it to 1... \n");
        } else if (input < 0) {
            gameMode = 0;
            System.out.println("Number entered was less than 0, increasing it to 0... \n");
        } else if (input == 1 || input == 0) {
            gameMode = input;
        }

        if (gameMode == 1) {
            System.out.println("\n \033[1mWall Hacks Enabled!\033[0m \n");
        }

        System.out.println("Enter row count greater than 2 and less than 11: ");
        int tempRow = in.nextInt(); //get row number

        if (tempRow < 3) {
            rowCount = 3;
        } else if (tempRow > 10) {
            rowCount = 10;
        } else {
            rowCount = tempRow;
        }

        System.out.println("Enter column count greater than 2 and less than 11: ");
        int tempColumn = in.nextInt(); //get column number

        if (tempColumn < 3) {
            columnCount = 3;
        } else if (tempColumn > 10) {
            columnCount = 10;
        } else {
            columnCount = tempColumn;
        }
        System.out.println("\n");
        //Create game board
        this.BattleShipBoard(rowCount, columnCount); //creates game board using rowCount and columnCount
    } //set up initial variables (columns, rows), etc.

    public void write(int score) {
        try {
            int bestScore = read();
            if (bestScore != -1) {
                PrintWriter p = new PrintWriter(new File("highscores.txt"));

                if (score < bestScore) {
                    p.print(score);
                    System.out.println("Best Score: " + score);
                }else {
                    p.print(bestScore);
                }
                p.close();            }
            else{
                PrintWriter p = new PrintWriter(new File("highscores.txt"));
                p.print(score);
                p.close();
                System.out.println("Best Score: " + score);
            }
        } catch (Exception e){

        }
    }
    public int read() {
        try {
            Scanner s = new Scanner(new File ("highscores.txt"));
            return s.nextInt();
        } catch (Exception e){
            return -1;
        }
    }

    public int[][] BattleShipBoard(int x, int y) {
        rowCount = x;
        columnCount = y;
        return gameBoard = new int[rowCount][columnCount];
    } //creates game board with dimensions x and y, specified by the user
    public void displayMatrix() {
        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[x].length; y++) {
                System.out.printf("%4d", gameBoard[x][y]);
            }
            System.out.println();
        }
        System.out.println("\n");
    } //prints array
    public String determineShipOrientation() {
        if (Math.random() > 0.5) {
            return "vertical";
        } else {
            return "horizontal";
        }
    } //generates a random direction to place a ship in
    public int determineShipLocation() {
        int value;
        Random r = new Random();
        value = r.nextInt(gameBoard.length - 2);
        return value;
    }
    public int determineMineLocation() {
        int value;
        Random r = new Random();
        value = r.nextInt(gameBoard.length);
        return value;
    }
    public void validAttack(int row, int column) {
        tempRow = row;
        tempColumn = column;
        if (!this.isHit(row, column)) {
            if (!isNear(row + 1, column)) {
                if (!isNear(row - 1, column)) {
                    if (!isNear(row, column + 1)) {
                        if (!isNear(row, column - 1)) {
                        }
                    }
                }
            }
        }
    } //determines whether an attack is going to hit something or miss
    public boolean isHit(int row, int column) {
        try {
            outOfBounds = false;
            if (this.gameBoard[column][row] == 1) { // if chosen tile contains a ship
                System.out.println("We have a hit, Captain!");
                remainingShips -= 1;
                this.gameBoard[column][row] = 7;
                return true;
            } else if (this.gameBoard[column][row] == 2) { // if chosen tile contains a mine
                tempScore++;
                System.out.println("We've hit a mine, Captain!");
                this.gameBoard[column][row] = 5;
                return true;
            } else if (this.gameBoard[column][row] == 7 || this.gameBoard[column][row] == 5) {
                System.out.println("We've already fired our cannons there, Captain!");
                tempScore++;
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Our cannons cannot fire that far, Captain!");
            outOfBounds = true;
        }
        return false;
    } //used in validAttack to determine if attack is going to hit
    public boolean isNear(int row, int column) {
        if (outOfBounds == true) {
            return false;
        }
        if (row < 0 || row >= this.gameBoard.length) {
            System.out.println("A Miss, but Close");
            return true;
        } else if (column < 0 || column >= this.gameBoard.length) {
            System.out.println("A Miss, but Close");
            this.gameBoard[tempColumn][tempRow] = 8;
            return true;
        } else if (gameBoard[tempColumn][tempRow] == 0) {
            System.out.println("A Miss, but Very Close");
            this.gameBoard[tempColumn][tempRow] = 8;
            return true;
        }
        else {
            System.out.println("A total miss, Captain!");
            this.gameBoard[tempColumn][tempRow] = 8;
            return true;
        }
    }//used in validAttack to determine if attack is going to be near a ship
    public void createBattleShips() {
        // This loop successfully generates the correct number of ships about 80-90% of the time, for an unknown reason.

        int mineCount = -100;
        int shipCount = -100;

        if (columnCount * rowCount > 8 && columnCount * rowCount <= 16) { //determine amount of ships and mines
            mineCount = 1;
            shipCount = 1;
            remainingShips = 3;
        } else if (columnCount * rowCount > 16 && columnCount * rowCount <= 36) {
            mineCount = 2;
            shipCount = 2;
            remainingShips = 6;
        } else if (columnCount * rowCount > 36) {
            mineCount = 3;
            shipCount = 3;
            remainingShips = 9;
        }

        System.out.printf("There are %d ships on the board. \n", shipCount);
        while (mineCount > 0) {

            int mineLocationX = determineMineLocation();
            int mineLocationY = determineMineLocation();

            if (gameBoard[mineLocationX][mineLocationY] == 0) {
                gameBoard[mineLocationX][mineLocationY] = 2;
                mineCount -= 1;
            }
        }
        while (shipCount > 0) {
            String orientation = determineShipOrientation();
            int shipLocationX = determineShipLocation();
            int shipLocationY = determineShipLocation();
            if (orientation.equals("horizontal")) { //set ship horizontally if horizontal was rolled
                if (gameBoard[shipLocationX][shipLocationY] == 0) {
                    if (gameBoard[shipLocationX + 1][shipLocationY] == 0) {
                        if (gameBoard[shipLocationX + 2][shipLocationY] == 0) {
                            gameBoard[shipLocationX][shipLocationY] = 1;
                            gameBoard[shipLocationX + 1][shipLocationY] = 1;
                            gameBoard[shipLocationX + 2][shipLocationY] = 1;
                            if (gameMode == 1){
                                System.out.println("Ship: " + shipLocationY + "," + shipLocationX);
                            }
                            shipCount -= 1;

                        }
                    }
                }
            }


            if (orientation.equals("vertical")) { //set ship vertically if vertical was rolled
                if (gameBoard[shipLocationX][shipLocationY] == 0) {
                    if (gameBoard[shipLocationX][shipLocationY + 1] == 0) {
                        if (gameBoard[shipLocationX][shipLocationY + 2] == 0) {
                            gameBoard[shipLocationX][shipLocationY] = 1;
                            gameBoard[shipLocationX][shipLocationY + 1] = 1;
                            gameBoard[shipLocationX][shipLocationY + 2] = 1;
                            if (gameMode == 1){
                                System.out.println("Ship: " + shipLocationY + "," + shipLocationX);
                            }
                            shipCount -= 1;
                        }
                    }
                }
            }

        }

        if (gameMode == 1) { //If testing mode is enabled then the game board will be displayed
            this.displayMatrix();
        }
    } //sets the ships and mines on the board using determineShipLocation and determineMineLocation
    public static void main(String[] args) {
        Battleship ship = new Battleship();
        ship.init();
        ship.createBattleShips();
        if (ship.read() == -1){
            System.out.println("Best Score: Not Set");
        }
        else{
            System.out.println("Best Score: " + ship.read());
        }
        while (ship.sunkMyBattleShips == false) {
            Scanner input = new Scanner(System.in);
            if (ship.turn == 0) {
                ship.turn++;
            }
            System.out.println("\033[1mTurn #\033[0m" + " " + ship.turn);
            System.out.println("Captain, how far shall we fire? (Enter X-Coordinate)");
            int attackX = input.nextInt();
            System.out.println("Captain, how high shall we fire? (Enter Y-Coordinate)");
            int attackY = input.nextInt();
            ship.validAttack(attackX, attackY);
            ship.score = ship.tempScore + ship.turn;
            if (ship.gameMode == 1) {
                ship.displayMatrix();
            }
            System.out.println("Current Score: " + ship.score);
            ship.turn++;
            if (ship.remainingShips == 0) {
                ship.sunkMyBattleShips = true;
                System.out.println("Captain! We're victorious!");
            }
        }
        ship.write(ship.score);
    }
}


