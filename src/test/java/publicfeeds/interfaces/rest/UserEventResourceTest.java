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
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import publicfeeds.application.Service;
import publicfeeds.application.internal.SpringBootAppConfig;
import publicfeeds.domain.Author;
import publicfeeds.domain.Item;
import publicfeeds.domain.Media;
import publicfeeds.domain.UserEvent;

/**
 *
 * @author io
 */
@ContextConfiguration(classes = SpringBootAppConfig.class)
@WebMvcTest(UserEventResource.class)
public class UserEventResourceTest {
	
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
	
	private final UserEvent EV1 = new UserEvent(Instant.EPOCH, USERNAME_1, "like", ITEM_ID_1);
	private final UserEvent EV2 = new UserEvent(Instant.EPOCH, USERNAME_1, "like", ITEM_ID_2);
	private final UserEvent EV3 = new UserEvent(Instant.EPOCH, USERNAME_2, "like", ITEM_ID_1);
	private final UserEvent EV4 = new UserEvent(Instant.EPOCH, USERNAME_2, "like", ITEM_ID_2);
	private final UserEvent EV5 = new UserEvent(Instant.EPOCH, USERNAME_3, "like", ITEM_ID_1);
	private final UserEvent EV6 = new UserEvent(Instant.EPOCH, USERNAME_3, "like", ITEM_ID_2);
	
	private final UserEvent EV7 = new UserEvent(Instant.EPOCH, USERNAME_1, "comment", ITEM_ID_1);
	private final UserEvent EV8 = new UserEvent(Instant.EPOCH, USERNAME_1, "comment", ITEM_ID_2);
	private final UserEvent EV9 = new UserEvent(Instant.EPOCH, USERNAME_1, "comment", ITEM_ID_2);
	
	@BeforeEach
	public void init() {
		given(service.getItemById(ITEM_ID_1)).willReturn(Optional.of(ITEM_1));
		given(service.getItemById(ITEM_ID_2)).willReturn(Optional.of(ITEM_2));
		
		given(service.getUserEventsByItemId(ITEM_ID_1)).willReturn(Arrays.asList(EV1, EV3, EV5, EV7));
		given(service.getUserEventsByItemId(ITEM_ID_2)).willReturn(Arrays.asList(EV2, EV4, EV6, EV8, EV9));
		given(service.getUserEventsByItemId(NON_EXISTING_STRING)).willReturn(Collections.emptyList());
		
		given(service.getUserEventsByUsername(USERNAME_1)).willReturn(Arrays.asList(EV1, EV2, EV7, EV8, EV9));
		given(service.getUserEventsByUsername(NON_EXISTING_STRING)).willReturn(Collections.emptyList());
	}
	
	
	@Test
	public void getFromItemIdTest() throws Exception {
		mvc.perform(get("/user-event/item?itemId={itemId}", ITEM_ID_1))
				
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				
				.andExpect(jsonPath("$", hasSize(4)))
				
				.andExpect(jsonPath("$[0].time", is(Instant.EPOCH.toString())))
				.andExpect(jsonPath("$[0].username", is(USERNAME_1)))
				.andExpect(jsonPath("$[0].message", is("like")))
				.andExpect(jsonPath("$[0].itemId", is(ITEM_ID_1)))
				
				.andExpect(jsonPath("$[3].time", is(Instant.EPOCH.toString())))
				.andExpect(jsonPath("$[3].username", is(USERNAME_1)))
				.andExpect(jsonPath("$[3].message", is("comment")))
				.andExpect(jsonPath("$[3].itemId", is(ITEM_ID_1)));
	}
	
	@Test
	public void getFromItemIdTest_NotFound() throws Exception {
		mvc.perform(get("/user-event/item?itemId={itemId}", NON_EXISTING_STRING))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$", anEmptyMap()));
	}
	
	@Test
	public void getFromItemIdTest_MissingParam() throws Exception {
		mvc.perform(get("/user-event/item"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", is("400")))
				.andExpect(jsonPath("$.reason", not(emptyString())));
	}
	
	@Test
	public void getFromUserTest() throws Exception {
		mvc.perform(get("/user-event/user?username={0}", USERNAME_1))
				
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				
				.andExpect(jsonPath("$", hasSize(5)))
				
				.andExpect(jsonPath("$[0].time", is(Instant.EPOCH.toString())))
				.andExpect(jsonPath("$[0].username", is(USERNAME_1)))
				.andExpect(jsonPath("$[0].message", is("like")))
				.andExpect(jsonPath("$[0].itemId", is(ITEM_ID_1)))
				
				.andExpect(jsonPath("$[3].time", is(Instant.EPOCH.toString())))
				.andExpect(jsonPath("$[3].username", is(USERNAME_1)))
				.andExpect(jsonPath("$[3].message", is("comment")))
				.andExpect(jsonPath("$[3].itemId", is(ITEM_ID_2)));
	}
	
	@Test
	public void getFromUserTest_NotFound() throws Exception {
		mvc.perform(get("/user-event/user?username={0}", NON_EXISTING_STRING))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$", empty()));
	}
	
	@Test
	public void getFromUserTest_MissingParam() throws Exception {
		mvc.perform(get("/user-event/user"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", is("400")))
				.andExpect(jsonPath("$.reason", not(emptyString())));
	}
	
}
