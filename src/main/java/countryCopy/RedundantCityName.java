package countryCopy;

public class RedundantCityName extends Exception{
    public RedundantCityName(String label) {
        super("City "+label+" already exists");
    }
}
