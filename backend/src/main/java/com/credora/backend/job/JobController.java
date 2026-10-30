package com.credora.backend.job;

import com.credora.backend.common.ApiException;
import com.credora.backend.cv.CvRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Backs src/pages/JobInfo.jsx (job list + search) and src/pages/SkillsCheck.jsx
 * (skill gap) on the frontend - those pages used to have their data
 * hard-coded in JS objects; here it comes from the database instead.
 */
@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobRepository jobRepository;
    private final JobCategoryService jobCategoryService;
    private final SkillGapService skillGapService;
    private final JobMatchService jobMatchService;
    private final CvRepository cvRepository;

    public JobController(JobRepository jobRepository, JobCategoryService jobCategoryService,
                          SkillGapService skillGapService, JobMatchService jobMatchService,
                          CvRepository cvRepository) {
        this.jobRepository = jobRepository;
        this.jobCategoryService = jobCategoryService;
        this.skillGapService = skillGapService;
        this.jobMatchService = jobMatchService;
        this.cvRepository = cvRepository;
    }

    /** Public - matches the "Filter jobs" search box in JobInfo.jsx. */
    @GetMapping
    public List<JobSummary> list(@RequestParam(required = false, defaultValue = "") String q) {
        List<Job> jobs = q.isBlank() ? jobRepository.findAll() : jobRepository.findByTitleContainingIgnoreCase(q);
        return jobs.stream().map(JobSummary::new).toList();
    }

    /** Public - the career-category TREE used to group jobs. */
    @GetMapping("/categories")
    public List<CategoryNode> categories() {
        return jobCategoryService.getTree();
    }

    /** Requires login - compares the current user's CV skills against one job. */
    @GetMapping("/{id}/skill-gap")
    public SkillGapResult skillGap(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Job not found."));
        String cvSkills = cvRepository.findByUserId(userId).map(cv -> cv.getSkills()).orElse("");
        return skillGapService.compare(job, cvSkills);
    }

    /** Requires login - every job ranked by how well the current CV's skills match it. */
    @GetMapping("/match")
    public List<JobMatchResult> match(@RequestAttribute("userId") Long userId) {
        String cvSkills = cvRepository.findByUserId(userId).map(cv -> cv.getSkills()).orElse("");
        return jobMatchService.rankJobsBySkillMatch(cvSkills);
    }
}
