package com.credora.backend.job;

import com.credora.backend.ds.DynamicArray;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Builds the career-category TREE (slide: "Career Categories -> Tree -> Traversal").
 *
 * The categories table is self-referencing (each row points at its parent),
 * so turning it into the nested JSON the frontend wants is a classic
 * depth-first tree traversal: visit a category, list the jobs that belong to
 * it directly, then recurse into each child category. The roots are
 * collected in a hand-written {@link DynamicArray}, not java.util.ArrayList, and
 * only converted to java.util.List at the very end, where Spring needs it
 * to serialize the HTTP response.
 */
@Service
public class JobCategoryService {

    private final JobCategoryRepository categoryRepository;
    private final JobRepository jobRepository;

    public JobCategoryService(JobCategoryRepository categoryRepository, JobRepository jobRepository) {
        this.categoryRepository = categoryRepository;
        this.jobRepository = jobRepository;
    }

    /** Returns every root category as a fully expanded tree. */
    public List<CategoryNode> getTree() {
        DynamicArray<CategoryNode> roots = new DynamicArray<>();
        for (JobCategory root : categoryRepository.findByParentIsNull()) {
            roots.add(buildNode(root));
        }
        return roots.toJavaList();
    }

    /** Depth-first traversal: node, then its jobs, then each child subtree. */
    private CategoryNode buildNode(JobCategory category) {
        CategoryNode node = new CategoryNode(category.getId(), category.getName());

        for (Job job : jobRepository.findByCategory(category)) {
            node.addJob(new JobSummary(job));
        }

        for (JobCategory child : categoryRepository.findByParent(category)) {
            node.addChild(buildNode(child));
        }

        return node;
    }
}
