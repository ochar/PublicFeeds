/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.interfaces.rest.support;

/**
 * Response to be returned by RestController when an error occurs.
 *
 * @author io
 */
public class ErrorResponse {
	
	private static final String DEFAULT_STATUS_CODE = "500";
	
	
	private final String statusCode;
	
	private final String reason;
	
	/**
	 * Creates an error response with a defined status code and reason message.
	 * 
	 * @param statusCode http status code of this error response
	 * @param reason reason of this error
	 */
	public ErrorResponse(String statusCode, String reason) {
		this.statusCode = statusCode;
		this.reason = reason;
	}
	
	/**
	 * Creates an error response with a defined status code.
	 * 
	 * @param statusCode http status code of this error response
	 * @param ex exception related to this error response
	 */
	public ErrorResponse(String statusCode, Exception ex) {
		this.statusCode = statusCode;
		this.reason = ex.getMessage();
	}
	
	/**
	 * Creates an error response with default status code.
	 * 
	 * @param ex exception related to this error response
	 */
	public ErrorResponse(Exception ex) {
		this.statusCode = DEFAULT_STATUS_CODE;
		this.reason = ex.getMessage();
	}
	
	/**
	 * Returns http status code of this error response.
	 * 
	 * @return http status code of this error response
	 */
	public String getStatusCode() {
		return statusCode;
	}
	
	/**
	 * Returns reason of this error.
	 * 
	 * @return reason of this error
	 */
	public String getReason() {
		return reason;
	}

	@Override
	public String toString() {
		return "ErrorResponse{" 
				+ "statusCode=" + statusCode 
				+ ", reason=" + reason + '}';
	}
	
}
