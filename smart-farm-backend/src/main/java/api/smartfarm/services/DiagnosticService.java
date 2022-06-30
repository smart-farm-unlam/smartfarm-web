package api.smartfarm.services;

import api.smartfarm.models.documents.Diagnostic;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.entities.Plant;
import api.smartfarm.models.exceptions.FailedStorageException;
import api.smartfarm.models.exceptions.InvalidFileException;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.DiagnosticDAO;
import api.smartfarm.repositories.FarmDAO;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.specialized.BlockBlobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Date;

@Service
public class DiagnosticService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiagnosticService.class);

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String azureBlobContName;

    private final FarmDAO farmDAO;
    private final DiagnosticDAO diagnosticDao;
    private final BlobServiceClient blobServiceClient;

    @Autowired
    public DiagnosticService(
        FarmDAO farmDAO,
        DiagnosticDAO diagnosticDao,
        BlobServiceClient blobServiceClient
    ) {
        this.farmDAO = farmDAO;
        this.diagnosticDao = diagnosticDao;
        this.blobServiceClient = blobServiceClient;
    }

    private boolean validateDiagnosticFile(MultipartFile file) {
        long maxDataSize = DataSize.parse(maxFileSize).toBytes();
        return file.getSize() <= maxDataSize;
    }

    public void saveDiagnosticFile(String farmId, String plantId, MultipartFile file) {
        if (!validateDiagnosticFile(file)) {
            throw new InvalidFileException("Uploaded file not valid.");
        }
        Farm plantFarm = getPlantFarm(farmId, plantId);
        Plant plant = plantFarm.getPlantById(plantId);

        String url = saveFileOnStorage(file);
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setImgUrl(url);
        diagnostic.setDateTime(new Date());
        diagnostic.setPlantId(plant.getId());
        diagnostic = diagnosticDao.save(diagnostic);
        if (plant.getDiagnostics() == null) {
            plant.setDiagnostics(new ArrayList<>());
        }
        plant.getDiagnostics().add(diagnostic.getId());
        farmDAO.save(plantFarm);
        LOGGER.info("File uploaded successfully.");
    }

    private Farm getPlantFarm(String farmId, String plantId) {
        return farmDAO.findByIdAndSectorsCropPlantsId(farmId, plantId).orElseThrow(() -> {
            String errorMsg = "Farm with id " + farmId + " not exists on database " +
                "or plant with id " + plantId + " not exists on farm";
            return new NotFoundException(errorMsg);
        });
    }

    private String saveFileOnStorage(MultipartFile file) {
        String fileUrl;
        String filename = file.getOriginalFilename(); //ver con lucas nombre del archivo
        BlockBlobClient blockBlobClient = blobServiceClient.getBlobContainerClient(azureBlobContName)
            .getBlobClient(filename).getBlockBlobClient();
        try {
            blockBlobClient.upload(new BufferedInputStream(file.getInputStream()), file.getSize(), true);
            fileUrl = blockBlobClient.getBlobUrl();
        } catch (Exception e) {
            throw new FailedStorageException("Failed to upload file to azure storage.");
        }
        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new FailedStorageException("Failed to obtain file URL.");
        }
        return fileUrl;
    }

}
