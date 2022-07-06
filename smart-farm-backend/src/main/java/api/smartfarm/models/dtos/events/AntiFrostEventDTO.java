package api.smartfarm.models.dtos.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class AntiFrostEventDTO extends EventDTO {

    private String status;

    private String sectorId;

    private Date date;

    public AntiFrostEventDTO(){
        super("AntiFrostEvent");
    }
}
