package com.credora.backend.template;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * One of the 3 CV layouts a user can pick (slide: "3 CV templates").
 * Stored in PostgreSQL (table cv_templates, seeded by data.sql) instead of a
 * hard-coded Java list, so adding/renaming a template never needs a code
 * change or a redeploy - just a row in the database.
 */
@Entity
@Table(name = "cv_templates")
public class CvTemplate {

    /** Natural id matching the template values the frontend already uses: classic/modern/minimal. */
    @Id
    private String id;

    private String name;

    private String description;

    @Column(name = "sort_order")
    private int sortOrder;

    public CvTemplate() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
