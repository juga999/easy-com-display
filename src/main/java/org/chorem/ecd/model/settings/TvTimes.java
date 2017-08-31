package org.chorem.ecd.model.settings;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public class TvTimes {

    private String wakeupTime;

    private String sleepTime;

    public TvTimes() {

    }

    public TvTimes(String wakeupTime, String sleepTime) {
        this.wakeupTime = wakeupTime;
        this.sleepTime = sleepTime;
    }

    public String getWakeupTime() {
        return wakeupTime;
    }

    public void setWakeupTime(String wakeupTime) {
        this.wakeupTime = wakeupTime;
    }

    public String getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(String sleepTime) {
        this.sleepTime = sleepTime;
    }

}
