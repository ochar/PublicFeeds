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
import java.util.List;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import publicfeeds.application.Fetcher;
import publicfeeds.application.Service;
import publicfeeds.application.internal.SpringBootAppConfig;
import publicfeeds.domain.Author;
import publicfeeds.domain.Item;
import publicfeeds.domain.Media;

/**
 *
 * @author io
 */
@ContextConfiguration(classes = SpringBootAppConfig.class)
@WebMvcTest(FeedResource.class)
public class FeedResourceTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean private Service service;
	@MockBean private Fetcher FETCHER;
	@MockBean private ItemCache itemCache;
	
	
	private final String NON_EXISTING_STRING = "non existing";
	private final long NON_EXISTING_LONG = 987654L;
	
	
	private final Author AUTHOR_A = new Author("137858975@N02", "depablette");
	private final Item ITEM_1 = new Item("50599310838", 
			"La Jungle, L’Entrepôt Arlon 2018",
			"https://www.flickr.com/photos/depablette/50599310838/",
			new Media(
					"https://live.staticflickr.com/65535/50599310838_6970543c4c_m.jpg", 
					"La Jungle, L’Entrepôt Arlon 2018", 
					Collections.emptyList()),
			Instant.parse("2018-11-11T07:49:12Z"),
			"insert html here",
			Instant.parse("2020-11-14T08:49:18Z"),
			AUTHOR_A);
	
	private final Author AUTHOR_B = new Author("125788771@N07", "cesarmongegonzalez");
	private final Item ITEM_2 = new Item("50599311963", 
			"2020-11-14_09-49-43",
			"https://www.flickr.com/photos/125788771@N07/50599311963/",
			new Media(
					"https://live.staticflickr.com/65535/50599311963_3c1a46d2ba_m.jpg", 
					"2020-11-14_09-49-43", 
					Collections.emptyList()),
			Instant.parse("2020-10-24T20:26:42Z"),
			"insert html here",
			Instant.parse("2020-11-14T08:49:48Z"),
			AUTHOR_B);
	
	private final Author AUTHOR_C = new Author("62264172@N00", "International Diabetes Federation");
	private final Item ITEM_3 = new Item("50599312353", 
			"file",
			"https://www.flickr.com/photos/idf/50599312353/",
			new Media(
					"https://live.staticflickr.com/65535/50599312353_af7302e876_m.jpg", 
					"file", 
					Collections.emptyList()),
			Instant.parse("2020-11-14T08:49:58Z"),
			"insert html here",
			Instant.parse("2020-11-14T08:49:58Z"),
			AUTHOR_C);
	
	private final Author AUTHOR_D = new Author("75317139@N05", "Elena m.d.");
	private final Item ITEM_4 = new Item("50599312413", 
			"Fotogenic_6702f0bc-13dd-468a-8197-4084799dda35",
			"https://www.flickr.com/photos/75317139@N05/50599312413/",
			new Media(
					"https://live.staticflickr.com/65535/50599312413_d18a39af12_m.jpg", 
					"Fotogenic_6702f0bc-13dd-468a-8197-4084799dda35", 
					Collections.emptyList()),
			Instant.parse("2020-11-14T08:49:59Z"),
			"insert html here",
			Instant.parse("2020-11-14T08:49:59Z"),
			AUTHOR_D);
	
	private final Author AUTHOR_E = new Author("9277249@N03", "Darin Kamnetz");
	private final Item ITEM_5 = new Item("50599312933", 
			"2020-11-12 U of M Occupational Therapy-Darin Kamnetz - 00989.jpg",
			"https://www.flickr.com/photos/darin_k_/50599312933/",
			new Media(
					"https://live.staticflickr.com/65535/50599312933_803d860a68_m.jpg", 
					"2020-11-12 U of M Occupational Therapy-Darin Kamnetz - 00989.jpg", 
					Collections.emptyList()),
			Instant.parse("2020-11-12T18:09:04Z"),
			"insert html here",
			Instant.parse("2020-11-14T08:50:11Z"),
			AUTHOR_E);
	
	private final Author AUTHOR_F = new Author("54989697@N02", "eitb.eus");
	private final Item ITEM_6 = new Item("50599313198", 
			"Amanecer por el Burgoa",
			"https://www.flickr.com/photos/54989697@N02/50599313198/",
			new Media(
					"https://live.staticflickr.com/65535/50599313198_3e41708d5d_m.jpg", 
					"Amanecer por el Burgoa", 
					Arrays.asList("eitbcom", "36093", "g1", "tiemponaturaleza", 
							"tiempon2020", "bizkaia", "bakio", "joséluisazaola")),
			Instant.parse("2020-11-14T17:18:28Z"),
			"insert html here",
			Instant.parse("2020-11-14T08:50:19Z"),
			AUTHOR_F);
	
	private final Author AUTHOR_G = new Author("95114211@N08", "Sapienza Università di Roma_Archivio fotografico");
	private final Item ITEM_7 = new Item("50600056581", 
			"🌄 #Buongiorno Sapienza e #buonsabato con una foto di una fontanella della Città universitaria di @valemo96 ・・・ #Goodmorning and have a nice Saturday from a water fountain in the main City Campus ・・・ #Repost: «Studium Urbis 📜",
			"https://www.flickr.com/photos/sapienzaroma/50600056581/",
			new Media(
					"https://live.staticflickr.com/65535/50600056581_b310a421ec_m.jpg", 
					"🌄 #Buongiorno Sapienza e #buonsabato con una foto di una fontanella della Città universitaria di @valemo96 ・・・ #Goodmorning and have a nice Saturday from a water fountain in the main City Campus ・・・ #Repost: «Studium Urbis 📜", 
					Arrays.asList("ifttt", "instagram")),
			Instant.parse("2020-11-14T17:49:10Z"),
			"insert html here",
			Instant.parse("2020-11-14T08:49:10Z"),
			AUTHOR_G);
	
	private final Author AUTHOR_H = new Author("147432645@N07", "Melissen-Ghost");
	private final Item ITEM_8 = new Item("50596865878", 
			"9283745928736922246",
			"https://www.flickr.com/photos/147432645@N07/50596865878/",
			new Media(
					"https://live.staticflickr.com/65535/50596865878_fe86026aef_m.jpg", 
					"9283745928736922246", 
					Arrays.asList("japan", "tokyo", "street", "photography", "fuji",
							"fujifilm", "color", "grain", "urban", "streets")),
			Instant.parse("2020-10-18T21:55:06Z"),
			"insert html here",
			Instant.parse("2020-11-13T16:35:20Z"),
			AUTHOR_H);
	private final Item ITEM_9 = new Item("50597613561", 
			"2456920485098246723",
			"https://www.flickr.com/photos/147432645@N07/50597613561/",
			new Media(
					"https://live.staticflickr.com/65535/50597613561_3c744869e6_m.jpg", 
					"2456920485098246723", 
					Arrays.asList("japan", "tokyo", "street", "photography", "fuji",
							"fujifilm", "color", "grain", "urban", "streets")),
			Instant.parse("2020-10-18T23:23:15Z"),
			"insert html here",
			Instant.parse("2020-11-13T16:35:20Z"),
			AUTHOR_H);
	private final Item ITEM_10 = new Item("50597612246", 
			"923047850123475689021457",
			"https://www.flickr.com/photos/147432645@N07/50597612246/",
			new Media(
					"https://live.staticflickr.com/65535/50597612246_57209aef19_m.jpg", 
					"923047850123475689021457", 
					Arrays.asList("japan", "tokyo", "street", "photography", "fuji",
							"fujifilm", "color", "grain", "urban", "streets")),
			Instant.parse("2020-10-20T22:42:17Z"),
			"insert html here",
			Instant.parse("2020-11-13T16:35:20Z"),
			AUTHOR_H);
	private final Item ITEM_11 = new Item("50597738342", 
			"32495871935861354245",
			"https://www.flickr.com/photos/147432645@N07/50597738342/",
			new Media(
					"https://live.staticflickr.com/65535/50597738342_11727e3a83_m.jpg", 
					"32495871935861354245", 
					Arrays.asList("japan", "tokyo", "street", "photography", "fuji",
							"fujifilm", "color", "grain", "urban", "streets")),
			Instant.parse("2020-09-03T06:00:56Z"),
			"insert html here",
			Instant.parse("2020-11-13T16:35:20Z"),
			AUTHOR_H);
	private final Item ITEM_12 = new Item("50596871933", 
			"723495782956245774",
			"https://www.flickr.com/photos/147432645@N07/50596871933/",
			new Media(
					"https://live.staticflickr.com/65535/50596871933_4b9b5766b7_m.jpg", 
					"723495782956245774", 
					Arrays.asList("japan", "tokyo", "street", "photography", "fuji",
							"fujifilm", "color", "grain", "urban", "streets")),
			Instant.parse("2020-11-03T01:47:30Z"),
			"insert html here",
			Instant.parse("2020-11-13T16:35:21Z"),
			AUTHOR_H);
	
	private final Author AUTHOR_I = new Author("191014191@N04", "OsborneToRule");
	private final Item ITEM_13 = new Item("50598933111", 
			"Evie clutching her toy.",
			"https://www.flickr.com/photos/191014191@N04/50598933111/",
			new Media(
					"https://live.staticflickr.com/65535/50598933111_1eb9290aa8_m.jpg", 
					"Evie clutching her toy.", 
					Arrays.asList("cat", "gingercat", "ginger", "cats", "fuji",
							"fujifilm", "xt20", "56mmf12", "xf56mmf12", "stairs",
							"portrait", "southend", "southendonsea", "essex", "bokeh")),
			Instant.parse("2020-01-15T00:05:50Z"),
			"insert html here",
			Instant.parse("2020-11-14T00:21:46Z"),
			AUTHOR_I);
	
	
	private final List<Item> itemList = Arrays.asList(ITEM_1, ITEM_2, ITEM_3, ITEM_4,
			ITEM_5, ITEM_6, ITEM_7, ITEM_8, ITEM_9, ITEM_10, ITEM_11, ITEM_12, ITEM_13);
	
	
	@BeforeEach
	public void init() {
		given(FETCHER.fetchPlain()).willReturn(itemList);
	}
	
	@Test
	public void currentTest() throws Exception {
		mvc.perform(get("/feed/current"))
				
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				
				.andExpect(jsonPath("$.pageSize", is(20)))
				.andExpect(jsonPath("$.currentPageNumber", is(1)))
				.andExpect(jsonPath("$.totalPage", is(1)))
				.andExpect(jsonPath("$.totalCount", is(13)))
				
				.andExpect(jsonPath("$.content", hasSize(13)))
				
				.andExpect(jsonPath("$.content[6].id", is(ITEM_7.getId())))
				.andExpect(jsonPath("$.content[6].title", is(ITEM_7.getTitle())))
				.andExpect(jsonPath("$.content[6].link", is(ITEM_7.getLink())))
				.andExpect(jsonPath("$.content[6].media.contentUrl", is(ITEM_7.getMedia().getContentUrl())))
				.andExpect(jsonPath("$.content[6].media.tags.length()", is(ITEM_7.getMedia().getTags().size())))
				.andExpect(jsonPath("$.content[6].takenDate", is(ITEM_7.getTakenDate().toString())))
				.andExpect(jsonPath("$.content[6].htmlDescription", is(ITEM_7.getHtmlDescription())))
				.andExpect(jsonPath("$.content[6].publishedDate", is(ITEM_7.getPublishedDate().toString())))
				.andExpect(jsonPath("$.content[6].author.id", is(ITEM_7.getAuthor().getId())))
				.andExpect(jsonPath("$.content[6].author.name", is(ITEM_7.getAuthor().getName())))
				
				.andExpect(jsonPath("$.content[12].id", is(ITEM_13.getId())))
				.andExpect(jsonPath("$.content[12].title", is(ITEM_13.getTitle())))
				.andExpect(jsonPath("$.content[12].link", is(ITEM_13.getLink())))
				.andExpect(jsonPath("$.content[12].media.contentUrl", is(ITEM_13.getMedia().getContentUrl())))
				.andExpect(jsonPath("$.content[12].media.tags.length()", is(ITEM_13.getMedia().getTags().size())))
				.andExpect(jsonPath("$.content[12].takenDate", is(ITEM_13.getTakenDate().toString())))
				.andExpect(jsonPath("$.content[12].htmlDescription", is(ITEM_13.getHtmlDescription())))
				.andExpect(jsonPath("$.content[12].publishedDate", is(ITEM_13.getPublishedDate().toString())))
				.andExpect(jsonPath("$.content[12].author.id", is(ITEM_13.getAuthor().getId())))
				.andExpect(jsonPath("$.content[12].author.name", is(ITEM_13.getAuthor().getName())));
	}
	
	@Test
	public void currentTest_Paging() throws Exception {
		mvc.perform(get("/feed/current?pageSize=3&pageNumber=3"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				
				.andExpect(jsonPath("$.pageSize", is(3)))
				.andExpect(jsonPath("$.currentPageNumber", is(3)))
				.andExpect(jsonPath("$.totalPage", is(5)))
				.andExpect(jsonPath("$.totalCount", is(13)))
				
				.andExpect(jsonPath("$.content", hasSize(3)));
	}
	
	@Test
	public void currentTest_PagingException() throws Exception {
		mvc.perform(get("/feed/current?pageSize=3&pageNumber=23"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", is("400")))
				.andExpect(jsonPath("$.reason", not(emptyString())));
	}
	
	@Test
	public void saveCurrentFeedTest() throws Exception {
		
	}
	
	@Test
	public void saveFeedsTest() throws Exception {
		
	}
	
	@Test
	public void queryTagsTest() throws Exception {
		
	}
	
	@Test
	public void saveQueryTagsTest() throws Exception {
		
	}
	
	@Test
	public void queryIdsTest() throws Exception {
		
	}
	
	@Test
	public void saveQueryIdsTest() throws Exception {
		
	}
	
	@Test
	public void processSearchTest() throws Exception {
		
	}
	
	@Test
	public void searchItemsByDateTest() throws Exception {
		
	}
	
	@Test
	public void searchItemsTest() throws Exception {
		
	}
	
	
}
