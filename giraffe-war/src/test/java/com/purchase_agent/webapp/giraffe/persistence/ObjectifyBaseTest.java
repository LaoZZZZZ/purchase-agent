package com.purchase_agent.webapp.giraffe.persistence;
import com.google.appengine.tools.development.testing.*;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;
import com.googlecode.objectify.util.Closeable;
import org.junit.Before;
import org.junit.After;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import com.purchase_agent.webapp.giraffe.objectify_entity.Entities;

/**
 * Base test class for unit test of Dao class.
 * Created by lukez on 2/25/17.a
 */
public class ObjectifyBaseTest {
    protected final LocalServiceTestHelper localServiceTestHelper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy(), new LocalMemcacheServiceTestConfig());
            //new LocalSearchServiceTestConfig()), new LocalBlobstoreServiceTestConfig());
            //(new LocalTaskQueueTestConfig()).setQueueXmlPath("war/WEB-INF/queue.xml").setDisableAutoTaskExecution(false));
    private static Closeable closeable;

    @Before
    public void setUp() {
        this.localServiceTestHelper.setUp();
    }

    @After
    public void tearDown() {
        this.localServiceTestHelper.tearDown();
    }

    @BeforeClass
    public static void setUpObjectify() {
        closeable = ObjectifyService.begin();

        JodaTimeTranslators.add(ObjectifyService.factory());
        for (Class entity : Entities.entities) {
            ObjectifyService.register(entity);
        }
    }

    @AfterClass
    public static void tearDownObjectify() {
        closeable.close();
    }
}
