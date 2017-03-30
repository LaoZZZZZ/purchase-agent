package com.purchase_agent.webapp.giraffe.script;

import com.google.common.base.Throwables;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;
import com.purchase_agent.webapp.giraffe.objectify_entity.Entities;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.utils.Currency;
import com.purchase_agent.webapp.giraffe.utils.MoneyAmount;
import org.joda.time.DateTime;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * A remote script util that helps to login
 * Created by lukez on 2/5/17.
 */
public class ScriptUtil {
    private RemoteApiOptions remoteApiOptions;
    private RemoteApiInstaller installer;
    private final String APP_HOSTNAME = "purchase-agent.appspot.com";
    private final int PORT = 443;

    public void login() {
        try {
            remoteApiOptions = new RemoteApiOptions()
                    .server(APP_HOSTNAME, PORT).useApplicationDefaultCredential();
            installer = new RemoteApiInstaller();
            installer.install(remoteApiOptions);
            registerEntities();
        } catch (final IOException exp) {
            Throwables.propagate(exp);
        }
    }

    private void registerEntities() {
        JodaTimeTranslators.add(ObjectifyService.factory());
        for (Class<?> clazz : Entities.entities) {
            ObjectifyService.register(clazz);
        }
    }

    public void logout() {
        installer.uninstall();
    }
    public static void main(String[] argvs) throws IOException {

        ScriptUtil util = new ScriptUtil();
        util.login();

        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

        Transaction transaction = new Transaction();
        transaction.setStatus(Transaction.Status.RESERVE);
        transaction.setCustomerId(1);
        transaction.setCreationTime(DateTime.now());
        transaction.setDeleted(false);
        transaction.setId(UUID.randomUUID().toString());
        transaction.setMoneyAmount(new MoneyAmount());
        transaction.getMoneyAmount().setAmount(BigDecimal.ONE);
        transaction.getMoneyAmount().setCurrency(Currency.RMB);
        transaction.setSaler("test");
        ObjectifyService.ofy().save().entity(transaction).now();
        util.logout();
        System.exit(0);

    }
}
