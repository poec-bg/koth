package jobs;

import play.jobs.Every;
import play.jobs.Job;
import services.RessourceService;

@Every("10s")
public class RessourceJob extends Job {

    public void doJob() {
        RessourceService.harvest();
    }

}
