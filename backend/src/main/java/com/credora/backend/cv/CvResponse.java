package com.credora.backend.cv;

import java.time.Instant;
import java.util.List;

/** What the frontend receives back - adds read-only info the request doesn't have. */
public class CvResponse {

    private String template;
    private String name;
    private String photo;
    private String jobTitle;
    private String email;
    private String phone;
    private String location;
    private String linkedin;
    private String portfolio;
    private String summary;
    private String education;
    private String experience;
    private String projects;
    private String skills;
    private String languages;
    private String certificates;
    private List<String> certificateImages;
    private int completionPercent;
    private Instant updatedAt;

    public static CvResponse empty() {
        CvResponse response = new CvResponse();
        response.template = "modern";
        response.name = "";
        response.jobTitle = "";
        response.email = "";
        response.phone = "";
        response.location = "";
        response.linkedin = "";
        response.portfolio = "";
        response.summary = "";
        response.education = "";
        response.experience = "";
        response.projects = "";
        response.skills = "";
        response.languages = "";
        response.certificates = "";
        response.certificateImages = List.of();
        response.completionPercent = 0;
        return response;
    }

    public static CvResponse from(Cv cv, int completionPercent) {
        CvResponse response = new CvResponse();
        response.template = cv.getTemplate();
        response.name = cv.getName();
        response.photo = cv.getPhoto();
        response.jobTitle = cv.getJobTitle();
        response.email = cv.getEmail();
        response.phone = cv.getPhone();
        response.location = cv.getLocation();
        response.linkedin = cv.getLinkedin();
        response.portfolio = cv.getPortfolio();
        response.summary = cv.getSummary();
        response.education = cv.getEducation();
        response.experience = cv.getExperience();
        response.projects = cv.getProjects();
        response.skills = cv.getSkills();
        response.languages = cv.getLanguages();
        response.certificates = cv.getCertificates();
        response.certificateImages = cv.getCertificateImages().stream().map(CertificateImage::getImageData).toList();
        response.completionPercent = completionPercent;
        response.updatedAt = cv.getUpdatedAt();
        return response;
    }

    public String getTemplate() {
        return template;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public String getSummary() {
        return summary;
    }

    public String getEducation() {
        return education;
    }

    public String getExperience() {
        return experience;
    }

    public String getProjects() {
        return projects;
    }

    public String getSkills() {
        return skills;
    }

    public String getLanguages() {
        return languages;
    }

    public String getCertificates() {
        return certificates;
    }

    public List<String> getCertificateImages() {
        return certificateImages;
    }

    public int getCompletionPercent() {
        return completionPercent;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
