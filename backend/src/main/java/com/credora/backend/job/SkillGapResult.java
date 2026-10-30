package com.credora.backend.job;

import java.util.List;

public class SkillGapResult {

    private final String jobTitle;
    private final List<String> required;
    private final List<String> matched;
    private final List<String> missing;

    public SkillGapResult(String jobTitle, List<String> required, List<String> matched, List<String> missing) {
        this.jobTitle = jobTitle;
        this.required = required;
        this.matched = matched;
        this.missing = missing;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public List<String> getRequired() {
        return required;
    }

    public List<String> getMatched() {
        return matched;
    }

    public List<String> getMissing() {
        return missing;
    }
}
