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
package publicfeeds.application.internal;

import publicfeeds.application.Fetcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import publicfeeds.application.dto.json.PublicFeedRespJson;
import publicfeeds.domain.Item;

/**
 * Fetcher implementation using HttpURLConnection.
 * 
 * Resulted DTO response will be cached. If the resource fetched is not modified, 
 * result from cache will be returned.
 * Response string is parse to DTO using Jackson ObjectMapper.
 *
 * @author io
 */
@ApplicationScope
@Component
public class FetcherHULImpl implements Fetcher {
	
	@Value("${appDebug:false}")
	private boolean DEBUG = false;
	
	private static final String ATOM_URL = "https://api.flickr.com/services/feeds/photos_public.gne";
	private static final String RSS20_URL = ATOM_URL + "?format=rss2";
	private static final String JSON_URL = ATOM_URL + "?format=json";
	
	/**
	 * Default feeds url to be fetched.
	 */
	private static final String DEFAULT_URL = JSON_URL;
	
	
	private PublicFeedRespJson plainRespCache;
	private List<Item> plainItemsCache;
	
	@Override
	public List<Item> fetchPlain() {
		PublicFeedRespJson resp = null;
		List<Item> resultItems = null;
		
		Optional<PublicFeedRespJson> optResp = fetchUrl(DEFAULT_URL);
		if (optResp.isPresent()) {
			resp = optResp.get();
			resultItems = resp.getItems()
					.stream()
					.map(itemJson -> itemJson.toItem())
					.collect(toList());
			
			plainRespCache = resp;
			plainItemsCache = resultItems;
			
		} else {
			resp = plainRespCache;
			resultItems = plainItemsCache;
		}
		
		if (DEBUG) {
			System.out.println(DEFAULT_URL);
			System.out.println("json feed last modified: "+resp.getModified());
		}
		
		return resultItems;
	}
	
	
	private ConcurrentHashMap<String, PublicFeedRespJson> respCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, List<Item>> itemsCache = new ConcurrentHashMap<>();
	
	private String appendUrlQuery(String urlString, String queryString) {
		if (urlString.contains("?")) {
			return urlString + "&" + queryString;
		} else {
			return urlString + "?" + queryString;
		}
	}
	
	@Override
	public List<Item> fetchWithQuery(String queryString) {
		String reqUrl = appendUrlQuery(DEFAULT_URL, queryString);
		
		PublicFeedRespJson resp = null;
		List<Item> resultItems = null;

		Optional<PublicFeedRespJson> optResp = fetchUrl(reqUrl);
		if (optResp.isPresent()) {
			resp = optResp.get();
			resultItems = resp.getItems()
					.stream()
					.map(itemJson -> itemJson.toItem())
					.collect(toList());
			
			respCache.put(reqUrl, resp);
			itemsCache.put(reqUrl, resultItems);
			
		} else {
			resp = respCache.get(reqUrl);
			resultItems = itemsCache.get(reqUrl);
		}

		if (DEBUG) {
			System.out.println(reqUrl);
			System.out.println("json feed last modified: " + resp.getModified());
		}

		return resultItems;
	}
	
	
	private static final String PARAM_TAGS = "tags";
	private static final String PARAM_TAG_MODE = "tagmode";
	
	private static final String TAG_MODE_ALL = "all";
	private static final String TAG_MODE_ANY = "any";
	
	@Override
	public List<Item> fetchWithTags(List<String> tags, boolean anyTags) {
		String queryString = "";
		
		String encodedTagsParam = encodeParamValues(tags);
		String tagMode = anyTags ? TAG_MODE_ANY : TAG_MODE_ALL;
		
		queryString += PARAM_TAGS + "=" + encodedTagsParam;
		queryString += "&" + PARAM_TAG_MODE + "=" + tagMode;
		
		return fetchWithQuery(queryString);
	}
	
	
	private static final String PARAM_IDS = "ids";
	
