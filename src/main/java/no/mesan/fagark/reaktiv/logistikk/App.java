package no.mesan.fagark.reaktiv.logistikk;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import javax.ws.rs.core.UriBuilder;

import no.mesan.fagark.reaktiv.logistikk.web.AtomXmlProvider;
import no.mesan.fagark.reaktiv.logistikk.web.EiendelController;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger logger = LoggerFactory.getLogger(App.class);
    public static Properties properties = loadApplicationProperties();

    public App() {
        // Gjør en sjekk på at database blir opprettet og kjører, samt for å
        // synligjøre den i koden.
        if (!EmbeddedDb.database.isClosed()) {
            startWebServer();
        }
    }

    public static void main(final String[] args) {
        try {
            new App();
            logger.trace("Applikasjon startet.");
        } catch (final Exception e) {
            logger.error("Klarte ikke starte applikajon.", e);
        }
    }

    protected void startWebServer() {

        final UriBuilder builder = UriBuilder.fromPath(properties.getProperty(PropertyEnum.ROOT_PATH.toString()));


        final URI baseUri = builder.host(properties.getProperty(PropertyEnum.LOCALHOST.toString()))
                .port(new Integer(properties.getProperty(PropertyEnum.LOCALPORT.toString()))).scheme("http").build();

        final ResourceConfig config = buildWSConfig();
        final HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
    }

    protected ResourceConfig buildWSConfig() {
        final ResourceConfig config = new ResourceConfig();
        config.register(EiendelController.class);
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
            logger.error("Feilet ved last av propperties", io);
        }

        return new Properties();
    }

}
