package api.smartfarm.models.dtos;

import api.smartfarm.models.documents.Measure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class AverageMeasureHistoricDTO {
    private Date dateTime;
    private String sensorCode;
    private Double value;

    public AverageMeasureHistoricDTO(Measure measure) {
        this.dateTime = measure.getDateTime();
        this.sensorCode = measure.getSensorCode();
        this.value = measure.getValue();
    }
}
