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
