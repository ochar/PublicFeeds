/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.interfaces.rest.support;

/**
 *
 * @author io
 */
public class ErrorResponse {
	
	private static final String DEFAULT_STATUS_CODE = "500";
	
	
	private final String statusCode;
	
	private final String reason;

	public ErrorResponse(String statusCode, String reason) {
		this.statusCode = statusCode;
		this.reason = reason;
	}
	
	public ErrorResponse(String statusCode, Exception ex) {
		this.statusCode = statusCode;
		this.reason = ex.getMessage();
	}
	
	public ErrorResponse(Exception ex) {
		this.statusCode = DEFAULT_STATUS_CODE;
		this.reason = ex.getMessage();
	}

	public String getStatusCode() {
		return statusCode;
	}

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
