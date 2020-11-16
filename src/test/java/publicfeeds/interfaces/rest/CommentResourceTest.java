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
import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import org.mockito.stubbing.Answer;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import publicfeeds.application.Service;
import publicfeeds.application.internal.SpringBootAppConfig;
import publicfeeds.domain.Author;
import publicfeeds.domain.Item;
import publicfeeds.domain.ItemComment;
import publicfeeds.domain.Media;
import publicfeeds.interfaces.JwtFilter;

/**
 *
 * @author io
 */
@ContextConfiguration(classes = SpringBootAppConfig.class)
@WebMvcTest(controllers = CommentResource.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
						classes = JwtFilter.class))
public class CommentResourceTest {
	
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
	
	private final String COMM_1 = "Noice!";
	private final String COMM_2 = "bit blurry";
	private final String COMM_3 = "autofocus too quick!?!";

	private final ItemComment COMMENT_1 = new ItemComment(COMM_1, ITEM_1, USERNAME_1);
	private final ItemComment COMMENT_2 = new ItemComment(COMM_2, ITEM_2, USERNAME_1);
	private final ItemComment COMMENT_3 = new ItemComment(COMM_3, ITEM_2, USERNAME_1);
	
	@BeforeEach
	public void init() {
		given(service.getItemById(ITEM_ID_1)).willReturn(Optional.of(ITEM_1));
		given(service.getItemById(ITEM_ID_2)).willReturn(Optional.of(ITEM_2));
		
		given(service.getCommentsByItemId(ITEM_ID_1)).willReturn(Arrays.asList(COMMENT_1));
		given(service.getCommentsByItemId(ITEM_ID_2)).willReturn(Arrays.asList(COMMENT_2, COMMENT_3));
		given(service.getCommentsByItemId(NON_EXISTING_STRING)).willReturn(Collections.emptyList());
		
		given(service.getCommentsByUsername(USERNAME_1)).willReturn(Arrays.asList(COMMENT_1, COMMENT_2, COMMENT_3));
		given(service.getCommentsByUsername(NON_EXISTING_STRING)).willReturn(Collections.emptyList());
	}
	
	
	@Test
	public void getFromItemIdTest() throws Exception {
		mvc.perform(get("/comment/item?itemId={itemId}", ITEM_ID_1))
				
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				
				.andExpect(jsonPath("$", hasSize(1)))
				
				.andExpect(jsonPath("$[0].content", is(COMM_1)))
				.andExpect(jsonPath("$[0].itemId", is(ITEM_ID_1)))
				.andExpect(jsonPath("$[0].username", is(USERNAME_1)));
	}
	
	@Test
	public void getFromItemIdTest_NotFound() throws Exception {
		mvc.perform(get("/comment/item?itemId={itemId}", NON_EXISTING_STRING))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$", anEmptyMap()));
	}
	
