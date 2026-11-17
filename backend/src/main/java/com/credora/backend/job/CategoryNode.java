package com.credora.backend.job;

import com.credora.backend.ds.DynamicArray;
import java.util.List;

/**
 * One node of the career-category TREE sent to the frontend: its own jobs
 * plus its child categories, built by a recursive (depth-first) traversal
 * in JobCategoryService. Storage is the hand-written {@link DynamicArray}, not
 * java.util.ArrayList - converted to java.util.List only in the getters,
 * where Spring/Jackson needs it to serialize the HTTP response.
 */
public class CategoryNode {

    private final Long id;
    private final String name;
    private final DynamicArray<JobSummary> jobs = new DynamicArray<>();
    private final DynamicArray<CategoryNode> children = new DynamicArray<>();

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

    public void addJob(JobSummary job) {
        jobs.add(job);
    }

    public void addChild(CategoryNode child) {
        children.add(child);
    }

    public List<JobSummary> getJobs() {
        return jobs.toJavaList();
    }

    public List<CategoryNode> getChildren() {
        return children.toJavaList();
    }
}
