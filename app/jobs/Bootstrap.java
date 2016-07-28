package jobs;

import models.Player;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        if(Player.count() == 0) {
            Fixtures.loadModels("initial-data.yml");
        }
    }

}
