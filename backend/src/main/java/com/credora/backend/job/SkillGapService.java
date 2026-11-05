package com.credora.backend.job;

import com.credora.backend.ds.MyList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Implements "Skill Gap -> Array/List + Searching and Comparison -> Find missing
 * skills" from the slide's feature table.
 *
 * The CV stores skills as one comma-separated string (e.g. "HTML, CSS, React").
 * Here it is split into a hand-written {@link MyList}, then each required
 * skill for a job is looked up in that list with a plain linear search
 * (case-insensitive string comparison) - no database and no java.util.List
 * involved, this is pure in-memory comparison over our own list structure.
 * Results are converted to java.util.List only when building the response
 * DTO, which Spring needs to serialize.
 */
@Service
public class SkillGapService {

    public SkillGapResult compare(Job job, String cvSkillsCsv) {
        MyList<String> ownedSkills = splitSkills(cvSkillsCsv);
        List<String> required = job.getRequiredSkills();

        MyList<String> matched = new MyList<>();
        MyList<String> missing = new MyList<>();

        for (String requiredSkill : required) {
            if (containsIgnoreCase(ownedSkills, requiredSkill)) {
                matched.add(requiredSkill);
            } else {
                missing.add(requiredSkill);
            }
        }

        return new SkillGapResult(job.getTitle(), required, matched.toJavaList(), missing.toJavaList());
    }

    /** Linear search: walk the list comparing each entry, ignoring case/whitespace. */
    private boolean containsIgnoreCase(MyList<String> ownedSkills, String target) {
        for (String owned : ownedSkills) {
            if (owned.equalsIgnoreCase(target.trim())) {
                return true;
            }
        }
        return false;
    }

    private MyList<String> splitSkills(String csv) {
        MyList<String> result = new MyList<>();
        if (csv == null || csv.isBlank()) {
            return result;
        }
        for (String skill : csv.split(",")) {
            String trimmed = skill.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }
}
