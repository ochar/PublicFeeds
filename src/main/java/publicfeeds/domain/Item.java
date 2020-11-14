/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public Instant getTakenDate() {
		return takenDate;
	}

	public void setTakenDate(Instant takenDate) {
		this.takenDate = takenDate;
	}

	public String getHtmlDescription() {
		return htmlDescription;
	}

	public void setHtmlDescription(String htmlDescription) {
		this.htmlDescription = htmlDescription;
	}

	public Instant getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Instant publishedDate) {
		this.publishedDate = publishedDate;
	}

	public Author getAuthor() {
		return author;
	}

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
