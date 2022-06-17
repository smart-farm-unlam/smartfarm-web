package api.smartfarm.models.entities;

import api.smartfarm.models.documents.DiagnosticType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Diagnostic {
    private DiagnosticType id; //Ver como persiste esto
}
