package com.purchase_agent.webapp.giraffe.script;

import com.google.appengine.api.datastore.Entity;
import com.google.common.base.Throwables;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import java.io.IOException;

/**
 * A remote scrit util that helps to login
 * Created by lukez on 2/5/17.
 */
public class ScriptUtil {
    private RemoteApiOptions remoteApiOptions;
    private RemoteApiInstaller installer;
    private final String APP_DOMAIN = "purchase-agent.appspot.com";
    private final int PORT = 443;

    public void login() {
        try {
            remoteApiOptions = new RemoteApiOptions()
                    .server("purchase-agent.appspot.com", 443).useApplicationDefaultCredential();
            installer = new RemoteApiInstaller();
            installer.install(remoteApiOptions);
        } catch (final IOException exp) {
            Throwables.propagate(exp);
        }
    }

    public void logout() {
        installer.uninstall();
    }
    public static void main(String[] argvs) throws IOException {

        ScriptUtil util = new ScriptUtil();
        util.login();

        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        System.out.println("Key of new entity is " + ds.put(new Entity("Hello Remote API!")));

        util.logout();
        System.exit(0);

    }
}
