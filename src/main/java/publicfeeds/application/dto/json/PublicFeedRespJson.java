/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.application.dto.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.Instant;
import java.util.List;
import static java.util.stream.Collectors.toList;
import publicfeeds.domain.Item;

/**
 * A DTO representing top level response of the public feed.
 *
 * @author io
 */
public class PublicFeedRespJson {
	
	private String title;
	
	private String link;
	
	private String description;
	
	@JsonDeserialize(converter = InstantConverter.class)
	private Instant modified;
	
	private String generator;
	
	private List<PublicFeedItemJson> items;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Instant getModified() {
		return modified;
	}

	public void setModified(Instant modified) {
		this.modified = modified;
	}

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public List<PublicFeedItemJson> getItems() {
		return items;
	}

	public void setItems(List<PublicFeedItemJson> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "PublicFeedRespJson{" 
				+ "title=" + title 
				+ ", link=" + link 
				+ ", description=" + description 
				+ ", modified=" + modified 
				+ ", generator=" + generator 
				+ ", items=" + items + '}';
	}
	
	/**
	 * Shortcut methods to convert all feed items to Item domain object.
	 * 
	 * @return List of Item domain object contained in this DTO.
	 */
	public List<Item> toItems() {
		return getItems().stream()
				.map(itemJson -> itemJson.toItem())
				.collect(toList());
	}
	
}
