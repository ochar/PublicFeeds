/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.domain;

import java.util.List;

/**
 *
 * @author io
 */
public class Media {
	
	private String contentUrl;
	
	private String title;
	
	private String htmlDescription;
	
	private String thumbnailUrl;
	
	private String credit;
	
	private List<String> tags;

	public Media() {
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
