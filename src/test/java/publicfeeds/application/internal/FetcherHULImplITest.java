/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.application.internal;

import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import publicfeeds.application.Fetcher;
import publicfeeds.domain.Item;

/**
 *
 * @author io
 */
@SpringBootTest
public class FetcherHULImplITest {
	
	@Autowired
	private Fetcher fetcher;
	
	
	@Test
	public void fetchPlainTest() {
		List<Item> results = fetcher.fetchPlain();
		
		assertThat(results).hasSizeGreaterThanOrEqualTo(20);
	}
	
	@Test
	public void fetchWithQueryTest() {
		
	}
	
	@Test
	public void fetchWithIdsTest() {
		List<Item> results = fetcher.fetchWithIds(
				Arrays.asList("191014191@N04", "147432645@N07"));
		
		assertThat(results).hasSizeGreaterThanOrEqualTo(20)
				.allSatisfy(item -> 
						assertThat(item.getAuthor().getId())
								.satisfiesAnyOf(
										id -> assertThat(id).isEqualTo("191014191@N04"), 
										id -> assertThat(id).isEqualTo("147432645@N07")));
	}
	
	@Test
	public void fetchWithTagsTest() {
		List<Item> results = fetcher.fetchWithTags(
				Arrays.asList("fuji", "japan"), false);
		
		assertThat(results).hasSizeGreaterThanOrEqualTo(20)
				.allMatch(item -> item.getMedia().getTags().stream()
						.filter(tag -> "fuji".equals(tag) || "japan".equals(tag))
						.findAny().isPresent());
	}
	
	@Test
	public void parsePlainJsonTest() {
		
	}
	
	@Test
	public void parseWrapperJsonTest() {
		
	}
	
}
