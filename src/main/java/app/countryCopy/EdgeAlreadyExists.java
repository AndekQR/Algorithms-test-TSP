package app.countryCopy;

public class EdgeAlreadyExists extends Exception{
    public EdgeAlreadyExists(String vertexOne, String vertexTwo) {
        super("Road between "+vertexOne+" and "+vertexTwo+" already exists");
    }
}
