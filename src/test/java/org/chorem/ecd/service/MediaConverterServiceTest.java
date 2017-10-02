package org.chorem.ecd.service;

import com.google.common.base.Strings;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@Ignore
public class MediaConverterServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(MediaConverterServiceTest.class);

    private static WeldContainer container;

    @BeforeClass
    public static void beforeClass() {
        container = (WeldContainer)Weld.newInstance().initialize();
        MediaConverterService service = container.select(MediaConverterService.class).get();

        String unoconvVersion = service.getUnoconvVersion();
        Assert.assertFalse(Strings.isNullOrEmpty(unoconvVersion));

        String convertVersion = service.getConvertVersion();
        Assert.assertFalse(Strings.isNullOrEmpty(convertVersion));
    }

    @Test
    public void testSlideExtraction() {
    }

}
