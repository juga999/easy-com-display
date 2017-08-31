package org.chorem.ecd.model.settings;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public class Settings {
    private Location location;

    private TvTimes tvTimes;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TvTimes getTvTimes() {
        return tvTimes;
    }

    public void setTvTimes(TvTimes tvTimes) {
        this.tvTimes = tvTimes;
    }
}
