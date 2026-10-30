package com.credora.backend.cv;

import com.credora.backend.common.ApiException;
import com.credora.backend.request.RequestQueueService;
import com.credora.backend.user.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Ties together every CV-related data structure called for on the slide:
 *  - List + Traversal, to work out how complete a CV is (computeCompletion)
 *  - Stack + Push/Pop, for undo (CvUndoService)
 *  - Queue + Enqueue/Dequeue, so writes run in the order they arrive (RequestQueueService)
 */
@Service
public class CvService {

    private final CvRepository cvRepository;
    private final UserRepository userRepository;
    private final CvUndoService cvUndoService;
    private final RequestQueueService requestQueueService;

    public CvService(CvRepository cvRepository, UserRepository userRepository,
                      CvUndoService cvUndoService, RequestQueueService requestQueueService) {
        this.cvRepository = cvRepository;
        this.userRepository = userRepository;
        this.cvUndoService = cvUndoService;
        this.requestQueueService = requestQueueService;
    }

    public CvResponse getCv(Long userId) {
        return cvRepository.findByUserId(userId)
                .map(cv -> CvResponse.from(cv, computeCompletion(cv)))
                .orElseGet(CvResponse::empty);
    }

    /** Saves (creates or updates) the CV. The actual DB write happens inside the request queue. */
    public CvResponse saveCv(Long userId, CvRequest request) {
        Cv[] savedHolder = new Cv[1];
        requestQueueService.enqueue(() -> savedHolder[0] = applyAndPersist(userId, request));
        requestQueueService.processAll();

        Cv saved = savedHolder[0];
        return CvResponse.from(saved, computeCompletion(saved));
    }

    /** Pops the last snapshot off the undo stack and restores it. */
    public CvResponse undoLastChange(Long userId) {
        Cv cv = cvRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No CV to undo yet."));

        CvSnapshot snapshot = cvUndoService.pop(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Nothing to undo."));

        snapshot.applyTo(cv);
        replaceCertificateImages(cv, snapshot.getCertificateImages());
        cv.setUpdatedAt(Instant.now());

        Cv saved = cvRepository.save(cv);
        return CvResponse.from(saved, computeCompletion(saved));
    }

    private Cv applyAndPersist(Long userId, CvRequest request) {
        Optional<Cv> existing = cvRepository.findByUserId(userId);
        Cv cv;
        if (existing.isPresent()) {
            cv = existing.get();
            cvUndoService.push(userId, new CvSnapshot(cv)); // remember the old state before overwriting it
        } else {
            cv = new Cv();
            cv.setUser(userRepository.getReferenceById(userId));
        }

        cv.setTemplate(request.getTemplate());
        cv.setName(request.getName());
        cv.setPhoto(request.getPhoto());
        cv.setJobTitle(request.getJobTitle());
        cv.setEmail(request.getEmail());
        cv.setPhone(request.getPhone());
        cv.setLocation(request.getLocation());
        cv.setLinkedin(request.getLinkedin());
        cv.setPortfolio(request.getPortfolio());
        cv.setSummary(request.getSummary());
        cv.setEducation(request.getEducation());
        cv.setExperience(request.getExperience());
        cv.setProjects(request.getProjects());
        cv.setSkills(request.getSkills());
        cv.setLanguages(request.getLanguages());
        cv.setCertificates(request.getCertificates());
        replaceCertificateImages(cv, request.getCertificateImages());
        cv.setUpdatedAt(Instant.now());

        return cvRepository.save(cv);
    }

    private void replaceCertificateImages(Cv cv, List<String> imageData) {
        List<CertificateImage> images = cv.getCertificateImages();
        images.clear();
        for (String data : imageData) {
            CertificateImage image = new CertificateImage(data);
            image.setCv(cv);
            images.add(image);
        }
    }

    /**
     * "CV information -> Array/List + Traversal -> Store and access CV data":
     * every text field is collected into a List, then traversed once to count
     * how many are actually filled in.
     */
    private int computeCompletion(Cv cv) {
        List<String> fields = List.of(
                cv.getName(), cv.getJobTitle(), cv.getEmail(), cv.getPhone(), cv.getLocation(),
                cv.getSummary(), cv.getEducation(), cv.getExperience(), cv.getProjects(), cv.getSkills()
        );

        int filled = 0;
        for (String field : fields) {
            if (field != null && !field.isBlank()) {
                filled++;
            }
        }
        return Math.round((filled * 100f) / fields.size());
    }
}
