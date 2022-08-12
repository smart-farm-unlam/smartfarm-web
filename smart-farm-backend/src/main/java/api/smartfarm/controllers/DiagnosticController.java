package api.smartfarm.controllers;

import api.smartfarm.models.documents.Diagnostic;
import api.smartfarm.services.DiagnosticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/diagnostic")
@CrossOrigin
public class DiagnosticController {

    private final DiagnosticService diagnosticService;

    @Autowired
    public DiagnosticController(DiagnosticService diagnosticService) {
        this.diagnosticService = diagnosticService;
    }

    @PostMapping("/analyze")
    @ResponseStatus(HttpStatus.OK)
    public Diagnostic analyzePhoto(
        @RequestParam("farmId") String farmId,
        @RequestParam("plantId") String plantId,
        @RequestParam("file") MultipartFile imageFile
    ) {
        return diagnosticService.runDiagnosticFromFile(farmId, plantId, imageFile);
    }
}
