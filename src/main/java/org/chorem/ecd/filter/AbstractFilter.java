package org.chorem.ecd.filter;

import org.chorem.ecd.service.AuthService;
import spark.Filter;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
abstract public class AbstractFilter implements Filter {

    @Inject
    protected AuthService authService;

    @SuppressWarnings("unused")
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        init();
    }

    abstract protected void init();

}
