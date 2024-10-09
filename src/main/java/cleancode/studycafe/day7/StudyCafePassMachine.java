package cleancode.studycafe.day7;

import cleancode.studycafe.day7.exception.AppException;
import cleancode.studycafe.day7.io.ConsoleInputHandler;
import cleancode.studycafe.day7.io.ConsoleOutputHandler;
import cleancode.studycafe.day7.io.StudyCafeFileHandler;
import cleancode.studycafe.day7.model.StudyCafeLockerPass;
import cleancode.studycafe.day7.model.StudyCafePass;
import cleancode.studycafe.day7.model.StudyCafePassType;
import cleancode.studycafe.day7.service.PassSelector;

import java.util.List;

public class StudyCafePassMachine {
    private final ConsoleInputHandler consoleInputHandler;
    private final ConsoleOutputHandler consoleOutputHandler;
    private final StudyCafeFileHandler fileHandler;
    private final PassSelector passSelector;

    public StudyCafePassMachine(ConsoleInputHandler consoleInputHandler, ConsoleOutputHandler outputHandler, StudyCafeFileHandler fileHandler) {
        this.consoleInputHandler = consoleInputHandler;
        this.consoleOutputHandler = outputHandler;
        this.fileHandler = fileHandler;
        this.passSelector = new PassSelector(fileHandler);
    }

    public void run() {
        try {
            consoleOutputHandler.showWelcomeMessage();
            consoleOutputHandler.showAnnouncement();

            consoleOutputHandler.askPassTypeSelection();
            StudyCafePassType passType = consoleInputHandler.getPassTypeSelectingUserAction();

            StudyCafePass selectedPass = passSelector.selectPass(passType, consoleInputHandler, consoleOutputHandler);
            if (passType == StudyCafePassType.FIXED) {
                processFixedPass(selectedPass);
            } else {
                consoleOutputHandler.showPassOrderSummary(selectedPass, null);
            }

        } catch (AppException e) {
            consoleOutputHandler.showSimpleMessage(e.getMessage());
        } catch (Exception e) {
            consoleOutputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }

    private void processFixedPass(StudyCafePass selectedPass) {
        List<StudyCafeLockerPass> lockerPasses = fileHandler.readLockerPasses();
        StudyCafeLockerPass lockerPass = lockerPasses.stream()
                .filter(option -> option.getPassType() == selectedPass.getPassType()
                        && option.getDuration() == selectedPass.getDuration())
                .findFirst()
                .orElse(null);

        if (lockerPass != null) {
            consoleOutputHandler.askLockerPass(lockerPass);
            if (!consoleInputHandler.getLockerSelection()) {
                lockerPass = null; // 만약 사용자가 선택하지 않았으면 lockerPass를 null로 설정
            }
        }

        consoleOutputHandler.showPassOrderSummary(selectedPass, lockerPass);
    }
}

