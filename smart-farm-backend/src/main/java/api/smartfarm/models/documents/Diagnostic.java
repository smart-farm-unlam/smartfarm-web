package api.smartfarm.models.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "diagnostics")
public class Diagnostic {

    @Id private String id;

    private String plantId;
    
    private Date dateTime;

    private String imgUrl;

    private String farmId;

    @DocumentReference
    private DiagnosticType diagnosticType;
}
