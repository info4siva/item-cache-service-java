package com.services;

import com.services.constants.ItemCacheConstants;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;
/**
 * This main application class configures Jersey container and starts Jetty Web Server on the given port
 * to listen to HTTP requests from external clients
 * @author info4siva
 **/
public class ItemCacheJerseyApplication {
    private static final Logger logger = LoggerFactory.getLogger(ItemCacheJerseyApplication.class);

    public static void main(String[] args) {
        Server server = new Server(ItemCacheConstants.SERVER_CONFIG_PORT);
        ServletContextHandler servletContextHandler = new ServletContextHandler(NO_SESSIONS);
        servletContextHandler.setContextPath("/");
        server.setHandler(servletContextHandler);
        ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class,
                ItemCacheConstants.SERVER_CONFIG_PATH_SPEC);
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter(
                "jersey.config.server.provider.classnames",
                "com.services.controllers.ItemCacheController"
        );
        try {
            server.start();
            server.join();
        } catch (Exception serverExec) {
            logger.error("Error occurred while starting Item-Cache Jetty Server", serverExec);
            System.exit(1);
        }
        finally {
            server.destroy();
        }
    }
}
