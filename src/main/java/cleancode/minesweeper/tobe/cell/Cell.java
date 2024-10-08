package cleancode.minesweeper.tobe.cell;

public interface Cell {
    String FLAG_SIGN = "⚑";
    String UNCHECKED_SIGN = "□";

    void flag();

    void open();

    boolean isChecked();

    boolean isOpened();

    boolean isLandMine();

    boolean hasLandMineCount();

    String getSign();
}
