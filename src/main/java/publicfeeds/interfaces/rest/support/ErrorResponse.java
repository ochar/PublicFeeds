/*
 * Copyright (C) 2020 io
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
