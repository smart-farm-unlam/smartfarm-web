package api.smartfarm.models.documents.notifications;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "notifications")
public abstract class SmartFarmNotification {

    private String id;
    private Date date;
    private String farmId;
    private String userId;
    private List<String> devices;
    private NotificationStatus status;
    private List<String> messageIds;

    protected SmartFarmNotification(
        Date date,
        String farmId,
        String userId,
        List<String> devices
    ) {
        this.date = date;
        this.farmId = farmId;
        this.userId = userId;
        this.devices = devices;
        this.messageIds = new ArrayList<>();
    }
}
