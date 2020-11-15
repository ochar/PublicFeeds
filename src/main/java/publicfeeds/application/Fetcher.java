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
package publicfeeds.application;

import java.util.List;
import publicfeeds.application.dto.json.PublicFeedRespJson;
import publicfeeds.domain.Item;

/**
 * An object for accessing the public feed
 *
 * @author io
 */
public interface Fetcher {

	/**
	 * Fetch the public feeds and parse the response to Item objects.
	 * 
	 * @return List of parsed feed items.
	 */
	List<Item> fetchPlain();
	
	/**
	 * Fetch the public feeds with a query string.
	 * 
	 * @param queryString Query string to be used in the fetch url
	 * @return List of parsed feed items matching the query.
	 */
	List<Item> fetchWithQuery(String queryString);

	/**
	 * Fetch the public feeds with a query string of author ids.
	 * 
	 * @param ids List of author ids to be queried to the public feed
	 * @return List of parsed feed items which author matching one of the ids
	 */
	List<Item> fetchWithIds(List<String> ids);

	/**
	 * Fetch the public feeds with a query string of media tags.
	 *
	 * @param tags List of tags to be queried to the public feed
	 * @param anyTags Whether feed must match all of the tags or can be any of them
	 * @return List of parsed feed items which author matching the tags
	 */
	List<Item> fetchWithTags(List<String> tags, boolean anyTags);

	/**
	 * Parse a feed response string with content-type json to PublicFeedRespJson
	 * DTO. This method parse the response with just json content; that is 
	 * already stripped of any wrapper.
	 *
	 * @param jsonString the json response string to be parsed
	 * @return PublicFeedRespJson DTO from the json response string
	 */
	PublicFeedRespJson parsePlainJson(String jsonString);

	/**
	 * Parse a feed response string with content-type json to PublicFeedRespJson
	 * DTO. This method parse the raw response which still has wrapper.
	 *
	 * @param respString the raw json response string to be parsed
	 * @return PublicFeedRespJson DTO from the json response string
	 */
	PublicFeedRespJson parseWrapperJson(String respString);
	
}
