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
 * An application event with a related user and feed item.
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
	
	/**
	 * Creates new user event.
	 * 
	 * @param time time which the event occurs
	 * @param username username of user which related to the event
	 * @param message readable message about the event
	 * @param itemId id of item which related to the event
	 */
	public UserEvent(Instant time, String username, String message, String itemId) {
		this.time = time;
		this.username = username;
		this.message = message;
		this.itemId = itemId;
		this.url = "";
	}
	
	/**
	 * Creates new user event with a related url.
	 * 
	 * @param time time which the event occurs
	 * @param username username of user which related to the event
	 * @param message readable message about the event
	 * @param itemId id of item which related to the event
	 * @param url a url related to this event
	 */
	public UserEvent(Instant time, String username, String message, String itemId, String url) {
		this.time = time;
		this.username = username;
		this.message = message;
		this.itemId = itemId;
		this.url = url;
	}
	
	/**
	 * Returns id of this event.
	 *
	 * @return id of this event
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets the id of this event.
	 *
	 * @param id the id to be set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Returns time which this event occurs.
	 *
	 * @return time which this event occurs
	 */
	public Instant getTime() {
		return time;
	}
	
	/**
	 * Sets the time which this event occurs.
	 *
	 * @param time the time to be set.
	 */
	public void setTime(Instant time) {
		this.time = time;
	}
	
	/**
	 * Returns username of user related to this event.
	 *
	 * @return username of user related to this event
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets the username of user related to this event.
	 *
	 * @param username the username to be set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Returns message of this event.
	 *
	 * @return message of this event
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the message of this event.
	 *
	 * @param message the message to be set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Returns item id related to this event.
	 *
	 * @return item id related to this event
	 */
	public String getItemId() {
		return itemId;
	}
	
	/**
	 * Sets the item id related to this event.
	 *
	 * @param itemId the item id to be set.
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	/**
	 * Returns related url of this event.
	 *
	 * @return related url of this event
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Sets the related url of this event.
	 *
	 * @param url the url to be set.
	 */
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
