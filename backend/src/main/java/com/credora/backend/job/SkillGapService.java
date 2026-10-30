package com.credora.backend.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Implements "Skill Gap -> Array/List + Searching and Comparison -> Find missing
 * skills" from the slide's feature table.
 *
 * The CV stores skills as one comma-separated string (e.g. "HTML, CSS, React").
 * Here it is split into a List, then each required skill for a job is looked
 * up in that list with a plain linear search (case-insensitive string
 * comparison) - no database involved, this is pure in-memory comparison.
 */
@Service
public class SkillGapService {

    public SkillGapResult compare(Job job, String cvSkillsCsv) {
        List<String> ownedSkills = splitSkills(cvSkillsCsv);
        List<String> required = job.getRequiredSkills();

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (String requiredSkill : required) {
            if (containsIgnoreCase(ownedSkills, requiredSkill)) {
                matched.add(requiredSkill);
            } else {
                missing.add(requiredSkill);
            }
        }

        return new SkillGapResult(job.getTitle(), required, matched, missing);
    }

    /** Linear search: walk the list comparing each entry, ignoring case/whitespace. */
    private boolean containsIgnoreCase(List<String> ownedSkills, String target) {
        for (String owned : ownedSkills) {
            if (owned.equalsIgnoreCase(target.trim())) {
                return true;
            }
        }
        return false;
    }

    private List<String> splitSkills(String csv) {
        if (csv == null || csv.isBlank()) {
            return List.of();
        }
        List<String> result = new ArrayList<>();
        for (String skill : Arrays.asList(csv.split(","))) {
            String trimmed = skill.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }
}
