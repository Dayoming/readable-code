package cleancode.minesweeper.tobe;

public class GameApplication {
    // 지뢰찾기 게임을 실행시키는 책임만 남겨둠
    public static void main(String[] args) {
        Minesweeper minesweeper = new Minesweeper();
        minesweeper.run();
    }
}
