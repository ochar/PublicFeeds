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
package publicfeeds.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

/**
 * A Feed Item. Contains Media.
 *
 * @author io
 */
@Entity
public class Item implements Serializable {
	
	@Id
	private String id;
	
	private String title;

	private String link;

	private Media media;
	
	private Instant takenDate;
	
	@Lob
	private String htmlDescription;
	
	private Instant publishedDate;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Author author;
	
	protected Item() {
	}

	/**
	 * Creates a new feed Item.
	 *
	 * @param id id of the feed item.
	 * @param title title of the feed item.
	 * @param link url to the feed item.
	 * @param media media contained in the feed item.
	 * @param takenDate date which media is taken.
	 * @param htmlDescription description of the feed item in html.
	 * @param publishedDate date which the feed item is published.
	 * @param author author of the feed item.
	 */
	public Item(String id, String title, String link, Media media, 
			Instant takenDate, String htmlDescription, Instant publishedDate, 
			Author author) {
		this.id = id;
		this.title = title;
		this.link = link;
		this.media = media;
		this.takenDate = takenDate;
		this.htmlDescription = htmlDescription;
		this.publishedDate = publishedDate;
		this.author = author;
	}
	
	/**
	 * Returns id of this item.
	 *
	 * @return id of this item
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id of this item.
	 *
	 * @param id the id to be set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Returns title of this item.
	 *
	 * @return title of this item
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title of this item.
	 *
	 * @param title the title to be set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Returns url to this item.
	 *
	 * @return url to this item
	 */
	public String getLink() {
		return link;
	}
	
	/**
	 * Sets the url to this item.
	 *
	 * @param link the url to be set.
	 */
	public void setLink(String link) {
		this.link = link;
	}
	
	/**
	 * Returns media content of this item.
	 *
	 * @return media content of this item
	 */
	public Media getMedia() {
		return media;
	}
	
	/**
	 * Sets the media content of this item.
	 *
	 * @param media the media content to be set.
	 */
	public void setMedia(Media media) {
		this.media = media;
	}
	
	/**
	 * Returns date which this item media is taken.
	 *
	 * @return date which this item media is taken
	 */
	public Instant getTakenDate() {
		return takenDate;
	}
	
	/**
	 * Sets date which this item media is taken.
	 *
	 * @param takenDate the date to be set.
	 */
	public void setTakenDate(Instant takenDate) {
		this.takenDate = takenDate;
	}
	
	/**
	 * Returns description of this feed in html.
	 *
	 * @return description of this feed in html
	 */
	public String getHtmlDescription() {
		return htmlDescription;
	}
	
	/**
	 * Sets the description of this feed in html.
	 *
	 * @param htmlDescription the description to be set.
	 */
	public void setHtmlDescription(String htmlDescription) {
		this.htmlDescription = htmlDescription;
	}
	
	/**
	 * Returns published date of this feed.
	 *
	 * @return published date of this feed
	 */
	public Instant getPublishedDate() {
		return publishedDate;
	}
	
	/**
	 * Sets the published date of this feed.
	 *
	 * @param publishedDate published date to be set.
	 */
	public void setPublishedDate(Instant publishedDate) {
		this.publishedDate = publishedDate;
	}
	
	/**
	 * Returns author of this item.
	 *
	 * @return author of this item
	 */
	public Author getAuthor() {
		return author;
	}
	
	/**
	 * Sets the author of this item.
	 * 
	 * @param author the author to be set
	 */
	public void setAuthor(Author author) {
		this.author = author;
	}
	
	@Override
	public String toString() {
		return "Item{" 
				+ "id=" + id 
				+ ", title=" + title 
				+ ", link=" + link 
				+ ", media=" + media.toString()
				+ ", takenDate=" + takenDate 
				+ ", htmlDescription=" + htmlDescription 
				+ ", publishedDate=" + publishedDate 
				+ ", author=" + author.toString() + '}';
	}
	
}
