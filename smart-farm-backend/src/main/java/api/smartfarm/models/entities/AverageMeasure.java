package api.smartfarm.models.entities;

import api.smartfarm.models.documents.SensorType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AverageMeasure {

    private Double average;

    private SensorType.SensorTypeId sensorTypeId;

    private List<String> isOverMaxFor;

    private List<String> isBelowMinFor;

    private static final String OVER_MAX_MSG = "%s: está por encima del límite para %s.";

    private static final String BELOW_MIN_MSG = "%s: está por debajo del límite para %s.";

    public AverageMeasure(Double average, SensorType.SensorTypeId sensorTypeId) {
        this.average = average;
        this.sensorTypeId = sensorTypeId;
    }

    public void addBelowMinFor(String cropName) {
        if(isBelowMinFor==null)
            isBelowMinFor = new ArrayList<>();
        isBelowMinFor.add(cropName);
    }

    public void addOverMaxFor(String cropName) {
        if(isOverMaxFor==null)
            isOverMaxFor = new ArrayList<>();
        isOverMaxFor.add(cropName);
    }

    public String getNotificationMsg() {
        if(isOverMaxFor!=null && !isOverMaxFor.isEmpty()) {
            return formatNotificationMsg(OVER_MAX_MSG,isOverMaxFor);
        } else if (isBelowMinFor!=null && !isBelowMinFor.isEmpty()) {
            return formatNotificationMsg(BELOW_MIN_MSG,isBelowMinFor);
        }
        return null;
    }

    private String formatNotificationMsg(String baseFormat, List<String> cropNamesList) {
        return String.format(
                baseFormat,
                sensorTypeId.getDescription(),
                String.join(", ",cropNamesList)
        );
    }

    public enum AverageMeasureLevel{
        OVER_MAX,
        BELOW_MIN,
        NORMAL
    }
}