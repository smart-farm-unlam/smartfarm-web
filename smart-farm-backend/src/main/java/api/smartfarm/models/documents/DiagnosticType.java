package api.smartfarm.models.documents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
//@Document(collection = "diagnostic_types")
public class DiagnosticType {

    @Id
    private String id;

    private String description;
    private String treatment;

}
