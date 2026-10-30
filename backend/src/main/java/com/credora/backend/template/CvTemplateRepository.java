package com.credora.backend.template;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CvTemplateRepository extends JpaRepository<CvTemplate, String> {
    List<CvTemplate> findAllByOrderBySortOrderAsc();
}
