package api.smartfarm.services;

import api.smartfarm.models.documents.EventType;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.documents.User;
import api.smartfarm.models.documents.events.Event;
import api.smartfarm.models.documents.events.IrrigationEvent;
import api.smartfarm.models.dtos.FarmDTO;
import api.smartfarm.models.dtos.events.EventDTO;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.EventDAO;
import api.smartfarm.repositories.EventTypeDAO;
import api.smartfarm.repositories.FarmDAO;
import api.smartfarm.repositories.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FarmService {

    private final FarmDAO farmDAO;
    private final UserDAO userDAO;
    private final EventTypeDAO eventTypeDAO;

    private final EventDAO eventDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(FarmService.class);

    @Autowired
    public FarmService(FarmDAO farmDAO, UserDAO userDAO, EventTypeDAO eventTypeDAO, EventDAO eventDAO) {
        this.farmDAO = farmDAO;
        this.userDAO = userDAO;
        this.eventTypeDAO = eventTypeDAO;
        this.eventDAO = eventDAO;
    }

    public FarmDTO create(FarmDTO farmDTO) {
        Optional<User> user = userDAO.findById(farmDTO.getUserId());
        if (!user.isPresent()) {
            String errorMsg = "User with id " + farmDTO.getUserId() + "not exists on database";
            throw new NotFoundException(errorMsg);
        }

        Farm farm = new Farm(farmDTO);
        farmDAO.save(farm);
        LOGGER.info("Saved farm {} successfully", farm);

        farmDTO.setId(farm.getId());
        farmDTO.setSensors(farm.getSensors());
        farmDTO.setSectors(farm.getSectors());
        farmDTO.setEvents(farm.getEvents());
        return farmDTO;
    }

    //calcular si la ultima medición es de hace menos de 15 minutos y si es mayor
    //determinar que el microcontrolador esta desconectado del wifi
    public FarmDTO getById(String id) {
        Farm farm = getFarmById(id);
        return new FarmDTO(farm);
    }

    public Farm getFarmById(String id) {
        return farmDAO.findById(id).orElseThrow(() -> {
            String errorMsg = "Farm with id " + id + " not exists on database";
            return new NotFoundException(errorMsg);
        });
    }

    public void registerEvent(String farmId, EventDTO eventDTO) {
        Event event = getEventFromEventDTO(eventDTO);
        handleEvent(event,farmId);
    }

    private Event getEventFromEventDTO(EventDTO eventDTO) {
        EventType eventType = getEventTypeById(eventDTO.getEventType());
        switch (eventType.getId()){
            case "IrrigationEvent":
                return new IrrigationEvent(eventDTO,eventType);
        }
        return null;
    }

    private void handleEvent(Event event, String farmId) {
        Farm farm = getFarmById(farmId);
        if(event instanceof IrrigationEvent){
            IrrigationEvent irrigationEvent = (IrrigationEvent) event;
            irrigationEvent.setFarmId(farmId);
            if(irrigationEvent.getStartDate()!=null){
                eventDAO.save(event);
                if(farm.getEvents()==null){
                    farm.setEvents(new ArrayList<>());
                }
                farm.getEvents().add(event.getId());
            } else if(irrigationEvent.getEndDate()!=null) {
                event = getLastStartedEvent(farmId,event.getEventType().getId());
                ((IrrigationEvent) event).setEndDate(irrigationEvent.getEndDate());
                eventDAO.save(event);
            }
            farmDAO.save(farm);
        }
    }

    private Event getLastStartedEvent(String farmId, String eventType) {
        return eventDAO.findLastEventByFarmAndEventType(farmId,eventType)
                .orElseThrow(()-> new NotFoundException("No started event in data base for farm id : ["+farmId+"]"+
                        " and event type: ["+eventType+"]"));
    }

    private EventType getEventTypeById(String id) {
       return eventTypeDAO.findById(id).orElseThrow(()-> new NotFoundException("Event type: [ "+id+" ] not found in database"));
    }

    public List<Event> getFarmEvents(String farmId) {
        return eventDAO.findByFarmId(farmId);
    }
}
