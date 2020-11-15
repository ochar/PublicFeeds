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
 * Like for a feed item.
 * Has a related username.
 *
 * @author io
 */
@Entity
public class ItemLike implements Serializable {
	
	public static final String ANON_USER = "Anonymous";
	
	
	@Id @GeneratedValue
	private Long id;
	
	@JsonProperty("itemId")
	@JsonSerialize(converter = ItemToIdConverter.class)
	@ManyToOne
	private Item item;
	
	private Instant submittedTime;
	
	private String username;

	protected ItemLike() {
	}
	
	/**
	 * Creates a new like for an Item without a username.
	 * 
	 * @param item feed item which the like belongs to
	 */
	public ItemLike(Item item) {
		this.item = item;
		this.submittedTime = Instant.now();
		this.username = ANON_USER;
	}
	
	/**
	 * Creates a new like for an Item with a username.
	 * 
	 * @param item feed item which the like belongs to
	 * @param username username of user which own the comment
	 */
	public ItemLike(Item item, String username) {
		this.item = item;
		this.submittedTime = Instant.now();
		this.username = (username == null || username.trim().isEmpty()) ? ANON_USER : username;
	}
	
	/**
	 * Returns id of this like.
	 *
	 * @return id of this like
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets the id of this like.
	 *
	 * @param id the id to be set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Returns item which this like belongs.
	 *
	 * @return item which this like belongs
	 */
	public Item getItem() {
		return item;
	}
	
	/**
	 * Sets the item which this like belongs.
	 *
	 * @param item the item to be set.
	 */
	public void setItem(Item item) {
		this.item = item;
	}	
	
	/**
	 * Returns submitted time of this like.
	 *
	 * @return submitted time of this like
	 */
	public Instant getSubmittedTime() {
		return submittedTime;
	}
	
	/**
	 * Sets the submitted time of this like.
	 *
	 * @param submittedTime the username to be set.
	 */
	public void setSubmittedTime(Instant submittedTime) {
		this.submittedTime = submittedTime;
	}
	
	/**
	 * Returns username of this like submitter.
	 *
	 * @return username of this like submitter
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets the username of this like submitter.
	 *
	 * @param username the username to be set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "ItemLike{" 
				+ "id=" + id 
				+ ", item=" + item 
				+ ", submittedTime=" + submittedTime 
				+ ", username=" + username + '}';
	}
	
}