	@Override
	public List<Item> fetchWithIds(List<String> ids) {
		String queryString = "";
		
		String encodedIdsParam = encodeParamValues(ids);
		
		queryString += PARAM_IDS + "=" + encodedIdsParam;
		
		return fetchWithQuery(queryString);
	}
	
	/**
	 * Encode list of HTTP query parameters and joining them.
	 * 
	 * @param paramVals list of parameters to be encoded
	 * @return string of encoded and joined parameters
	 */
	private String encodeParamValues(List<String> paramVals) {
		return paramVals.stream()
				.map(tag -> {
					try {
						return URLEncoder.encode(tag, "UTF-8");
					} catch (UnsupportedEncodingException ex) {
						return tag;
					}
				})
				.collect(joining(","));
	}
	
	/**
	 * Fetching public feed from url using HttpURLConnection.
	 * Will store last access time, so that next fetch can use 
	 * If-Modified-Since header.
	 * 
	 * @param urlString Url of public feed to be fetched
	 * @return An Optional of PublicFeedRespJson DTO. It is empty if fetched url
	 * is not modified
	 */
	private Optional<PublicFeedRespJson> fetchUrl(String urlString) {
		try {
			Instant lastFetch = getUrlLastFetch(urlString);
			
			URL url = URI.create(urlString).toURL();
			URLConnection conn = url.openConnection();
			
			if (lastFetch != null) {
				conn.setIfModifiedSince(lastFetch.toEpochMilli());
			}
			conn.connect();
			
			setUrlLastFetch(urlString);

			String respString = inputStreamToString(conn.getInputStream());
			
			if (DEBUG) {
				HttpURLConnection httpConn = (HttpURLConnection)conn;
				System.out.println("Response body length: "+respString.length());
				System.out.println("Response status: "+httpConn.getResponseCode()+" "+httpConn.getResponseMessage());
			}
			
			return Optional.ofNullable((respString.length() >= 1) ? parseWrapperJson(respString) : null);

		} catch (IOException ex) {
			Logger.getLogger(FetcherHULImpl.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
	
	/**
	 * Stores last access time of an url.
	 */
	private ConcurrentHashMap<String, Instant> urlLastFetch = new ConcurrentHashMap<>();
	
	private void setUrlLastFetch(String urlString) {
		Objects.requireNonNull(urlString, "Parameter urlString must not be null");
		urlLastFetch.put(urlString, Instant.now());
	}
	
	private Instant getUrlLastFetch(String urlString) {
		Objects.requireNonNull(urlString, "Parameter urlString must not be null");
		return urlLastFetch.get(urlString);
	}
	
	/**
	 * Read an InputStream to a String.
	 * 
	 * @param is InputStream to be read.
	 * @return A String from contents of the InputStream
	 * @throws IOException 
	 */
	private String inputStreamToString(InputStream is) throws IOException {
		try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);) {
		
			char[] buffer = new char[1024];
			int nRead;

			StringBuilder sb = new StringBuilder();		

			while ((nRead = reader.read(buffer, 0, buffer.length)) > 0) {
				sb.append(buffer, 0, nRead);
			}

			return sb.toString();
		}
	}
	
	@Override
	public PublicFeedRespJson parseWrapperJson(String respString) {
		// remove wrapper String
		respString = respString.substring(15, respString.length());
		
		return parsePlainJson(respString);
	}
	
	@Override
	public PublicFeedRespJson parsePlainJson(String jsonString) {
		try {
			PublicFeedRespJson resp = mapper.readValue(jsonString, PublicFeedRespJson.class);
			if (DEBUG) {
				resp.getItems().forEach(System.out::println);
			}
			return resp;

		} catch (JsonProcessingException ex) {
			Logger.getLogger(FetcherHULImpl.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
	
	
	/**
	 * Jackson ObjectMapper json parser.
	 */
	private static final ObjectMapper mapper;
	
	static {
		mapper = new ObjectMapper();
		
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
}
