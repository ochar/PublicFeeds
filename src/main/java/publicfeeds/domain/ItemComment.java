/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import publicfeeds.interfaces.json.dto.ItemToIdConverter;

/**
 * Comment for a feed item.
 * Has a related username.
 *
 * @author io
 */
@Entity
public class ItemComment implements Serializable {
	
	public static final String ANON_USER = "Anonymous";
	
	
	@Id @GeneratedValue
	private Long id;
	
	private String content;
	
	@JsonProperty("itemId")
	@JsonSerialize(converter = ItemToIdConverter.class)
	@ManyToOne
	private Item item;

	private Instant submittedTime;

	private String username;

	protected ItemComment() {
	}
	
	/**
	 * Creates a new comment for an Item without a username.
	 * 
	 * @param content content of the comment
	 * @param item feed item which the comment belongs to
	 */
	public ItemComment(String content, Item item) {
		this.content = content;
		this.item = item;
		this.submittedTime = Instant.now();
		this.username = ANON_USER;
	}
	
	/**
	 * Creates a new comment for an Item with a username.
	 * 
	 * @param content content of the comment
	 * @param item feed item which the comment belongs to
	 * @param username username of user which own the comment
	 */
	public ItemComment(String content, Item item, String username) {
		this.content = content;
		this.item = item;
		this.submittedTime = Instant.now();
		this.username = (username == null || username.trim().isEmpty()) ? ANON_USER : username;
	}
	
	/**
	 * Returns id of this comment.
	 *
	 * @return id of this comment
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets the id of this comment.
	 *
	 * @param id the id to be set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Returns content of this comment.
	 *
	 * @return content of this comment
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * Sets the content of this comment.
	 *
	 * @param content the content to be set.
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * Returns item which this comment belongs.
	 *
	 * @return item which this comment belongs
	 */
	public Item getItem() {
		return item;
	}
	
	/**
	 * Sets the item which this comment belongs.
	 *
	 * @param item the item to be set.
	 */
	public void setItem(Item item) {
		this.item = item;
	}
	
	/**
	 * Returns submitted time of this comment.
	 *
	 * @return submitted time of this comment
	 */
	public Instant getSubmittedTime() {
		return submittedTime;
	}
	
	/**
	 * Sets the submitted time of this comment.
	 *
	 * @param submittedTime the username to be set.
	 */
	public void setSubmittedTime(Instant submittedTime) {
		this.submittedTime = submittedTime;
	}
	
	/**
	 * Returns username of this comment submitter.
	 *
	 * @return username of this comment submitter
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets the username of this comment submitter.
	 *
	 * @param username the username to be set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "ItemComment{" 
				+ "id=" + id 
				+ ", content=" + content 
				+ ", item=" + item 
				+ ", submittedTime=" + submittedTime 
				+ ", username=" + username + '}';
	}
	
}
