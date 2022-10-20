package api.smartfarm.models.documents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "diagnostic_types")
public class DiagnosticType {
    @Id
    private DiagnosticTypeId id;
    private String description;
    private String treatment;

    @Getter
    public enum DiagnosticTypeId {
        DM("Downy Mildew"),
        PM("Powdery Mildew"),
        HT("Healthy"),
        BT("Bacterial"),
        SB("Septoria Blight"),
        SLS("Stemphylium Leaf Spot"),
        WR("White Rust"),
        UW("Underwatered");
        private final String description;

        DiagnosticTypeId(String description) {
            this.description = description;
        }
    }


}
