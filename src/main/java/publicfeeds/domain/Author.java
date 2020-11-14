/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author io
 */
@Entity
public class Author implements Serializable {
	
	public static final String profileBaseUrl = "https://www.flickr.com/people/";
	
	@Id
	private String id;
	
	private String name;
	
	private String profileUrl = "";

	protected Author() {
	}
	
	public Author(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Author(String id, String name, String profileUrl) {
		this.id = id;
		this.name = name;
		this.profileUrl = profileUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	@Override
	public String toString() {
		return "Author{" 
				+ "id=" + id 
				+ ", name=" + name 
				+ ", profileUrl=" + profileUrl + '}';
	}	
	
}
