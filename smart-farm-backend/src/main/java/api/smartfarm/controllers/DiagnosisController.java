package api.smartfarm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import api.smartfarm.services.DiagnosisService;

@RestController
@RequestMapping("/diagnosis")
@CrossOrigin
public class DiagnosisController {

    @Autowired DiagnosisService diagnosisService;

    @PostMapping("/analize")
    @ResponseBody
    public ResponseEntity<?> analizePhoto(
        @RequestParam("farmid")String farmId,
        @RequestParam("plantid")String plantId,
        @RequestParam("file") MultipartFile imageFile){
        if(diagnosisService.validateDiagnosisFile(imageFile)){
            if(diagnosisService.saveDiagnosisFile(farmId, plantId, imageFile)){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Archivo subido, pesa: " + imageFile.getSize());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo guardar el archivo");    
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("El archivo subido no soportado");
    }
}
