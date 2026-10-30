package com.credora.backend.cv;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import com.credora.backend.user.User;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * One CV per user. Field names mirror the "emptyCv" object in the frontend's
 * src/CvContext.jsx one-to-one, so the JSON the frontend already sends/expects
 * needs almost no translation.
 *
 * Photos and certificate images are stored as base64 text directly in
 * PostgreSQL (TEXT columns) instead of a separate file store - simpler to run
 * and deploy for a student project, at the cost of a bigger database. See the
 * documentation for this trade-off.
 */
@Entity
@Table(name = "cvs")
public class Cv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String template = "modern";

    private String name = "";

    @Column(columnDefinition = "TEXT")
    private String photo;

    @Column(name = "job_title")
    private String jobTitle = "";

    private String email = "";
    private String phone = "";
    private String location = "";
    private String linkedin = "";
    private String portfolio = "";

    @Column(columnDefinition = "TEXT")
    private String summary = "";

    @Column(columnDefinition = "TEXT")
    private String education = "";

    @Column(columnDefinition = "TEXT")
    private String experience = "";

    @Column(columnDefinition = "TEXT")
    private String projects = "";

    @Column(columnDefinition = "TEXT")
    private String skills = "";

    @Column(columnDefinition = "TEXT")
    private String languages = "";

    @Column(columnDefinition = "TEXT")
    private String certificates = "";

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "position")
    private List<CertificateImage> certificateImages = new ArrayList<>();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    public Cv() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getCertificates() {
        return certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    public List<CertificateImage> getCertificateImages() {
        return certificateImages;
    }

    public void setCertificateImages(List<CertificateImage> certificateImages) {
        this.certificateImages = certificateImages;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
