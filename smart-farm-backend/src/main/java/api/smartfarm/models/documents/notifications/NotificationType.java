package api.smartfarm.models.documents.notifications;

import lombok.Getter;

@Getter
public enum NotificationType {
    PARAMETER_OUT_OF_RANGE("ParameterOutOfRangeNotification"),
    SENSOR_FAIL_NOTIFICATION("SensorFailNotification"),
    WEATHER_ALERT_NOTIFICATION("WeatherAlertNotification"),
    FARM_DISCONNECTED_NOTIFICATION("FarmDisconnectedNotification");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }
}
