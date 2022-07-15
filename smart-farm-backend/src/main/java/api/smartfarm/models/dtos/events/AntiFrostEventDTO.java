package api.smartfarm.models.dtos.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AntiFrostEventDTO extends EventDTO {

    @Override
    public String toString() {
        return "AntiFrostEventDTO(" +
            "eventType=" + this.getEventType() +
            ", date=" + this.getDate() +
            ", status=" + this.getStatus() +
            ")";
    }
}
