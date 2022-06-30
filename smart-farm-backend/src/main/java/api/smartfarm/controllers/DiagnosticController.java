package api.smartfarm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import api.smartfarm.services.DiagnosticService;

@RestController
@RequestMapping("/diagnostic")
@CrossOrigin
public class DiagnosticController {

    @Autowired
    DiagnosticService diagnosticService;

    @PostMapping("/analize")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void analizePhoto(
            @RequestParam("farmId") String farmId,
            @RequestParam("plantId") String plantId,
            @RequestParam("file") MultipartFile imageFile
    ) {
        diagnosticService.saveDiagnosticFile(farmId, plantId, imageFile);
    }
}
