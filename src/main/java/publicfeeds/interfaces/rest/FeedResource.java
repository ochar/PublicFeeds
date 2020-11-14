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
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import publicfeeds.application.Fetcher;
import publicfeeds.domain.Item;
import publicfeeds.interfaces.rest.support.PageResponse;
import publicfeeds.interfaces.support.Page;

/**
 *
 * @author io
 */
@RequestMapping("/feed")
@RestController
public class FeedResource {
	
	@Autowired
	private Fetcher FETCHER;
	
	
	
	@GetMapping("/current")
	public ResponseEntity current(
			@RequestParam(name = "q", required = false) String queryString,
			
			@RequestParam(name = "minDate", required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minDate,
			
			@RequestParam(name = "maxDate", required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxDate,
			
			@RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber) 
			throws IOException {
		
		List<Item> items = FETCHER.fetchPlain();
		
		items = processSearch(items, queryString, minDate, maxDate);

		return new ResponseEntity<>(processPaging(items, pageSize, pageNumber), HttpStatus.OK);
	}
	
	@GetMapping("/query/tags")
	public ResponseEntity query(
			@RequestParam(name = "tags") List<String> tags,
			@RequestParam(name = "anyTags", defaultValue = "false") boolean anyTags,
			
			@RequestParam(name = "q", required = false) String queryString,
			
			@RequestParam(name = "minDate", required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minDate,
			
			@RequestParam(name = "maxDate", required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxDate,
			
			@RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber) 
			throws IOException {
		
		List<Item> items = FETCHER.fetchWithTags(tags, anyTags);
		
		items = processSearch(items, queryString, minDate, maxDate);
		
		return new ResponseEntity<>(processPaging(items, pageSize, pageNumber), HttpStatus.OK);
	}
	
	
	
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
