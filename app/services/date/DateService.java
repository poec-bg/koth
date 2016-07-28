package services.date;

import org.joda.time.DateTime;

public abstract class DateService {

    private static DateService instance = new SystemDateService();

    protected DateService() {
    }

    public static DateService get() {
        return instance;
    }

    public static void configureWith(DateService dateService) {
        instance = dateService;
    }

    public abstract DateTime currentDateTime();
}
