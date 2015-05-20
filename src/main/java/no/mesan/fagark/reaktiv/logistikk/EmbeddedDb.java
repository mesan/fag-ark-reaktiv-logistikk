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

    public static DB database;

    // = DBMaker.newFileDB(new
    // File(App.properties.getProperty(PropertyEnum.DATABASE.toString())))
    // .closeOnJvmShutdown().make();

    public static void initDatabase() {
        final File dbFile = new File(App.properties.getProperty(PropertyEnum.DATABASE.toString()));

        if (dbFile.getParentFile().isDirectory()) {
            database = DBMaker.newFileDB(dbFile).closeOnJvmShutdown().make();
            return;
        }

        if (dbFile.getParentFile().mkdir()) {
            database = DBMaker.newFileDB(dbFile).closeOnJvmShutdown().make();
            return;
        }

        throw new RuntimeException("Får ikke opprettet database.");
    }

}
