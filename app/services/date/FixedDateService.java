package services.date;

import org.joda.time.DateTime;

public abstract class FixedDateService extends DateService {

    public abstract DateTime currentDateTime() ;

}