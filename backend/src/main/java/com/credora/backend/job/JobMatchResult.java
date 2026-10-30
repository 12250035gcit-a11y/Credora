package com.credora.backend.job;

public class JobMatchResult {

    private final Long jobId;
    private final String jobTitle;
    private final int matchedCount;
    private final int totalRequired;
    private final int scorePercent;

    public JobMatchResult(Long jobId, String jobTitle, int matchedCount, int totalRequired, int scorePercent) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.matchedCount = matchedCount;
        this.totalRequired = totalRequired;
        this.scorePercent = scorePercent;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public int getMatchedCount() {
        return matchedCount;
    }

    public int getTotalRequired() {
        return totalRequired;
    }

    public int getScorePercent() {
        return scorePercent;
    }
}
