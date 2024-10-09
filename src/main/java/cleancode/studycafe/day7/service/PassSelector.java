package cleancode.studycafe.day7.service;


import cleancode.studycafe.day7.io.ConsoleInputHandler;
import cleancode.studycafe.day7.io.ConsoleOutputHandler;
import cleancode.studycafe.day7.io.StudyCafeFileHandler;
import cleancode.studycafe.day7.model.PassCollection;
import cleancode.studycafe.day7.model.StudyCafePass;
import cleancode.studycafe.day7.model.StudyCafePassType;

import java.util.List;

public class PassSelector {
    private final StudyCafeFileHandler fileHandler;

    public PassSelector(StudyCafeFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public StudyCafePass selectPass(StudyCafePassType passType, ConsoleInputHandler consoleInputHander, ConsoleOutputHandler outputHandler) {
        List<StudyCafePass> studyCafePasses = fileHandler.readStudyCafePasses();
        PassCollection passCollection = new PassCollection(studyCafePasses);
        List<StudyCafePass> passes = passCollection.filterByPassType(passType);

        outputHandler.showPassListForSelection(passes);
        return consoleInputHander.getSelectPass(passes);
    }
}
