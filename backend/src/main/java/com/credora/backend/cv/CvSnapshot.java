package com.credora.backend.cv;

import java.util.List;

/**
 * An immutable copy of every CV field at one point in time - what gets
 * pushed onto the undo STACK before a save overwrites the CV.
 */
public class CvSnapshot {

    private final String template;
    private final String name;
    private final String photo;
    private final String jobTitle;
    private final String email;
    private final String phone;
    private final String location;
    private final String linkedin;
    private final String portfolio;
    private final String summary;
    private final String education;
    private final String experience;
    private final String projects;
    private final String skills;
    private final String languages;
    private final String certificates;
    private final List<String> certificateImages;

    public CvSnapshot(Cv cv) {
        this.template = cv.getTemplate();
        this.name = cv.getName();
        this.photo = cv.getPhoto();
        this.jobTitle = cv.getJobTitle();
        this.email = cv.getEmail();
        this.phone = cv.getPhone();
        this.location = cv.getLocation();
        this.linkedin = cv.getLinkedin();
        this.portfolio = cv.getPortfolio();
        this.summary = cv.getSummary();
        this.education = cv.getEducation();
        this.experience = cv.getExperience();
        this.projects = cv.getProjects();
        this.skills = cv.getSkills();
        this.languages = cv.getLanguages();
        this.certificates = cv.getCertificates();
        this.certificateImages = cv.getCertificateImages().stream().map(CertificateImage::getImageData).toList();
    }

    /** Copies this snapshot's values back onto a live CV entity (used by undo). */
    public void applyTo(Cv cv) {
        cv.setTemplate(template);
        cv.setName(name);
        cv.setPhoto(photo);
        cv.setJobTitle(jobTitle);
        cv.setEmail(email);
        cv.setPhone(phone);
        cv.setLocation(location);
        cv.setLinkedin(linkedin);
        cv.setPortfolio(portfolio);
        cv.setSummary(summary);
        cv.setEducation(education);
        cv.setExperience(experience);
        cv.setProjects(projects);
        cv.setSkills(skills);
        cv.setLanguages(languages);
        cv.setCertificates(certificates);
    }

    public List<String> getCertificateImages() {
        return certificateImages;
    }
}
