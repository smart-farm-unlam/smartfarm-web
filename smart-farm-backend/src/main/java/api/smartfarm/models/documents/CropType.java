package api.smartfarm.models.documents;

import api.smartfarm.models.entities.AverageMeasure;
import api.smartfarm.models.entities.CropParameter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "crop_types")
public class CropType {
    @Id
    private String id;
    private String name;
    private String description;
    private Double m2;
    private List<CropParameter> parameters;

    private OptimalEnvironment optimalEnvironment;
    private String plantation;
    private String properties;
    private String faq;
    private String harvest;

    public AverageMeasure.AverageMeasureLevel checkAverageMeasure(AverageMeasure measure) {
        CropParameter cropParameter = getParameterForMeasure(measure);

        if (cropParameter != null) {
            if (measure.getAverage() < cropParameter.getMin()) {
                return AverageMeasure.AverageMeasureLevel.BELOW_MIN;
            } else if (measure.getAverage() > cropParameter.getMax()) {
                return AverageMeasure.AverageMeasureLevel.OVER_MAX;
            }
        }
        return AverageMeasure.AverageMeasureLevel.NORMAL;
    }

    private CropParameter getParameterForMeasure(AverageMeasure measure) {
        return parameters.stream()
                .filter(parameter -> measure.getSensorTypeId().name().equals(parameter.getRelatedSensor()))
                .findFirst()
                .orElse(null);
    }
}
