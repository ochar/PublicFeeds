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
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import publicfeeds.application.Service;
import publicfeeds.application.internal.SpringBootAppConfig;
import publicfeeds.domain.Author;
import publicfeeds.domain.Item;
import publicfeeds.domain.ItemLike;
import publicfeeds.domain.Media;
import publicfeeds.interfaces.JwtFilter;

/**
 *
 * @author io
 */
@ContextConfiguration(classes = SpringBootAppConfig.class)
@WebMvcTest(controllers = LikeResource.class,
		excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, 
						classes = JwtFilter.class))
public class LikeResourceTest {
	
	@Autowired
	private MockMvc mvc;

	@MockBean
	private Service service;
	
	
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

	private final String USERNAME_1 = "Agus";
	private final String USERNAME_2 = "Sapto";
	private final String USERNAME_3 = "Wisnu";
	
	private final ItemLike LIKE_1 = new ItemLike(ITEM_1, USERNAME_1);
	private final ItemLike LIKE_2 = new ItemLike(ITEM_2, USERNAME_1);
	private final ItemLike LIKE_3 = new ItemLike(ITEM_1, USERNAME_2);
	private final ItemLike LIKE_4 = new ItemLike(ITEM_2, USERNAME_2);
	private final ItemLike LIKE_5 = new ItemLike(ITEM_1, USERNAME_3);
	private final ItemLike LIKE_6 = new ItemLike(ITEM_2, USERNAME_3);
	
	@BeforeEach
	public void init() {
		given(service.getItemById(ITEM_ID_1)).willReturn(Optional.of(ITEM_1));
		given(service.getItemById(ITEM_ID_2)).willReturn(Optional.of(ITEM_2));
		
		given(service.getLikesByItemId(ITEM_ID_1)).willReturn(Arrays.asList(LIKE_1, LIKE_3, LIKE_5));
		given(service.getLikesByItemId(ITEM_ID_2)).willReturn(Arrays.asList(LIKE_2, LIKE_4, LIKE_6));
		given(service.getLikesByItemId(NON_EXISTING_STRING)).willReturn(Collections.emptyList());
		
		given(service.getLikesByUsername(USERNAME_1)).willReturn(Arrays.asList(LIKE_1, LIKE_2));
		given(service.getLikesByUsername(USERNAME_2)).willReturn(Arrays.asList(LIKE_3, LIKE_4));
		given(service.getLikesByUsername(USERNAME_3)).willReturn(Arrays.asList(LIKE_5, LIKE_6));
		given(service.getCommentsByUsername(NON_EXISTING_STRING)).willReturn(Collections.emptyList());
	}
	

	@Test
	public void getFromItemIdTest() throws Exception {
		mvc.perform(get("/like/item?itemId={itemId}", ITEM_ID_1))
				
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				
				.andExpect(jsonPath("$", hasSize(3)))
				
				.andExpect(jsonPath("$[0].itemId", is(ITEM_ID_1)))
				.andExpect(jsonPath("$[0].username", is(USERNAME_1)));
	}
	
	@Test
	public void getFromItemIdTest_NotFound() throws Exception {
		mvc.perform(get("/like/item?itemId={itemId}", NON_EXISTING_STRING))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$", anEmptyMap()));
	}
	
	@Test
	public void getFromItemIdTest_MissingParam() throws Exception {
		mvc.perform(get("/like/item"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", is("400")))
				.andExpect(jsonPath("$.reason", not(emptyString())));
	}

	
	@Test
	public void getFromUserTest() throws Exception {
		mvc.perform(get("/like/user?username={0}", USERNAME_1))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				
				.andExpect(jsonPath("$", hasSize(2)))
				
				.andExpect(jsonPath("$[0].itemId", is(ITEM_ID_1)))
				.andExpect(jsonPath("$[0].username", is(USERNAME_1)))
				
				.andExpect(jsonPath("$[1].itemId", is(ITEM_ID_2)))
				.andExpect(jsonPath("$[1].username", is(USERNAME_1)));
	}

	@Test
	public void getFromUserTest_NotFound() throws Exception {
		mvc.perform(get("/like/user?username={0}", NON_EXISTING_STRING))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$", empty()));
	}

	@Test
	public void getFromUserTest_MissingParam() throws Exception {
		mvc.perform(get("/like/user"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", is("400")))
				.andExpect(jsonPath("$.reason", not(emptyString())));
	}
	

	@Test
	public void likeTest() throws Exception {
		final long LIKE_1_ID = 9876L;

		doAnswer((Answer<ItemLike>) invocation -> {
			ItemLike like = new ItemLike(LIKE_1.getItem(), LIKE_1.getUsername());
			like.setId(LIKE_1_ID);
			return like;
		}).when(service).likeAnItem(eq(LIKE_1.getItem().getId()), eq(LIKE_1.getUsername()));

		mvc.perform(put("/like?itemId={0}&username={1}", 
				LIKE_1.getItem().getId(), LIKE_1.getUsername()))
				
				.andExpect(status().isCreated())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is((int)LIKE_1_ID)))
				.andExpect(jsonPath("$.itemId", is(LIKE_1.getItem().getId())))
				.andExpect(jsonPath("$.username", is(LIKE_1.getUsername())));
	}

	@Test
	public void likeTest_NotFound() throws Exception {
		given(service.likeAnItem(NON_EXISTING_STRING, USERNAME_1)).willReturn(null);

		mvc.perform(put("/like?itemId={0}&username={1}", NON_EXISTING_STRING, USERNAME_1))
				.andExpect(status().isNotFound());
	}

	@Test
	public void likeTest_MissingParam() throws Exception {
		mvc.perform(put("/like"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", is("400")))
				.andExpect(jsonPath("$.reason", not(emptyString())));
	}
	

	@Test
	public void unlikeTest() throws Exception {
		given(service.unlikeAnItem(LIKE_1.getItem().getId(), LIKE_1.getUsername()))
				.willReturn(true);
		
		mvc.perform(delete("/like?itemId={0}&username={1}", LIKE_1.getItem().getId(), 
				LIKE_1.getUsername()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$", is(true)));
	}
	
	@Test
	public void unlikeTest_NotFound() throws Exception {
		given(service.unlikeAnItem(NON_EXISTING_STRING, USERNAME_1)).willReturn(false);
		
		mvc.perform(delete("/like?itemId={0}&username={1}", NON_EXISTING_STRING, USERNAME_1))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void unlikeTest_MissingParam() throws Exception {
		mvc.perform(delete("/like"))
				.andExpect(status().is4xxClientError());
	}
	
}
