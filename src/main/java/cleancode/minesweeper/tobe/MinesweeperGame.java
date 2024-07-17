package cleancode.minesweeper.tobe;

import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    private static String[][] board = new String[8][10];
    private static Integer[][] landMineCounts = new Integer[8][10];
    private static boolean[][] landMines = new boolean[8][10];
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        printGameStart();

        Scanner scanner = new Scanner(System.in);

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
                String cellInput = getCellInput(scanner);
                String userActionInput = getActionInput(scanner);
                int selectedColIndex = getSelectedColIndex(cellInput);
                int selectedRowIndex = getSelectedRowIndex(cellInput);
                if (isUserInputFlag(userActionInput)) {
                    board[selectedRowIndex][selectedColIndex] = "⚑";
                    checkIfGameIsOver();
                } else if (isUserLand(userActionInput)) {
                    if (isUserLandMine(selectedRowIndex, selectedColIndex)) {
                        board[selectedRowIndex][selectedColIndex] = "☼";
                        gameStatus = -1;
                        continue;
                    } else {
                        open(selectedRowIndex, selectedColIndex);
                    }
                    checkIfGameIsOver();
                } else {
                    System.out.println("잘못된 번호를 선택하셨습니다.");
                }
            } catch (AppException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("프로그램에 문제가 발생했습니다.");
            }

        }
    }

    private static boolean isUserLandMine(int selectedRowIndex, int selectedColIndex) {
        return landMines[selectedRowIndex][selectedColIndex];
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
        boolean isAllOpend = true;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                if ("□".equals(board[row][col])) {
                    isAllOpend = false;
                }
            }
        }
        return isAllOpend;
    }

    private static int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow) - 1;
        if (rowIndex >= 8) {
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
        for (int row = 0; row < 8; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < 10; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }

    private static void initializeGame() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                board[row][col] = "□";
            }
        }
        for (int i = 0; i < 10; i++) {
            int col = new Random().nextInt(10);
            int row = new Random().nextInt(8);
            landMines[row][col] = true;
        }
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                int count = 0;
                if (!isUserLandMine(row, col)) {
                    if (row - 1 >= 0 && col - 1 >= 0 && isUserLandMine(row - 1, col - 1)) {
                        count++;
                    }
                    if (row - 1 >= 0 && isUserLandMine(row - 1, col)) {
                        count++;
                    }
                    if (row - 1 >= 0 && col + 1 < 10 && isUserLandMine(row - 1, col + 1)) {
                        count++;
                    }
                    if (col - 1 >= 0 && isUserLandMine(row, col - 1)) {
                        count++;
                    }
                    if (col + 1 < 10 && isUserLandMine(row, col + 1)) {
                        count++;
                    }
                    if (row + 1 < 8 && col - 1 >= 0 && isUserLandMine(row + 1, col - 1)) {
                        count++;
                    }
                    if (row + 1 < 8 && isUserLandMine(row + 1, col)) {
                        count++;
                    }
                    if (row + 1 < 8 && col + 1 < 10 && isUserLandMine(row + 1, col + 1)) {
                        count++;
                    }
                    landMineCounts[row][col] = count;
                    continue;
                }
                landMineCounts[row][col] = 0;
            }
        }
    }

    private static void printGameStart() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 10) {
            return;
        }
        if (!board[row][col].equals("□")) {
            return;
        }
        if (isUserLandMine(row, col)) {
            return;
        }
        if (landMineCounts[row][col] != 0) {
            board[row][col] = String.valueOf(landMineCounts[row][col]);
            return;
        } else {
            board[row][col] = "■";
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
