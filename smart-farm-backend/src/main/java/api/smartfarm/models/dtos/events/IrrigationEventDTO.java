package api.smartfarm.models.dtos.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class IrrigationEventDTO extends EventDTO {

    private String status;

    private String sectorId;

    private Date date;

    public IrrigationEventDTO(){
        super("IrrigationEvent");
    }
}
