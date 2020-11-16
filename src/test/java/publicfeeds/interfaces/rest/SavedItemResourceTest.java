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
package publicfeeds.interfaces.rest;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import publicfeeds.application.Service;
import publicfeeds.application.internal.SpringBootAppConfig;
import publicfeeds.domain.Author;
import publicfeeds.domain.Item;
import publicfeeds.domain.Media;
import publicfeeds.interfaces.JwtFilter;

/**
 *
 * @author io
 */
@ContextConfiguration(classes = SpringBootAppConfig.class)
@WebMvcTest(controllers = SavedItemResource.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
						classes = JwtFilter.class))
public class SavedItemResourceTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean private Service service;
	

	private final String NON_EXISTING_STRING = "non existing";
	private final long NON_EXISTING_LONG = 987654L;

	private final String AUTHOR_ID_1 = "author7890";
	private final String ITEM_ID_1 = "1234";

	private final Item ITEM_1 = new Item(ITEM_ID_1, "Title", "urlszzz",
			new Media("media url", "titles", Collections.EMPTY_LIST),
			Instant.EPOCH,
			"insert html here",
			Instant.EPOCH.plusSeconds(86000),
			new Author(AUTHOR_ID_1, "Mr.Anderson"));

	private final String AUTHOR_ID_2 = "buggy";
	private final String ITEM_ID_2 = "5678";

	private final Item ITEM_2 = new Item(ITEM_ID_2, "no more", "pizzaz",
			new Media("media url", "nomore", Collections.EMPTY_LIST),
			Instant.EPOCH,
			"insert html here",
			Instant.EPOCH.plusSeconds(86000),
			new Author(AUTHOR_ID_2, "HOMER"));
	
	@BeforeEach
	public void init() {
		given(service.getItemById(ITEM_ID_1)).willReturn(Optional.of(ITEM_1));
		given(service.getItemById(ITEM_ID_2)).willReturn(Optional.of(ITEM_2));
		given(service.getItemById(NON_EXISTING_STRING)).willReturn(Optional.empty());
	}
	
	
	@Test
	public void getByIdTest() throws Exception {
		mvc.perform(get("/saved/{id}", ITEM_ID_2))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				
				.andExpect(jsonPath("$.id", is(ITEM_ID_2)))
				.andExpect(jsonPath("$.title", is(ITEM_2.getTitle())))
				.andExpect(jsonPath("$.link", is(ITEM_2.getLink())))
				.andExpect(jsonPath("$.media.contentUrl", is(ITEM_2.getMedia().getContentUrl())))
				.andExpect(jsonPath("$.media.tags.length()", is(ITEM_2.getMedia().getTags().size())))
				.andExpect(jsonPath("$.takenDate", is(ITEM_2.getTakenDate().toString())))
				.andExpect(jsonPath("$.htmlDescription", is(ITEM_2.getHtmlDescription())))
				.andExpect(jsonPath("$.publishedDate", is(ITEM_2.getPublishedDate().toString())))
				.andExpect(jsonPath("$.author.id", is(ITEM_2.getAuthor().getId())))
				.andExpect(jsonPath("$.author.name", is(ITEM_2.getAuthor().getName())));
	}
	
	@Test
	public void getByIdTest_NotFound() throws Exception {
		mvc.perform(get("/saved/{id}", NON_EXISTING_STRING))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$", anEmptyMap()));
	}
	
	
	@Test
	public void getAllTest() throws Exception {
		given(service.getAllItems()).willReturn(Arrays.asList(ITEM_1, ITEM_2));
		
		mvc.perform(get("/saved"))
				
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				
				.andExpect(jsonPath("$", hasSize(2)))
				
				.andExpect(jsonPath("$[0].id", is(ITEM_ID_1)))
				.andExpect(jsonPath("$[0].title", is(ITEM_1.getTitle())))
				.andExpect(jsonPath("$[0].link", is(ITEM_1.getLink())))
				.andExpect(jsonPath("$[0].media.contentUrl", is(ITEM_1.getMedia().getContentUrl())))
				.andExpect(jsonPath("$[0].media.tags.length()", is(ITEM_1.getMedia().getTags().size())))
				.andExpect(jsonPath("$[0].takenDate", is(ITEM_1.getTakenDate().toString())))
				.andExpect(jsonPath("$[0].htmlDescription", is(ITEM_1.getHtmlDescription())))
				.andExpect(jsonPath("$[0].publishedDate", is(ITEM_1.getPublishedDate().toString())))
				.andExpect(jsonPath("$[0].author.id", is(ITEM_1.getAuthor().getId())))
				.andExpect(jsonPath("$[0].author.name", is(ITEM_1.getAuthor().getName())))
				
				.andExpect(jsonPath("$[1].id", is(ITEM_ID_2)))
				.andExpect(jsonPath("$[1].title", is(ITEM_2.getTitle())))
				.andExpect(jsonPath("$[1].link", is(ITEM_2.getLink())))
				.andExpect(jsonPath("$[1].media.contentUrl", is(ITEM_2.getMedia().getContentUrl())))
				.andExpect(jsonPath("$[1].media.tags.length()", is(ITEM_2.getMedia().getTags().size())))
				.andExpect(jsonPath("$[1].takenDate", is(ITEM_2.getTakenDate().toString())))
				.andExpect(jsonPath("$[1].htmlDescription", is(ITEM_2.getHtmlDescription())))
				.andExpect(jsonPath("$[1].publishedDate", is(ITEM_2.getPublishedDate().toString())))
				.andExpect(jsonPath("$[1].author.id", is(ITEM_2.getAuthor().getId())))
				.andExpect(jsonPath("$[1].author.name", is(ITEM_2.getAuthor().getName())));
	}
	
	@Test
	public void getAllTest_NotFound() throws Exception {
		given(service.getAllItems()).willReturn(Collections.emptyList());
		
		mvc.perform(get("/saved"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$", empty()));
	}
	
	
	@Test
	public void countTest() throws Exception {
		given(service.countItems()).willReturn(10L);
		
		mvc.perform(get("/saved/count"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.count", is(10)));
	}
	
	
	@Test
	public void clearTest() throws Exception {
		deleteAllTest();
	}
	
	
	@Test
	public void deleteByIdTest() throws Exception {
		given(service.deleteItemByIds(Arrays.asList(ITEM_ID_1))).willReturn(true);
		
		mvc.perform(delete("/saved/{id}", ITEM_ID_1))
				.andExpect(status().isOk());
	}
	
	
	@Test
	public void deleteAllTest() throws Exception {
		given(service.deleteAllItems()).willReturn(true);
		
		mvc.perform(delete("/saved"))
				.andExpect(status().isOk());
	}
	
}
