package com.credora.backend.template;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public endpoint, no login required - the frontend needs the template list
 * before a user has even logged in (see TEMPLATES in src/templates.js).
 * Reads straight from PostgreSQL via CvTemplateRepository.
 */
@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    private final CvTemplateRepository templateRepository;

    public TemplateController(CvTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @GetMapping
    public List<CvTemplate> list() {
        return templateRepository.findAllByOrderBySortOrderAsc();
    }
}
