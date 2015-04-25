package no.mesan.fagark.reaktiv.logistikk;


public enum PropertyEnum {
    APP_CONFIG_FILE("logistikk-config.properties"), SCHEME("http"), ROOT_PATH("rootpath"), LOCALHOST("localhost"), LOCALPORT(
            "localport"), DATABASE("database");

    private String propertyValue;

    PropertyEnum(final String value) {
        propertyValue = value;
    }

    @Override
    public String toString() {
        return propertyValue;
    }

}
