/**
 * Created by Petean Mihai on 3/20/2017.
 */
public class RentalException extends Exception{
    public RentalException() {
    }

    public RentalException(String message) {
        super(message);
    }

    public RentalException(String message, Throwable cause) {
        super(message, cause);
    }

    public RentalException(Throwable cause) {
        super(cause);
    }

    public RentalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
