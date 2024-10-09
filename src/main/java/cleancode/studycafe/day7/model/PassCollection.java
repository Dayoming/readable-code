package cleancode.studycafe.day7.model;

import java.util.List;
import java.util.stream.Collectors;

public class PassCollection {
    private final List<StudyCafePass> passes;

    public PassCollection(List<StudyCafePass> passes) {
        this.passes = passes;
    }

    public List<StudyCafePass> filterByPassType(StudyCafePassType passType) {
        return passes.stream()
                .filter(pass -> pass.getPassType() == passType)
                .collect(Collectors.toList());
    }
}
