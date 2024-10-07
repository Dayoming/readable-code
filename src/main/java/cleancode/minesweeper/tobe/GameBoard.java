package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;

public class GameBoard {
    private final int LAND_MINE_COUNT = 10;
    private final Cell[][] board;

    public GameBoard(int rowSize, int colSize) {
        board = new Cell[rowSize][colSize];
    }

    public void initializeGame() {
        int rowSize = getRowSize();
        int colSize = getColSize();

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                board[row][col] = Cell.create();
            }
        }

        // 지뢰 10개 만들기
        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int landMineCol = new Random().nextInt(colSize);
            int landMineRow = new Random().nextInt(rowSize);
            isOpenedCell(landMineRow, landMineCol).turnOnLandMine();
        }

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                // 지뢰가 아닌 칸에서는 선택한 셀 주변에 지뢰를 몇 개 가지고 있는지 확인
                if (isLandMineCell(row, col)) {
                    continue;
                }
                // 선택한 칸에 지뢰 갯수 삽입
                int count = countNearbyLandMines(row, col);
                Cell cell = isOpenedCell(row, col);
                cell.updateNearbyLandMineCount(count);
            }
        }
    }
    public int getRowSize() {
        return board.length;
    }

    public int getColSize() {
        return board[0].length;
    }

    public String getSign(int rowIndex, int colIndex) {
        Cell cell = isOpenedCell(rowIndex, colIndex);
        return cell.getSign();
    }

    public void flag(int rowIndex, int colIndex) {
        Cell cell = isOpenedCell(rowIndex, colIndex);
        cell.flag();
    }

    public void open(int rowIndex, int colIndex) {
        Cell cell = isOpenedCell(rowIndex, colIndex);
        cell.open();
    }

    public void openSurroundedCells(int row, int col) {
        // 보드를 벗어나는지 확인
        if (row < 0 || row >= getRowSize() || col < 0 || col >= getColSize()) {
            return;
        }
        // 보드의 셀에 접근해서 이미 열렸는지 확인
        if (isOpenedCell(row, col).isOpened()) {
            return;
        }
        // 지뢰 셀인지 확인
        if (isLandMineCell(row, col)) {
            return;
        }

        open(row, col);

        // 지뢰 숫자를 가지고 있는 칸이라면 보드에 숫자를 표시해주고 재귀를 멈춘다.
        if (doesCellHaveLandMineCount(row, col)) {
            return;
        }

        // 선택한 셀 주변에 있는 셀들을 재귀를 통해 하나씩 탐색
        openSurroundedCells(row - 1, col - 1);
        openSurroundedCells(row - 1, col);
        openSurroundedCells(row - 1, col + 1);
        openSurroundedCells(row, col - 1);
        openSurroundedCells(row, col + 1);
        openSurroundedCells(row + 1, col - 1);
        openSurroundedCells(row + 1, col);
        openSurroundedCells(row + 1, col + 1);
    }
    public boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return isOpenedCell(selectedRowIndex, selectedColIndex).isLandMine();
    }

    public boolean isAllCellChecked() {
        // BOARD를 하나씩 순회
        return Arrays.stream(board) // Stream<String[]>
                .flatMap(Arrays::stream) // Stream<String>
                .allMatch(Cell::isChecked);
    }


    private boolean doesCellHaveLandMineCount(int row, int col) {
        return isOpenedCell(row, col).hasLandMineCount();
    }

    private Cell isOpenedCell(int row, int col) {
        return findCell(row, col);
    }

    private Cell findCell(int rowIndex, int colIndex) {
        return board[rowIndex][colIndex];
    }

    private int countNearbyLandMines(int row, int col) {
        int rowSize = board.length;
        int colSize = board[0].length;
        int count = 0;

        // 왼쪽 위 칸이 보드를 벗어나지 않고 지뢰가 존재하면 count 1 증가
        if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
            count++;
        }
        // 위 칸이 보드를 벗어나지 않고 지뢰가 존재하면 count 1 증가
        if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
            count++;
        }
        // 오른쪽 위 칸이 보드를 벗어나지 않고 지뢰가 존재하면 count 1 증가
        if (row - 1 >= 0 && col + 1 < colSize && isLandMineCell(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
            count++;
        }
        if (col + 1 < colSize && isLandMineCell(row, col + 1)) {
            count++;
        }
        if (row + 1 < rowSize && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < rowSize && isLandMineCell(row + 1, col)) {
            count++;
        }
        if (row + 1 < rowSize && col + 1 < colSize && isLandMineCell(row + 1, col + 1)) {
            count++;
        }
        return count;
    }

}
