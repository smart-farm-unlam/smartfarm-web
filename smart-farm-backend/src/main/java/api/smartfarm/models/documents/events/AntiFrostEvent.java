package api.smartfarm.models.documents.events;

import api.smartfarm.models.documents.EventType;
import api.smartfarm.models.dtos.events.AntiFrostEventDTO;
import api.smartfarm.models.dtos.events.EventDTO;
import api.smartfarm.models.exceptions.EventDTOParseException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AntiFrostEvent extends Event {

    private String sectorId;

    private Date startDate = null;

    private Date endDate = null;

    public AntiFrostEvent(EventDTO eventDTO, EventType eventType) {
        super(eventType);
        AntiFrostEventDTO castedEventDTO = (AntiFrostEventDTO) eventDTO;

        this.sectorId = castedEventDTO.getSectorId();
        setDatesBasedOnStatus(castedEventDTO);
    }

    private void setDatesBasedOnStatus(AntiFrostEventDTO eventDTO) {
        String status = eventDTO.getStatus();
        //Date date = eventDTO.getDate(); //TODO cambiar cuando podamos enviar le fecha desde el micro
        Date date = new Date();
        switch (status) {
            case "ON":
                this.startDate = date;
                break;
            case "OFF":
                this.endDate = date;
                break;
            default:
                throw new EventDTOParseException("Failed to parse [ " + this.getClass().getSimpleName() + " ] from DTO");
        }
    }
}
