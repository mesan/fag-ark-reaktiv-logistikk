package no.mesan.fagark.reaktiv.logistikk;

import java.io.File;

import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 * Global statisk key/value database tilgjengelig for applikasjonen.
 * 
 * @author arne
 *
 */
public class EmbeddedDb {
    public static DB database = DBMaker.newFileDB(new File(App.properties.getProperty(PropertyEnum.DATABASE.toString())))
            .closeOnJvmShutdown().make();

}
