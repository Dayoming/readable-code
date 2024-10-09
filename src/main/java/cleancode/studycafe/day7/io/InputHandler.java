package cleancode.studycafe.day7.io;

import cleancode.studycafe.day7.model.StudyCafePass;
import cleancode.studycafe.day7.model.StudyCafePassType;

import java.util.List;

public interface InputHandler {
    StudyCafePassType getPassTypeSelectingUserAction();

    StudyCafePass getSelectPass(List<StudyCafePass> passes);

    boolean getLockerSelection();
}
