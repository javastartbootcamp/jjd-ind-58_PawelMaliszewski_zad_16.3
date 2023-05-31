package pl.javastart.task;

enum TimeZone {
    TIME_FROM_COMPUTER("Czas lokalny: ", "Europe/Paris"),
    UTC_ZONE("UTC: ", "Etc/UTC"),
    LONDON_TIME("Londyn: ", "Europe/London"),
    LOS_ANGELES_TIME("Los Angeles: ", "America/Los_Angeles"),
    SYDNEY_TIME("Sydney: ", "Australia/Sydney");

    private final String name;

    private final String zoneId;

    TimeZone(String name, String zoneId) {
        this.name = name;
        this.zoneId = zoneId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public String getName() {
        return name;
    }
}
