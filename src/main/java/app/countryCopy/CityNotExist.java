package app.countryCopy;

public class CityNotExist extends Exception{
    public CityNotExist(String label) {
        super("cityUtils.Building "+label+" not exists");
    }
}
