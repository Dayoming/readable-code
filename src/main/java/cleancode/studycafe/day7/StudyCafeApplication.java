package cleancode.studycafe.day7;

import cleancode.studycafe.day7.io.ConsoleInputHandler;
import cleancode.studycafe.day7.io.ConsoleOutputHandler;
import cleancode.studycafe.day7.io.StudyCafeFileHandler;

public class StudyCafeApplication {

    public static void main(String[] args) {
        ConsoleInputHandler consoleInputHander = new ConsoleInputHandler();
        ConsoleOutputHandler consoleOutputHandler = new ConsoleOutputHandler();
        StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();

        StudyCafePassMachine studyCafePassMachine = new StudyCafePassMachine(consoleInputHander, consoleOutputHandler, studyCafeFileHandler);
        studyCafePassMachine.run();
    }

}
