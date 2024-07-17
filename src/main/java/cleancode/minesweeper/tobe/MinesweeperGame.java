package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    private static final Scanner SCANNER = new Scanner(System.in);
    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    public static final String CLOSED_CELL_SIGN = "□";
    public static final String OPENED_CELL_SIGN = "■";
    public static final String LAND_MINE_SIGN = "☼";
    public static final String FLAG_SIGN = "⚑";
    private static String[][] BOARD = new String[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static Integer[][] NEARBY_LAND_MINE_COUNTS = new Integer[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static boolean[][] LAND_MINES = new boolean[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        printGameStart();
        initializeGame();

        while (true) {
            try {
                printBoard();

                if (doesUserWinGames()) {
                    System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                    break;
                }

                if (doesUserLoseGames()) {
                    System.out.println("지뢰를 밟았습니다. GAME OVER!");
                    break;
                }

                String cellInput = getCellInput(SCANNER);
                String userActionInput = getActionInput(SCANNER);
                actOnCell(cellInput, userActionInput);

            } catch (AppException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("프로그램에 문제가 발생했습니다.");
            }

        }
    }

    private static void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);
        if (isUserInputFlag(userActionInput)) {
            BOARD[selectedRowIndex][selectedColIndex] = FLAG_SIGN;
            checkIfGameIsOver();
            return ;
        }
        if (isUserLand(userActionInput)) {
            if (isUserLandMine(selectedRowIndex, selectedColIndex)) {
                BOARD[selectedRowIndex][selectedColIndex] = LAND_MINE_SIGN;
                changeGameStatusToLose();
                return ;
            }
            open(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return ;
        }
        throw new AppException("잘못된 입력입니다.");
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isUserLandMine(int selectedRowIndex, int selectedColIndex) {
        return LAND_MINES[selectedRowIndex][selectedColIndex];
    }

    private static boolean isUserLand(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean isUserInputFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        int selectedRowIndex = convertRowFrom(cellInputRow);
        return selectedRowIndex;
    }

    private static int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        int selectedColIndex = convertColFrom(cellInputCol);
        return selectedColIndex;
    }

    private static String getActionInput(Scanner scanner) {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        String userActionInput = scanner.nextLine();
        return userActionInput;
    }

    private static String getCellInput(Scanner scanner) {
        System.out.println();
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        String cellInput = scanner.nextLine();
        return cellInput;
    }

    private static boolean doesUserLoseGames() {
        return gameStatus == -1;
    }

    private static boolean doesUserWinGames() {
        return gameStatus == 1;
    }

    private static void checkIfGameIsOver() {
        if (isAllCellIsOpened()) {
            gameStatus = 1;
        }
    }

    private static boolean isAllCellIsOpened() {
        return Arrays.stream(BOARD)
                .flatMap(Arrays::stream)
                .noneMatch(cell -> cell.equals(CLOSED_CELL_SIGN));
    }

    private static int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow) - 1;
        if (rowIndex >= BOARD_ROW_SIZE) {
            throw new AppException("잘못된 입력입니다.");
        }
        return rowIndex;
    }

    private static int convertColFrom(char cellInputCol) {
        Map<String, Integer> colMap = Map.of(
                "a", 0,
                "b", 1,
                "c", 2,
                "d", 3,
                "e", 4,
                "f", 5,
                "g", 6,
                "h", 7,
                "i", 8,
                "j", 9);
        Integer result = colMap.getOrDefault(cellInputCol + "", -1);
        if (result == -1) {
            throw new AppException("잘못된 입력입니다.");
        }
        return result;
    }

    private static void printBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                System.out.print(BOARD[row][col] + " ");
            }
            System.out.println();
        }
    }

    private static void initializeGame() {
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                BOARD[row][col] = CLOSED_CELL_SIGN;
            }
        }
        for (int i = 0; i < BOARD_COL_SIZE; i++) {
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            LAND_MINES[row][col] = true;
        }
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                int count = 0;
                if (isUserLandMine(row, col)) {
                    NEARBY_LAND_MINE_COUNTS[row][col] = 0;
                    continue;
                }
                countNearbyLandMines(row, col, count);

            }
        }
    }

    private static void countNearbyLandMines(int row, int col, int count) {
        if (row - 1 >= 0 && col - 1 >= 0 && isUserLandMine(row - 1, col - 1)) {
            count++;
        }
        if (row - 1 >= 0 && isUserLandMine(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && isUserLandMine(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isUserLandMine(row, col - 1)) {
            count++;
        }
        if (col + 1 < BOARD_COL_SIZE && isUserLandMine(row, col + 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && isUserLandMine(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && isUserLandMine(row + 1, col)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COL_SIZE && isUserLandMine(row + 1, col + 1)) {
            count++;
        }
        NEARBY_LAND_MINE_COUNTS[row][col] = count;
        return;
    }

    private static void printGameStart() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COL_SIZE) {
            return;
        }
        if (!BOARD[row][col].equals(CLOSED_CELL_SIGN)) {
            return;
        }
        if (isUserLandMine(row, col)) {
            return;
        }
        if (NEARBY_LAND_MINE_COUNTS[row][col] != 0) {
            BOARD[row][col] = String.valueOf(NEARBY_LAND_MINE_COUNTS[row][col]);
            return;
        } else {
            BOARD[row][col] = OPENED_CELL_SIGN;
        }
        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}
