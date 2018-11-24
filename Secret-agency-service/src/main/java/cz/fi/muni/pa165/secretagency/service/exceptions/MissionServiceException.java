package cz.fi.muni.pa165.secretagency.service.exceptions;

/**
 * Class for exceptions thrown by Mission Service.
 *
 * @author Adam Skurla (487588)
 */
public class MissionServiceException extends RuntimeException {

    public MissionServiceException(String message) {
        super(message);
    }
}
