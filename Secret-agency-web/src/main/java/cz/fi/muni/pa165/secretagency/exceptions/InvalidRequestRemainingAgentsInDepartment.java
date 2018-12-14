package cz.fi.muni.pa165.secretagency.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Author: Adam Kral <433328>
 * Date: 12/14/18
 * Time: 6:32 PM
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Cannot delete department, there are still some agents")
public class InvalidRequestRemainingAgentsInDepartment extends RuntimeException {
    public InvalidRequestRemainingAgentsInDepartment() {}
}
