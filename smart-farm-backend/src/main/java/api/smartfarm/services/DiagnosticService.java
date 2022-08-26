package api.smartfarm.services;

import api.smartfarm.models.documents.Diagnostic;
import api.smartfarm.models.documents.DiagnosticType;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.entities.Plant;
import api.smartfarm.models.exceptions.FailedAICommandException;
import api.smartfarm.models.exceptions.FailedStorageException;
import api.smartfarm.models.exceptions.InvalidFileException;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.DiagnosticDAO;
import api.smartfarm.repositories.DiagnosticTypeDAO;
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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

@Service
public class DiagnosticService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiagnosticService.class);

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String azureBlobContName;

    @Value("#{systemProperties['java.class.path']}")
    private String classPath;
    @Value("${plant-health.python-script}")
    private String pythonScript;
    @Value("${plant-health.ai.model}")
    private String iaModel;
    private final FarmDAO farmDAO;
    private final DiagnosticDAO diagnosticDao;
    private final DiagnosticTypeDAO diagnosticTypeDao;
    private final BlobServiceClient blobServiceClient;

    @Autowired
    public DiagnosticService(
            FarmDAO farmDAO,
            DiagnosticDAO diagnosticDao,
            DiagnosticTypeDAO diagnosticTypeDao,
            BlobServiceClient blobServiceClient
    ) {
        this.farmDAO = farmDAO;
        this.diagnosticDao = diagnosticDao;
        this.blobServiceClient = blobServiceClient;
        this.diagnosticTypeDao = diagnosticTypeDao;
    }

    private boolean validateDiagnosticFile(MultipartFile file) {
        long maxDataSize = DataSize.parse(maxFileSize).toBytes();
        return file.getSize() <= maxDataSize;
    }

    public Diagnostic runDiagnosticFromFile(String farmId, String plantId, MultipartFile file) {
        if (!validateDiagnosticFile(file)) {
            throw new InvalidFileException("Uploaded file not valid.");
        }
        Farm plantFarm = getPlantFarm(farmId, plantId);
        Plant plant = plantFarm.getPlantById(plantId);
        Diagnostic diagnostic = getDiagnostic(file,plant.getId());
        diagnostic = diagnosticDao.save(diagnostic);
        if (plant.getDiagnostics() == null) {
            plant.setDiagnostics(new ArrayList<>());
        }
        plant.getDiagnostics().add(diagnostic.getId());
        farmDAO.save(plantFarm);
        LOGGER.info("File uploaded successfully.");
        return diagnostic;
    }

    private Diagnostic getDiagnostic(MultipartFile file, String plantId) {
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setDateTime(new Date());
        diagnostic.setPlantId(plantId);
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
        String result = callAI(url);
        if(result==null)
            return "INVALID";
        switch (result){
            case "arugula_downy_mildew":
            case "basil_downy_mildew":
            case "lettuce_fungus_downy_mildew":
            case "spinach_downy_mildew":
                return "DM";
            case "arugula_powdery_mildew":
            case "lettuce_fungus_powdery_mildew":
                return "PM";
            case "lettuce_fungus_septoria_blight":
                return "SB";
            case "basil_healthy":
            case "lettuce_healthy":
                return "HT";
            case "lettuce_bacterial":
                return "BT";
            case "spinach_stemphylium_leaf_spot":
                return "STS";
            case "spinach_white_rust":
                return "WR";
            default:
                return result;
        }
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

    private String callAI(String url) {
        try {
            String path = classPath.split(";")[0];

            String[] cmd = {
                    "python ",
                    path.concat(pythonScript)+" ",
                    path.concat(iaModel)+" ",
                    url
            };

            String finalCommand = String.format("%s%s%s%s", cmd[0], cmd[1], cmd[2], cmd[3]);
            LOGGER.info("Command to execute: {}", finalCommand);

            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process p = processBuilder.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String res = null;
            String resAux;
            while((resAux=in.readLine())!=null){
                res=resAux;
                LOGGER.info("AI log: {}",res);
            }
            LOGGER.info("Final value is: {}",res);
            return res;
        } catch (Exception e){
            e.printStackTrace();
            throw new FailedAICommandException("Failed to call AI for diagnosis.");
        }
    }
}