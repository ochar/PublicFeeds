/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author io
 */
@Entity
public class UserEvent implements Serializable {
	
	@Id @GeneratedValue
	private Long id;
	
	private Instant time;
	
	private String username;
	
	private String message;
	
	private String itemId;
	
	private String url;

	protected UserEvent() {
	}
	
	public UserEvent(Instant time, String username, String message, String itemId) {
		this.time = time;
		this.username = username;
		this.message = message;
		this.itemId = itemId;
		this.url = "";
	}

	public UserEvent(Instant time, String username, String message, String itemId, String url) {
		this.time = time;
		this.username = username;
		this.message = message;
		this.itemId = itemId;
		this.url = url;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getTime() {
		return time;
	}

	public void setTime(Instant time) {
		this.time = time;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "UserEvent{" 
				+ "id=" + id 
				+ ", time=" + time 
				+ ", username=" + username 
				+ ", message=" + message 
				+ ", itemId=" + itemId 
				+ ", url=" + url + '}';
	}	
	
}
