package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.gamelavel.Beginner;
import cleancode.minesweeper.tobe.gamelavel.GameLevel;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;

public class GameApplication {
    // 지뢰찾기 게임을 실행시키는 책임만 남겨둠
    public static void main(String[] args) {
        GameLevel gameLevel = new Beginner();
        InputHandler inputHandler = new ConsoleInputHandler();
        OutputHandler outputHandler = new ConsoleOutputHandler();

        Minesweeper minesweeper = new Minesweeper(gameLevel, inputHandler, outputHandler);
        minesweeper.initialize();
        minesweeper.run();
    }

    /**
     * DIP (Dependency Inversion Principle) : 의존성 역전의 법칙
     * 고수준 모듈과 저수준 모듈이 서로 직접적으로 의존하는 것이 아니라 추상화에 의존해야 한다
     *
     * DI (Dependency Injection) : 의존성 주입, "3"
     * 필요한 의존성을 내가 직접 생성하는 것이 아니라 외부에서 주입받겠다는 것
     * ex) A 객체가 B 객체를 필요로 한다. 이 둘이 의존성을 갖고 싶은데, A가 B를 생성해서 사용하는 것이 아니라 의존성을
     * 주입받고 싶다. 생성자를 통해 주입받고자 할 때, 이 둘은 주입받는다는 행위를 해 줄 수 없다.
     * 그렇기 때문에 제 3자가 항상 이 둘의 의존성을 맺어줄 수 밖에 없다.
     * 이 역할은 Spring에서 Spring Context(IoC Container)가 해준다.
     * 이것들이 런타임 시점에 객체의 결정과 주입이 일어난다.
     *
     * IoC (Inversion of Control) : 제어의 역전
     * 프로그램의 흐름을 개발자가 아닌 프레임워크가 담당하도록 하는 것.
     * 제어의 순방향 -> 프로그램이라는 것은 개발자가 만드는 것이기 때문에 프로그램은 개발자가 제어해야 한다.
     * 제어의 역방향 -> 내가 만든 프로그램이 이미 만들어진 프레임워크라는 공장 안에서 어떤 톱니바퀴 하나처럼 동작하는 것이다.
     * 내가 만들고자 하는 프로그램을 만들어 프레임워크 안에 끼우면 돌아가게 되는 것(프레임워크가 메인)이다.
     * 내 코드는 프레임워크의 일부가 되기 때문에 제어가 프레임워크 쪽으로 넘어가게 된다.
     * IoC Container가 객체를 직접적으로 생성하거나, 생명주기를 관리해주거나, 의존성 주입을 해준다.
     * 개발자는 객체의 생성과 소멸, 주입을 신경 쓸 필요 없이 쓰기만 하면 된다.
     * 어노테이션을 사용해서 Bean들을 생성하는 것을 IoC Container가 담당한다.
     */
}
