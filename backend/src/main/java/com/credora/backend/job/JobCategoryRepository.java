package com.credora.backend.job;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
    List<JobCategory> findByParentIsNull();
    List<JobCategory> findByParent(JobCategory parent);
}
