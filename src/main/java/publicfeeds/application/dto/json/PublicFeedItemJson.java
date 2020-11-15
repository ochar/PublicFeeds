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
import java.time.OffsetDateTime;
import java.util.List;
import publicfeeds.domain.Author;
import publicfeeds.domain.Item;
import publicfeeds.domain.Media;

/**
 * DTO of a single feed item.
 *
 * @author io
 */
public class PublicFeedItemJson {
	
	public static class MediaJson {
		private String m;
		public MediaJson() {}

		public String getM() {
			return m;
		}

		public void setM(String m) {
			this.m = m;
		}

		@Override
		public String toString() {
			return "Media{" + "m=" + m + '}';
		}
	}
	
	
	private String title;
	
	private String link;
	
	private MediaJson media;
	
	private String date_taken;
	
	private String description;
	
	private String published;
	
	private String author;
	
	private String author_id;
	
	@JsonDeserialize(converter = TagsListConverter.class)
	private List<String> tags;
	
	
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

	public MediaJson getMedia() {
		return media;
	}

	public void setMedia(MediaJson media) {
		this.media = media;
	}

	public String getDate_taken() {
		return date_taken;
	}

	public void setDate_taken(String date_taken) {
		this.date_taken = date_taken;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}

	public List<String> getTags() {
		return tags;
	}
	
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "PublicFeedItemJson{" 
				+ "title=" + title 
				+ ", link=" + link 
				+ ", media=" + media.toString()
				+ ", date_taken=" + date_taken 
				+ ", description=" + description 
				+ ", published=" + published 
				+ ", author=" + author 
				+ ", author_id=" + author_id 
				+ ", tags=" + tags + '}';
	}
	
	/**
	 * Converts this feed item DTO to an Item domain object
	 * 
	 * @return an Item domain object with data from this DTO
	 */
	public Item toItem() {
		
		// parse author name
		int start = author.indexOf("(\"") + 2;
		int end = author.indexOf("\")", start);
		String authorName = author.substring(start, end);
		
		Author authorObj = new Author(author_id, authorName);
		
		Media mediaObj = new Media(media.m, title, tags);
		
		// parse date_taken & published
		Instant date_takenInstant = OffsetDateTime.parse(date_taken).toInstant();
		Instant publishedInstant = Instant.parse(published);
		
		String[] splits = link.split("/");
		String itemId = splits[splits.length - 1];
		
		return new Item(itemId, title, link, mediaObj, date_takenInstant, description, 
				publishedInstant, authorObj);
	}
	
	
}
