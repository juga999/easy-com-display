package org.chorem.ecd.dao;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@TransactionRequired
@Interceptor
public class TransactionProvider {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProvider.class);

    @Inject
    protected EcdDataSource dataSource;

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        if (dataSource.getTransactionConfiguration().isPresent()) {
            return context.proceed();
        }

        DSLContext create = dataSource.getSqlContext();
        final Object[] result = new Object[1];
        create.transaction(configuration -> {
            dataSource.setTransactionConfiguration(configuration);
            try {
                result[0] = context.proceed();
            } catch(Exception e) {
                logger.error(e.getMessage(), e);
                throw e;
            } finally {
                dataSource.setTransactionConfiguration(null);
            }
        });

        return result[0];
    }

}
