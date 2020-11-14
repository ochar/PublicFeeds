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

	public Media(String contentUrl, String title, List<String> tags) {
		this.contentUrl = contentUrl;
		this.title = title;
		this.tags = tags;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHtmlDescription() {
		return htmlDescription;
	}

	public void setHtmlDescription(String htmlDescription) {
		this.htmlDescription = htmlDescription;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getCredit() {
		return credit;
	}

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
