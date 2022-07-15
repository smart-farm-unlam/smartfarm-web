package api.smartfarm.models.dtos.events;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IrrigationEventDTO extends EventDTO {
    private String sectorId;

    @Override
    public String toString() {
        return "IrrigationEventDTO(" +
            "sectorId=" + this.getSectorId() +
            ", eventType=" + this.getEventType() +
            ", date=" + this.getDate() +
            ", status=" + this.getStatus() +
            ")";
    }
}
