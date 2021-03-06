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
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Author of a feed Item
 *
 * @author io
 */
@Entity
public class Author implements Serializable {
	
	/**
	 * Base URL for the profile page of authors
	 */
	public static final String profileBaseUrl = "https://www.flickr.com/people/";
	
	@Id
	private String id;
	
	private String name;
	
	private String profileUrl = "";

	protected Author() {
	}
	
	/**
	 * Create new Author with id, name, and empty profile url.
	 * 
	 * @param id author id
	 * @param name author username
	 */
	public Author(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Create new Author with id, name, and profile url.
	 * 
	 * @param id author id
	 * @param name author username
	 * @param profileUrl url to the profile page of the author
	 */
	public Author(String id, String name, String profileUrl) {
		this.id = id;
		this.name = name;
		this.profileUrl = profileUrl;
	}

	/**
	 * Returns user id of this author.
	 *
	 * @return user id of this author
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the user id of this author.
	 *
	 * @param id the user id to be set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns username of this author.
	 *
	 * @return username of this author.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the username of this author.
	 *
	 * @param name the username to be set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns profile url of this author.
	 *
	 * @return profile url of this author.
	 */
	public String getProfileUrl() {
		return profileUrl;
	}

	/**
	 * Sets the profile url of this author.
	 *
	 * @param profileUrl the profile url to be set.
	 */
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
