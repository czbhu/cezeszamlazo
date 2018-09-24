/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.sym;

import org.jumpmind.symmetric.SymmetricWebServer;

/**
 *
 * @author szekus
 */
public class StartSymmetricEngine {

    SymmetricWebServer node;

    private StartSymmetricEngine() throws Exception {
        node = new SymmetricWebServer(
                "conf/corp-000.properties");
    }

    public static StartSymmetricEngine create() throws Exception {
        return new StartSymmetricEngine();
    }

    public void start() throws Exception {
        // this will create the database, sync triggers, start jobs running
        node.start(8080);
    }

    public void stop() throws Exception {
        // this will stop the node
        node.stop();
    }
}
