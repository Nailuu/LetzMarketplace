package lu.letzmarketplace.restapi.exceptions;

public class InvalidJWTTokenException extends RuntimeException {
    public InvalidJWTTokenException() {
        super("Invalid JWT token");
    }
}
