package com.credora.backend.job;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Implements "Job Matching -> Array/List + Comparison + Sorting -> Rank matches"
 * from the slide's feature table.
 *
 * Step 1 (Comparison): for every job, reuse SkillGapService's linear search to
 * count how many of its required skills the user already has.
 * Step 2 (Sorting): put every result in a List and rank it best-first with a
 * hand-written insertion sort, so the algorithm itself is visible in code
 * instead of being hidden behind a one-line Collections.sort call.
 */
@Service
public class JobMatchService {

    private final JobRepository jobRepository;
    private final SkillGapService skillGapService;

    public JobMatchService(JobRepository jobRepository, SkillGapService skillGapService) {
        this.jobRepository = jobRepository;
        this.skillGapService = skillGapService;
    }

    public List<JobMatchResult> rankJobsBySkillMatch(String cvSkillsCsv) {
        List<JobMatchResult> results = new ArrayList<>();

        for (Job job : jobRepository.findAll()) {
            SkillGapResult gap = skillGapService.compare(job, cvSkillsCsv);
            int total = gap.getRequired().size();
            int matched = gap.getMatched().size();
            int score = total == 0 ? 0 : Math.round((matched * 100f) / total);
            results.add(new JobMatchResult(job.getId(), job.getTitle(), matched, total, score));
        }

        insertionSortByScoreDescending(results);
        return results;
    }

    /**
     * Classic insertion sort: grow a sorted prefix of the list one element at a
     * time, shifting bigger-score items to the right to make room for the
     * element currently being inserted. O(n^2), easy to read, more than fast
     * enough for a handful of job listings.
     */
    private void insertionSortByScoreDescending(List<JobMatchResult> results) {
        for (int i = 1; i < results.size(); i++) {
            JobMatchResult current = results.get(i);
            int j = i - 1;
            while (j >= 0 && results.get(j).getScorePercent() < current.getScorePercent()) {
                results.set(j + 1, results.get(j));
                j--;
            }
            results.set(j + 1, current);
        }
    }
}
