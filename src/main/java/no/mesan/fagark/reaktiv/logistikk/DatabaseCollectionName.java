package no.mesan.fagark.reaktiv.logistikk;

public enum DatabaseCollectionName {
    EIER("eier"), KONTROLL("kontroll");
    
    DatabaseCollectionName(final String pName){
        name = pName;
    }
    
    @Override
    public String toString() {
        return name;
    }
    String name;
}
