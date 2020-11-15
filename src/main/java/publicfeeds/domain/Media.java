/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

/**
 * Media content of a feed item.
 *
 * @author io
 */
@Embeddable
public class Media implements Serializable {
	
	@Column(name = "media_content_url")
	private String contentUrl;
	
	@Column(name = "media_title")
	private String title;
	
	@Column(name = "media_html_description")
	@Lob
	private String htmlDescription;
	
	@Column(name = "media_thumbnail_url")
	private String thumbnailUrl;
	
	@Column(name = "media_credit")
	private String credit;
	
	@ElementCollection
	private List<String> tags;

	protected Media() {
	}
	
	/**
	 * Creates a new feed item media content.
	 * 
	 * @param contentUrl url to the media content
	 * @param title title of the media content
	 * @param tags list of tags of the media content
	 */
	public Media(String contentUrl, String title, List<String> tags) {
		this.contentUrl = contentUrl;
		this.title = title;
		this.tags = tags;
	}

	/**
	 * Returns list of tags of this media.
	 *
	 * @return list of tags of this media
	 */
	public List<String> getTags() {
		return tags;
	}
	
	/**
	 * Sets the list of tags of this media.
	 * 
	 * @param tags the list of tags to be set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	/**
	 * Returns url to this media.
	 *
	 * @return url to this media
	 */
	public String getContentUrl() {
		return contentUrl;
	}
	
	/**
	 * Sets the url to this media.
	 * 
	 * @param contentUrl the url to be set
	 */
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	
	/**
	 * Returns title of this media.
	 *
	 * @return title of this media
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title of this item.
	 * 
	 * @param title the title to be set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Returns description of this media in html.
	 *
	 * @return description of this media in html
	 */
	public String getHtmlDescription() {
		return htmlDescription;
	}
	
	/**
	 * Sets the description of this media in html.
	 * 
	 * @param htmlDescription the description to be set
	 */
	public void setHtmlDescription(String htmlDescription) {
		this.htmlDescription = htmlDescription;
	}
	
	/**
	 * Returns url to thumbnail of this media.
	 *
	 * @return url to thumbnail of this media
	 */
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	
	/**
	 * Sets the url to thumbnail of this media.
	 * 
	 * @param thumbnailUrl the url to be set
	 */
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	
	/**
	 * Returns credit of this media.
	 *
	 * @return credit of this media
	 */
	public String getCredit() {
		return credit;
	}
	
	/**
	 * Sets the credit of this media.
	 * 
	 * @param credit the credit to be set
	 */
	public void setCredit(String credit) {
		this.credit = credit;
	}

	@Override
	public String toString() {
		return "Media{" 
				+ "contentUrl=" + contentUrl 
				+ ", title=" + title 
				+ ", htmlDescription=" + htmlDescription 
				+ ", thumbnailUrl=" + thumbnailUrl 
				+ ", credit=" + credit 
				+ ", tags=" + tags + '}';
	}	
	
}
