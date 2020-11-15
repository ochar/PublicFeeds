/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.interfaces.rest;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import publicfeeds.application.Fetcher;
import publicfeeds.application.Service;
import publicfeeds.domain.Item;
import publicfeeds.interfaces.rest.support.PageResponse;
import publicfeeds.interfaces.support.Page;

/**
 * Rest endpoints serving {@link Item} type object from live public feed.
 *
 * @author io
 */
@RequestMapping("/feed")
@RestController
public class FeedResource {
	
	@Autowired
	private Fetcher FETCHER;
	
	@Autowired 
	private Service service;
	
	@Autowired
	private ItemCache itemCache;
	
	
	/**
	 * Get {@link Item} from live public feed. Results will be wrapped in a page
	 * object and can be specified further with a search string and date range.
	 * 
	 * Resulted items will be cache on server, so that it can be saved later.
	 *
	 * @param queryString a query parameter, search string for the result items,
	 * will search in media tags, title, link, author username and id, can be null
	 * @param minDate a query parameter, minimum date of result items, can be null
	 * @param maxDate a query parameter, minimum date of result items, can be null
	 * @param pageSize a query parameter, page size of the results
	 * @param pageNumber a query parameter, selected page number of the results
	 * @return {@link ResponseEntity} with status 200 OK and containing list of 
	 * item from public feeds wrapped in a page object
	 */
	@GetMapping("/current")
	public ResponseEntity current(
			@RequestParam(name = "q", required = false) String queryString,
			
			@RequestParam(name = "minDate", required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minDate,
			
			@RequestParam(name = "maxDate", required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxDate,
			
			@RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber) {
		
		List<Item> items = FETCHER.fetchPlain();
		
		items = processSearch(items, queryString, minDate, maxDate);
		
		itemCache.saveAll(items);

		return new ResponseEntity<>(processPaging(items, pageSize, pageNumber), HttpStatus.OK);
	}
	
	/**
	 * Save all {@link Item} from live public feeds. If the storage already contains an 
	 * item with same id, it is replaced.
	 *
	 * @return list of saved {@code Item} if operation is successful
	 */
	@GetMapping("/current/save")
	public List<Item> saveCurrentFeed() {
		List<Item> items = FETCHER.fetchPlain();
		service.saveItems(items);
		return items;
	}
	
	/**
	 * Save all {@link Item} from previously accessed public feeds. 
	 * If the storage already contains an item with same id, it is replaced.
	 * 
	 * @param itemIds a query parameter, id list of items to be saved
	 * @return {@link ResponseEntity} with status 201 CREATED and containing a
	 * {@code Map} of item ids and status of save operation. If the save
	 * operation is successful for that id, entry value is url for saved
	 * {@link Item}, otherwise if the item was not found on server cache, entry
	 * value is String "NOT FOUND"
	 */
	@GetMapping("/save")
	public ResponseEntity saveFeeds(@RequestParam(name = "itemIds") List<String> itemIds) {
		
		Map<String, String> resultMap = new HashMap<>(itemIds.size());
		
		for (String id : itemIds) {
			Item cachedItem = itemCache.get(id);
			if (cachedItem != null) {
				service.saveItem(cachedItem);
				String uriString = MvcUriComponentsBuilder
						.fromController(SavedItemResource.class)
						.pathSegment(id).toUriString();
				resultMap.put(id, uriString);
				
			} else {
				resultMap.put(id, "NOT FOUND");
			}
		}
		return new ResponseEntity(resultMap, HttpStatus.CREATED);
	}
	
	/**
	 * Get {@link Item} from live public feed which has given tags.
	 * Results will be wrapped in a page object and can be specified further 
	 * with a search string and date range.
	 * 
	 * Resulted items will be cache on server, so that it can be saved later.
	 *
	 * @param tags a query parameter, list of tags which feed items to be retrieved
	 * @param anyTags a query parameter, whether result items must match all the
	 * tags or just any of them
	 * @param queryString a query parameter, search string for the result items,
	 * will search in media tags, title, link, author username and id, can be null
	 * @param minDate a query parameter, minimum date of result items, can be null
	 * @param maxDate a query parameter, minimum date of result items, can be null
	 * @param pageSize a query parameter, page size of the results
	 * @param pageNumber a query parameter, selected page number of the results
	 * @return {@link ResponseEntity} with status 200 OK and containing list of 
	 * item from public feeds wrapped in a page object
	 */
	@GetMapping("/query/tags")
	public ResponseEntity queryTags(
			@RequestParam(name = "tags") List<String> tags,
			@RequestParam(name = "anyTags", defaultValue = "false") boolean anyTags,
			
			@RequestParam(name = "q", required = false) String queryString,
			
			@RequestParam(name = "minDate", required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minDate,
			
			@RequestParam(name = "maxDate", required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxDate,
			
			@RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber) {
		
		List<Item> items = FETCHER.fetchWithTags(tags, anyTags);
		
		itemCache.saveAll(items);
		
		items = processSearch(items, queryString, minDate, maxDate);
		
		return new ResponseEntity<>(processPaging(items, pageSize, pageNumber), HttpStatus.OK);
	}
	
	/**
	 * Save all {@link Item} from live public feeds which has given tags. 
	 * If the storage already contains an item with same id, it is replaced.
	 *
	 * @param tags a query parameter, list of tags which feed items to be retrieved
	 * @param anyTags a query parameter, whether result items must match all the
	 * tags or just any of them
	 * @return list of saved {@code Item} if operation is successful
	 */
	@GetMapping("/query/tags/save")
	public List<Item> saveQueryTags(
			@RequestParam(name = "tags") List<String> tags,
			@RequestParam(name = "anyTags", defaultValue = "false") boolean anyTags) {
		List<Item> items = FETCHER.fetchWithTags(tags, anyTags);
		service.saveItems(items);
		return items;
	}
	
	/**
	 * Get {@link Item} from live public feed from given authors.
	 * Results will be wrapped in a page object and can be specified further 
	 * with a search string and date range.
	 * 
	 * Resulted items will be cache on server, so that it can be saved later.
	 *
	 * @param ids a query parameter, list of author ids which feed items to be 
	 * retrieved
	 * @param queryString a query parameter, search string for the result items,
	 * will search in media tags, title, link, author username and id, can be null
	 * @param minDate a query parameter, minimum date of result items, can be null
	 * @param maxDate a query parameter, minimum date of result items, can be null
	 * @param pageSize a query parameter, page size of the results
	 * @param pageNumber a query parameter, selected page number of the results
	 * @return {@link ResponseEntity} with status 200 OK and containing list of 
	 * item from public feeds wrapped in a page object
	 */
	@GetMapping("/query/ids")
	public ResponseEntity queryIds(
			@RequestParam(name = "ids") List<String> ids,
			
			@RequestParam(name = "q", required = false) String queryString,
			
			@RequestParam(name = "minDate", required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minDate,
			
			@RequestParam(name = "maxDate", required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxDate,
			
			@RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber)
			throws IOException {

		List<Item> items = FETCHER.fetchWithIds(ids);

		items = processSearch(items, queryString, minDate, maxDate);

		return new ResponseEntity<>(processPaging(items, pageSize, pageNumber), HttpStatus.OK);
	}
	
	/**
	 * Save all {@link Item} from live public feeds from given authors. 
	 * If the storage already contains an item with same id, it is replaced.
	 *
	 * @param ids a query parameter, list of author ids which feed items to be 
	 * retrieved
	 * @return list of saved {@code Item} if operation is successful
	 */
	@GetMapping("/query/ids/save")
	public List<Item> saveQueryIds(@RequestParam(name = "ids") List<String> ids) {
		List<Item> items = FETCHER.fetchWithIds(ids);
		service.saveItems(items);
		return items;
	}
	
	
	/**
	 * Slice and wrap a list of items into a page object.
	 * 
	 * @param <T> type of object contained in the item collection
	 * @param items collection of items to converted to a page object
	 * @param pageSize number of items in a page
	 * @param pageNumber number of current selected page
	 * @return a page object {@link PageResponse} containing part of the input 
	 * collection, information of the page, and urls to previous and next page
	 */
	private <T> PageResponse<T> processPaging(List<T> items, int pageSize, int pageNumber) {
		int startPos = Page.calcStartPosition(pageSize, pageNumber, items.size());
		int totalPage = Page.calcTotalPage(pageSize, items.size());
		
		List<T> pageItems = items.stream().sequential()
				.skip(startPos)
				.limit(pageSize)
				.collect(toList());
		
		ServletUriComponentsBuilder currentReqUriBuilder = ServletUriComponentsBuilder.fromCurrentRequest();

		String currentUrl = currentReqUriBuilder.cloneBuilder().toUriString();

		String prevUrl = (pageNumber > 1)
				? currentReqUriBuilder.cloneBuilder().replaceQueryParam("pageNumber", pageNumber - 1).toUriString() : null;

		String nextUrl = (pageNumber < totalPage)
				? currentReqUriBuilder.cloneBuilder().replaceQueryParam("pageNumber", pageNumber + 1).toUriString() : null;

		return new PageResponse<>(
				pageItems,
				pageSize, pageNumber,
				totalPage, items.size(),
				currentUrl,
				prevUrl,
				nextUrl);
	}
	
	
	/**
	 * Returns sub list of a list of {@link Item} matching query string and date
	 * range.
	 * 
	 * @param items list of {@code Item} to be searched
	 * @param queryString search string for the result items, will search in 
	 * media tags, title, link, author username and id, can be null
	 * @param minDate minimum date of result items, can be null
	 * @param maxDate minimum date of result items, can be null
	 * @return new list containing {@code Item} matching the search
	 */
	public List<Item> processSearch(List<Item> items, String queryString, 
			LocalDate minDate, LocalDate maxDate) {
		List<Item> results = items;
		
		if (minDate != null || maxDate != null) {
			Instant minIns = (minDate != null)
					? minDate.atStartOfDay().atZone(ZoneId.of("+7")).toInstant() : null;

			Instant maxIns = (maxDate != null)
					? maxDate.atTime(LocalTime.MAX).atZone(ZoneId.of("+7")).toInstant() : null;

			results = searchItemsByDate(results, minIns, maxIns);
		}

		if (queryString != null) {
			results = searchItems(results, queryString);
		}
		
		return results;
	}
	
	/**
	 * Returns sub list of a list of {@link Item} matching date range.
	 * 
	 * @param items list of {@code Item} to be searched
	 * @param minDate minimum date of result items, can be null
	 * @param maxDate minimum date of result items, can be null
	 * @return new list containing {@code Item} matching the search
	 */
	public List<Item> searchItemsByDate(List<Item> items, Instant minDate, Instant maxDate) {
		if (minDate == null && maxDate == null) {
			return items;
		}
		if (minDate != null && maxDate != null && minDate.isAfter(maxDate)) {
			return new ArrayList<>();
		}
		return items.stream()
				.filter(item -> {
					boolean result = true;
					if (minDate != null) {
						result &= (item.getTakenDate().isBefore(minDate) == false)
								|| (item.getPublishedDate().isBefore(minDate) == false);
					}
					if (maxDate != null) {
						result &= (item.getTakenDate().isAfter(maxDate) == false)
								|| (item.getPublishedDate().isAfter(maxDate) == false);
					}
					return result;
				})
				.collect(toList());
	}
	
	/**
	 * Returns sub list of a list of {@link Item} matching query string.
	 * 
	 * @param items list of {@code Item} to be searched
	 * @param queryString search string for the result items, will search in 
	 * media tags, title, link, author username and id, can be null
	 * @return new list containing {@code Item} matching the search
	 */
	public List<Item> searchItems(List<Item> items, String queryString) {
		if (queryString == null || queryString.trim().isEmpty()) {
			return items;
		}
		
		final String lowerQueryStr = queryString.toLowerCase();
		
		return items.stream()
				.filter(item -> {
					return item.getMedia().getTags()
							.stream()
							.filter(tag -> tag.equalsIgnoreCase(lowerQueryStr))
							.findAny()
							.isPresent()
							|| item.getTitle().toLowerCase().contains(lowerQueryStr)
							|| item.getLink().toLowerCase().contains(lowerQueryStr)
							|| item.getAuthor().getName().toLowerCase().contains(lowerQueryStr)
							|| item.getAuthor().getId().contains(queryString);
				})
				.collect(toList());
	}
	
}
