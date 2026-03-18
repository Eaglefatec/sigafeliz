package com.eaglefatec.sigafeliz.engine;

import com.eaglefatec.sigafeliz.model.ScheduledAula;

import java.util.ArrayList;
import java.util.List;

/**
 * Result of the scheduling engine containing the generated aulas and any
 * warnings.
 */
public class ScheduleResult {
    private final List<ScheduledAula> aulas;
    private final List<String> warnings;
    private final List<String> errors;

    public ScheduleResult() {
        this.aulas = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    public List<ScheduledAula> getAulas() {
        return aulas;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }
}
