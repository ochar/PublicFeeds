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

	public ItemLike() {
	}
	
	public ItemLike(Item item) {
		this.item = item;
		this.submittedTime = Instant.now();
		this.username = ANON_USER;
	}

	public ItemLike(Item item, String username) {
		this.item = item;
		this.submittedTime = Instant.now();
		this.username = (username == null || username.trim().isEmpty()) ? ANON_USER : username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}	

	public Instant getSubmittedTime() {
		return submittedTime;
	}

	public void setSubmittedTime(Instant submittedTime) {
		this.submittedTime = submittedTime;
	}
	
	public String getUsername() {
		return username;
	}

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
