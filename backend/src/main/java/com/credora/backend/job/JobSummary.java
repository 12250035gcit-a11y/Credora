package com.credora.backend.job;

import java.util.List;

/** What the frontend sees for one job - no JPA entity details leak out. */
public class JobSummary {

    private final Long id;
    private final String title;
    private final String focus;
    private final String description;
    private final List<String> requiredSkills;

    public JobSummary(Job job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.focus = job.getFocus();
        this.description = job.getDescription();
        this.requiredSkills = job.getRequiredSkills();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getFocus() {
        return focus;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }
}
