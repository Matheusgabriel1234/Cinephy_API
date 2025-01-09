package projetos.test.Cinephy.Exceptions;


public class MovieNotFoundException extends RuntimeException {

    public MovieNotFoundException(String message){
        super(message);
    }

}
