package cz.fi.muni.pa165.secretagency.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Author: Adam Kral <433328>
 * Date: 1/12/19
 * Time: 9:57 PM
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="Mission name already exists")
public class InvalidMissionNameException extends RuntimeException {

}
