package countryCopy;

public class BuildingNotExists extends Exception{
    public BuildingNotExists(String label) {
        super("cityUtils.Building "+label+" not exists");
    }
}
