package com.credora.backend.cv;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Backs the "User enters CV information" / "User previews the CV" /
 * "System saves the information" steps of the slide's workflow diagram.
 * Every endpoint here requires a valid token - see AuthFilter.
 */
@RestController
@RequestMapping("/api/cv")
public class CvController {

    private final CvService cvService;

    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    @GetMapping
    public CvResponse getMyCv(@RequestAttribute("userId") Long userId) {
        return cvService.getCv(userId);
    }

    @PutMapping
    public CvResponse saveMyCv(@RequestAttribute("userId") Long userId, @Valid @RequestBody CvRequest request) {
        return cvService.saveCv(userId, request);
    }

    @PostMapping("/undo")
    public CvResponse undo(@RequestAttribute("userId") Long userId) {
        return cvService.undoLastChange(userId);
    }
}
