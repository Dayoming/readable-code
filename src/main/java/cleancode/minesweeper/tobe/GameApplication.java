package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.gamelavel.Advanced;
import cleancode.minesweeper.tobe.gamelavel.Beginner;
import cleancode.minesweeper.tobe.gamelavel.GameLevel;
import cleancode.minesweeper.tobe.gamelavel.Middle;

public class GameApplication {
    // 지뢰찾기 게임을 실행시키는 책임만 남겨둠
    public static void main(String[] args) {
        GameLevel gameLevel = new Beginner();

        Minesweeper minesweeper = new Minesweeper(gameLevel);
        minesweeper.initialize();
        minesweeper.run();
    }
}