	@Test
	public void getFromItemIdTest_MissingParam() throws Exception {
		mvc.perform(get("/comment/item"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", is("400")))
				.andExpect(jsonPath("$.reason", not(emptyString())));
	}

	
	@Test
	public void getFromUserTest() throws Exception {
		mvc.perform(get("/comment/user?username={0}", USERNAME_1))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				
				.andExpect(jsonPath("$", hasSize(3)))
				
				.andExpect(jsonPath("$[0].content", is(COMM_1)))
				.andExpect(jsonPath("$[0].itemId", is(ITEM_ID_1)))
				.andExpect(jsonPath("$[0].username", is(USERNAME_1)))
				
				.andExpect(jsonPath("$[2].content", is(COMM_3)))
				.andExpect(jsonPath("$[2].itemId", is(ITEM_ID_2)))
				.andExpect(jsonPath("$[2].username", is(USERNAME_1)));
	}

	@Test
	public void getFromUserTest_NotFound() throws Exception {
		mvc.perform(get("/comment/user?username={0}", NON_EXISTING_STRING))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$", empty()));
	}

	@Test
	public void getFromUserTest_MissingParam() throws Exception {
		mvc.perform(get("/comment/user"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", is("400")))
				.andExpect(jsonPath("$.reason", not(emptyString())));
	}
	

	@Test
	public void postCommentTest() throws Exception {
		final long COMMENT_1_ID = 9876L;

		doAnswer((Answer<ItemComment>) invocation -> {
			String content = invocation.getArgument(0);
			COMMENT_1.setId(COMMENT_1_ID);
			COMMENT_1.setContent(content);
			return COMMENT_1;
		}).when(service).postCommentToItem(ArgumentMatchers.anyString(), 
				eq(COMMENT_1.getItem().getId()), eq(COMMENT_1.getUsername()));

		mvc.perform(post("/comment?itemId={0}&username={1}", 
				COMMENT_1.getItem().getId(), COMMENT_1.getUsername())
				.content("new content"))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is((int)COMMENT_1_ID)))
				.andExpect(jsonPath("$.content", is("new content")));
	}

	@Test
	public void postCommentTest_NotFound() throws Exception {
		given(service.postCommentToItem("", NON_EXISTING_STRING, USERNAME_1)).willReturn(null);

		mvc.perform(post("/comment?itemId={0}&username={1}", NON_EXISTING_STRING, USERNAME_1)
				.content("new content"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void postCommentTest_EmptyReqBody() throws Exception {
		given(service.postCommentToItem("", ITEM_ID_1, USERNAME_1)).willReturn(null);

		mvc.perform(post("/comment?itemId={0}&username={1}", ITEM_ID_1, USERNAME_1))
				.andExpect(status().is5xxServerError());
	}

	@Test
	public void postCommentTest_MissingParam() throws Exception {
		mvc.perform(post("/comment").content("new content"))
				.andExpect(status().is4xxClientError());
	}
	
	
	@Test
	public void editCommentTest() throws Exception {
		final long COMMENT_1_ID = 9876L;
		COMMENT_1.setId(COMMENT_1_ID);
		
		doAnswer((Answer<ItemComment>) invocation -> {
			String content = invocation.getArgument(0);
			COMMENT_1.setContent(content);
			return COMMENT_1;
		}).when(service).updateComment(ArgumentMatchers.anyString(), eq(COMMENT_1.getId()));
		
		mvc.perform(put("/comment/{commentId}", COMMENT_1.getId()).content("updated content"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.content", is("updated content")));
	}
	
	@Test
	public void editCommentTest_NotFound() throws Exception {
		given(service.updateComment("", NON_EXISTING_LONG)).willReturn(null);
		
		mvc.perform(put("/comment/{commentId}", NON_EXISTING_LONG).content("updated content"))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void editCommentTest_EmptyReqBody() throws Exception {
		given(service.updateComment("", NON_EXISTING_LONG)).willReturn(null);
		
		mvc.perform(put("/comment/{commentId}", NON_EXISTING_LONG))
				.andExpect(status().is5xxServerError());
	}
	
	@Test
	public void editCommentTest_BadParam() throws Exception {
		mvc.perform(put("/comment/{commentId}", NON_EXISTING_STRING))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", is("400")))
				.andExpect(jsonPath("$.reason", not(emptyString())));
	}
	
	@Test
	public void editCommentTest_MissingParam() throws Exception {
		mvc.perform(put("/comment"))
				.andExpect(status().is4xxClientError());
	}
	
	
	@Test
	public void deleteCommentTest() throws Exception {
		final long COMMENT_1_ID = 9876L;
		COMMENT_1.setId(COMMENT_1_ID);
		
		given(service.deleteComment(COMMENT_1.getId())).willReturn(true);
		
		mvc.perform(delete("/comment/{commentId}", COMMENT_1.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$", is(true)));
	}
	
	@Test
	public void deleteCommentTest_NotFound() throws Exception {
		given(service.deleteComment(NON_EXISTING_LONG)).willReturn(false);
		
		mvc.perform(delete("/comment/{commentId}", NON_EXISTING_LONG))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteCommentTest_BadParam() throws Exception {
		mvc.perform(delete("/comment/{commentId}", NON_EXISTING_STRING))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", is("400")))
				.andExpect(jsonPath("$.reason", not(emptyString())));
	}
	
	@Test
	public void deleteCommentTest_MissingParam() throws Exception {
		mvc.perform(delete("/comment"))
				.andExpect(status().is4xxClientError());
	}
	
}
