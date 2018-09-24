/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo;

import cezeszamlazo.database.Query;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Loop {

    long delay = 60 * 1000; // delay in ms : 10 * 60 * 1000 ms = 10 min.
    LoopTask task = new LoopTask();
    Timer timer = new Timer("TaskName");

    public void start() {
        timer.cancel();
        timer = new Timer("TaskName");
        Date executionDate = new Date(); // no params = now
        timer.scheduleAtFixedRate(task, executionDate, delay);
    }

    private class LoopTask extends TimerTask {

        public void run() {
            try {
                System.out.println("Adatbázis kapcsolat frissítése.");
                Query query = new Query.QueryBuilder()
                        .select("id")
                        .from("cr_versions")
                        .where("1")
                        .order("id DESC LIMIT 1")
                        .build();
                App.db.select(query.getQuery());
            } catch (Exception ex) {
            }
        }
    }
}
