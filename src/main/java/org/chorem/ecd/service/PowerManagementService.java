package org.chorem.ecd.service;

import org.chorem.ecd.dao.SettingsDao;
import org.chorem.ecd.dao.TransactionRequired;
import org.chorem.ecd.exception.InvalidArgumentException;
import org.chorem.ecd.model.settings.TvTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class PowerManagementService {

    private static final Logger logger = LoggerFactory.getLogger(PowerManagementService.class);

    @Inject
    protected SettingsDao settingsDao;

    public TvTimes getTvTimes() {
        return settingsDao.getTvTimes();
    }

    @TransactionRequired
    public void setTvTimes(TvTimes tvTimes) {
        validateTvTimes(tvTimes);

        settingsDao.setTvTimes(tvTimes);
    }

    private void validateTvTimes(TvTimes tvTimes) throws InvalidArgumentException {
        try {
            LocalTime.parse(tvTimes.getWakeupTime());
            LocalTime.parse(tvTimes.getSleepTime());
        } catch(NullPointerException | DateTimeParseException e) {
            logger.error(e.getMessage(), e);
            throw new InvalidArgumentException("Les heures sont invalides");
        }
    }

}
