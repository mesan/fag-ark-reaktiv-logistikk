package no.mesan.fagark.reaktiv.logistikk.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionLogger implements Catchable {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void handle(final Exception e) {
        logger.log(Level.WARNING, e.getMessage(), e);
    }

}
