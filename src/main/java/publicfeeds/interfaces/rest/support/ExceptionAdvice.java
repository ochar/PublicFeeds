/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.interfaces.rest.support;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Global exception handler for controllers
 *
 * @author io
 */
@ControllerAdvice
public class ExceptionAdvice {
	
	/**
	 * Handles error when controller method being passed argument of wrong type.
	 *
	 * @param ex the exception thrown
	 * @return ErrorResponse object with 400 status code
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ErrorResponse handleMismatchArgumentType(MethodArgumentTypeMismatchException ex) {
		return new ErrorResponse("400", ex);
	}
	
	/**
	 * Handles error when required parameter is missing.
	 *
	 * @param ex the exception thrown
	 * @return ErrorResponse object with 400 status code
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ErrorResponse handleMissingArgument(MissingServletRequestParameterException ex) {
		return new ErrorResponse("400", ex);
	}
	
	/**
	 * Handles IllegalArgumentException
	 *
	 * @param ex the exception thrown
	 * @return ErrorResponse object with 400 status code
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
		return new ErrorResponse("400", ex);
	}
	
	/**
	 * Handles generic catch all RuntimeException
	 *
	 * @param ex the exception thrown
	 * @return ErrorResponse object with 500 status code
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(RuntimeException.class)
	public ErrorResponse handleRuntimeException(Exception ex) {
		return new ErrorResponse(ex);
	}
	
}
