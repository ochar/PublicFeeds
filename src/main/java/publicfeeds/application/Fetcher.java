/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
