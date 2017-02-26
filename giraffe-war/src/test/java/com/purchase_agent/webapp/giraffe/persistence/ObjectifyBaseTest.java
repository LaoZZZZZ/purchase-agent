package com.purchase_agent.webapp.giraffe.persistence;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.JodaMoneyTranslators;
import com.googlecode.objectify.util.Closeable;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import com.purchase_agent.webapp.giraffe.objectify_entity.Entities;

/**
 * Base test class for unit test of Dao class.
 * Created by lukez on 2/25/17.a
 */
public class ObjectifyBaseTest {
    private static Closeable closeable;

    @BeforeClass
    public static void setUpObjectify() {
        closeable = ObjectifyService.begin();

        JodaMoneyTranslators.add(ObjectifyService.factory());
        for (Class entity : Entities.entities) {
            ObjectifyService.register(entity);
        }
    }

    @AfterClass
    public static void tearDownObjectify() {
        closeable.close();
    }
}
