package no.mesan.fagark.reaktiv.logistikk;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;

import no.mesan.fagark.reaktiv.logistikk.web.AtomXmlProvider;
import no.mesan.fagark.reaktiv.logistikk.web.EiendelResource;
import no.mesan.fagark.reaktiv.logistikk.web.EierResource;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.sun.net.httpserver.HttpServer;

/**
 * Klassen starter applikasjonen. Den håndterer oppstart av database, webserver
 * og leser konfigurasjoner. Databasen som brukes er referert via en statisk
 * referanse i klassen EmbeddedDb.
 * 
 * 
 * @author arne
 *
 */
public class App {
    public static Properties properties = loadApplicationProperties();
    private static Logger logger = Logger.getLogger(App.class.getName());

    public static void main(final String[] args) {
        try {
            new App();
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "Klarte ikke starte applikasjonen.", e);
        }
    }

    public App() {
        EmbeddedDb.initDatabase();
        startWebServer();
    }

    protected void startWebServer() {

        final UriBuilder builder = UriBuilder.fromPath(properties.getProperty(PropertyEnum.ROOT_PATH.toString()));

        final URI baseUri = builder.host(properties.getProperty(PropertyEnum.LOCALHOST.toString()))
                .port(new Integer(properties.getProperty(PropertyEnum.LOCALPORT.toString()))).scheme("http").build();

        final ResourceConfig config = buildWSConfig();
        final HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
    }

    protected ResourceConfig buildWSConfig() {
        // Jersey uses java.util.logging - bridge to slf4
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        final ResourceConfig config = new ResourceConfig();
        config.register(new LoggingFilter(logger, true));
        config.register(EiendelResource.class);
        config.register(EierResource.class);
        config.register(AtomXmlProvider.class);

        return config;
    }

    /**
     * Loads the external application properties.
     * 
     * @return props.
     */
    public static Properties loadApplicationProperties() {
        try (final InputStream inputStream = App.class.getClassLoader().getResourceAsStream(
                PropertyEnum.APP_CONFIG_FILE.toString())) {
            final Properties prop = new Properties();
            prop.load(inputStream);
            return prop;
        } catch (final IOException io) {
            throw new RuntimeException(io);
        }
    }

}
