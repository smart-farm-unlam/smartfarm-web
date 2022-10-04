package api.smartfarm.services;

import api.smartfarm.clients.diagnostic.DiagnosticClient;
import api.smartfarm.clients.diagnostic.model.DiagnosticResponse;
import api.smartfarm.models.documents.Diagnostic;
import api.smartfarm.models.documents.DiagnosticType;
import api.smartfarm.models.exceptions.FailedStorageException;
import api.smartfarm.models.exceptions.InvalidFileException;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.DiagnosticDAO;
import api.smartfarm.repositories.DiagnosticTypeDAO;
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
import java.util.Date;
import java.util.List;

@Service
public class DiagnosticService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiagnosticService.class);

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String azureBlobContName;

    private final DiagnosticDAO diagnosticDao;
    private final DiagnosticTypeDAO diagnosticTypeDao;
    private final BlobServiceClient blobServiceClient;
    private final DiagnosticClient diagnosticClient;

    @Autowired
    public DiagnosticService(
        DiagnosticDAO diagnosticDao,
        DiagnosticTypeDAO diagnosticTypeDao,
        BlobServiceClient blobServiceClient,
        DiagnosticClient diagnosticClient
    ) {
        this.diagnosticDao = diagnosticDao;
        this.blobServiceClient = blobServiceClient;
        this.diagnosticTypeDao = diagnosticTypeDao;
        this.diagnosticClient = diagnosticClient;
    }

    private boolean validateDiagnosticFile(MultipartFile file) {
        long maxDataSize = DataSize.parse(maxFileSize).toBytes();
        return file.getSize() <= maxDataSize;
    }

    public Diagnostic runDiagnosticFromFile(String farmId, String plantId, MultipartFile file) {
        if (!validateDiagnosticFile(file)) {
            throw new InvalidFileException("Uploaded file not valid.");
        }
        Diagnostic diagnostic = getDiagnostic(file, plantId, farmId);
        diagnostic = diagnosticDao.save(diagnostic);
        LOGGER.info("File uploaded successfully.");
        return diagnostic;
    }

    private Diagnostic getDiagnostic(MultipartFile file, String plantId, String farmId) {
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setDateTime(new Date());
        diagnostic.setPlantId(plantId);
        diagnostic.setFarmId(farmId);
        String url = saveFileOnStorage(file);
        diagnostic.setImgUrl(url);
        DiagnosticType diagnosticType = getDiagnosticType(url);
        diagnostic.setDiagnosticType(diagnosticType);
        return diagnostic;
    }

    private DiagnosticType getDiagnosticType(String url) {
        String diagnosticTypeId = getDiagnosticTypeId(url);
        return diagnosticTypeDao.findById(diagnosticTypeId).orElseThrow(() -> {
            String errorMsg = "No diagnostic type " + diagnosticTypeId + " was found on database";
            return new NotFoundException(errorMsg);
        });
    }

    private String getDiagnosticTypeId(String url) {
        String result = callAIDiagnosticServer(url);
        return result != null ? result : "INVALID";
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

    private String callAIDiagnosticServer(String url) {
        DiagnosticResponse response = diagnosticClient.diagnosticPlantHealth(url);
        return response.getDiagnostic();
    }

    public List<Diagnostic> getDiagnosticHistoric(String farmId, String plantId) {
        return diagnosticDao.findByFarmIdAndPlantIdOrderByDateTimeDesc(farmId, plantId);
    }
}