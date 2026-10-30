package com.credora.backend.job;

import java.util.ArrayList;
import java.util.List;

/**
 * One node of the career-category TREE sent to the frontend: its own jobs
 * plus its child categories, built by a recursive (depth-first) traversal
 * in JobCategoryService.
 */
public class CategoryNode {

    private final Long id;
    private final String name;
    private final List<JobSummary> jobs = new ArrayList<>();
    private final List<CategoryNode> children = new ArrayList<>();

    public CategoryNode(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<JobSummary> getJobs() {
        return jobs;
    }

    public List<CategoryNode> getChildren() {
        return children;
    }
}
