package services.date;

import org.joda.time.DateTime;

public class SystemDateService extends DateService {
    @Override
    public DateTime currentDateTime() {
        return DateTime.now();
    }
}