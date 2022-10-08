package api.smartfarm.models.dtos.weather;

public enum AlertTemperature {
    TOO_COLD,
    TOO_HOT;

    private static final Double TOO_COLD_LIMIT = 5.0;
    private static final Double TOO_HOT_LIMIT = 30.0;

    public static String buildTemperatureAlert(Double currentTemperature) {
        String result = null;

        if (currentTemperature <= TOO_COLD_LIMIT) {
            result = AlertTemperature.TOO_COLD.name();
        } else if (currentTemperature >= TOO_HOT_LIMIT) {
            result = AlertTemperature.TOO_HOT.name();
        }

        return result;
    }

    public static String buildTemperatureAlert(Double minTemperature, Double maxTemperature) {
        String result = null;

        if (minTemperature <= TOO_COLD_LIMIT) {
            result = AlertTemperature.TOO_COLD.name();
        } else if (maxTemperature >= TOO_HOT_LIMIT) {
            result = AlertTemperature.TOO_HOT.name();
        }

        return result;
    }

}
