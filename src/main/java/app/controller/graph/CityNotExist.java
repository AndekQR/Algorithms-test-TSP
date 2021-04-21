package app.controller.graph;

public class CityNotExist extends Exception {

    public CityNotExist(String label) {
        super("cityUtils.Building " + label + " not exists");
    }
}
