package api.smartfarm.services;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.specialized.BlockBlobClient;

import api.smartfarm.models.documents.Diagnostic;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.entities.Plant;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.DiagnosticDAO;
import api.smartfarm.repositories.FarmDAO;

@Service
public class DiagnosisService {

    @Value("${spring.servlet.multipart.max-file-size}") private String maxFileSize;

    @Value("${spring.cloud.azure.storage.blob.container-name}") private String azureBlobContName;

    @Autowired private FarmDAO farmDAO;

    @Autowired private DiagnosticDAO diagnosisDao;

    @Autowired private BlobServiceClient blobServiceClient;

    public boolean validateDiagnosisFile(MultipartFile file){
        long maxDataSize = DataSize.parse(maxFileSize).toBytes();
        if(file.getSize() <= maxDataSize){
            //XXX ASK should validate file type?
            return true;
        }
        return false;
    }

    public boolean saveDiagnosisFile(String farmId, String plantId, MultipartFile file){
        Farm plantFarm = getPlantFarm(farmId,plantId);
        if(plantFarm!=null){
            Plant plant = plantFarm.getPlantById(plantId);
            if(plant!=null){
                String url = saveFileOnStorage(file);
                if(url!=null && !url.isEmpty()){
                    Diagnostic diagnosis  = new Diagnostic();
                    diagnosis.setImgUrl(url);
                    diagnosis.setDateTime(new Date());
                    diagnosis.setPlantId(plant.getId());
                    diagnosis=diagnosisDao.save(diagnosis);
                    if(plant.getDiagnostics()==null){
                        plant.setDiagnostics(new ArrayList<String>());
                    }
                    plant.getDiagnostics().add(diagnosis.getId());
                    farmDAO.save(plantFarm);
                    return true;
                }
            }    
        }
        return false;
    }

    private Farm getPlantFarm(String farmId, String plantId) {
        Farm farm = farmDAO.findByIdAndSectorsCropPlantsId(farmId,plantId).orElseThrow(() -> {
            String errorMsg = "Farm with id " + farmId +  " and plant with id "+ plantId +" not exists on database";
            return new NotFoundException(errorMsg);
        });

        return farm;
    }

    private String saveFileOnStorage(MultipartFile file) {
        String fileUrl=null;
        String filename = file.getOriginalFilename(); 
        BlockBlobClient blockBlobClient = blobServiceClient.getBlobContainerClient(azureBlobContName).getBlobClient(filename).getBlockBlobClient();
        try {
            blockBlobClient.upload(new BufferedInputStream(file.getInputStream()), file.getSize(), true);
            fileUrl = blockBlobClient.getBlobUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }


    
}
