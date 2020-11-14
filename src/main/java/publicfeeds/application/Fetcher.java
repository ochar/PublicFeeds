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
 *
 * @author io
 */
public interface Fetcher {

	List<Item> fetchPlain();
	
	List<Item> fetchWithQuery(String queryString);

	List<Item> fetchWithIds(List<String> ids);

	List<Item> fetchWithTags(List<String> tags, boolean anyTags);

	PublicFeedRespJson parsePlainJson(String jsonString);

	PublicFeedRespJson parseWrapperJson(String respString);
	
}
