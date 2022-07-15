package api.smartfarm.models.dtos.events;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventListsDTO {
    private List<IrrigationEventDTO> irrigationEvents = new ArrayList<>();
    private List<AntiFrostEventDTO> antiFrostEvents = new ArrayList<>();
}
