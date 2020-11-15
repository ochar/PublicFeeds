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
package publicfeeds.application.internal.jpa;

import java.time.Instant;
import java.util.Collections;
import javax.persistence.EntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import publicfeeds.domain.Author;
import publicfeeds.domain.Item;
import publicfeeds.domain.ItemComment;
import publicfeeds.domain.ItemLike;
import publicfeeds.domain.Media;
import publicfeeds.domain.UserEvent;

/**
 *
 * @author io
 */
public class InitTestFixtures {
	
	static final String AUTHOR_ID_1 = "author7890";
	static final String ITEM_ID_1 = "1234";
	
	static final String AUTHOR_ID_2 = "buggy";
	static final String ITEM_ID_2 = "5678";
	
	static final String USERNAME_1 = "Agus";
	static final String USERNAME_2 = "Sapto";
	static final String USERNAME_3 = "Wisnu";
	
	static final String COMMENT_1 = "Noice!";
	static final String COMMENT_2 = "bit blurry";
	static final String COMMENT_3 = "autofocus too quick!?!";
	
	
	
	static void init(TestEntityManager em) {
		
		Author author1 = new Author(AUTHOR_ID_1, "mr. author");
		
		Item item1 = new Item(ITEM_ID_1, "Title", "urlszzz", 
				new Media("media url", "titles", Collections.EMPTY_LIST), 
				Instant.EPOCH, 
				"insert html here", 
				Instant.EPOCH.plusSeconds(86000),
				author1);
		
		Author author2 = new Author(AUTHOR_ID_2, "shutterbug");
		
		Item item2 = new Item(ITEM_ID_2, "no more", "urlszzz", 
				new Media("media url", "nomore", Collections.EMPTY_LIST), 
				Instant.EPOCH, 
				"insert html here", 
				Instant.EPOCH.plusSeconds(86000), 
				author2);
		
		em.persist(item1);
		em.persist(item2);
		
		
		ItemLike like1 = new ItemLike(item1, USERNAME_1);
		ItemLike like2 = new ItemLike(item2, USERNAME_1);
		ItemLike like3 = new ItemLike(item1, USERNAME_2);
		ItemLike like4 = new ItemLike(item2, USERNAME_2);
		ItemLike like5 = new ItemLike(item1, USERNAME_3);
		ItemLike like6 = new ItemLike(item2, USERNAME_3);
		
		em.persist(like1);
		em.persist(like2);
		em.persist(like3);
		em.persist(like4);
		em.persist(like5);
		em.persist(like6);
		
		
		ItemComment comment1 = new ItemComment(COMMENT_1, item1, USERNAME_1);
		ItemComment comment2 = new ItemComment(COMMENT_2, item2, USERNAME_1);
		ItemComment comment3 = new ItemComment(COMMENT_3, item2, USERNAME_1);
		
		em.persist(comment1);
		em.persist(comment2);
		em.persist(comment3);
		
		
		UserEvent ev1 = new UserEvent(Instant.EPOCH, USERNAME_1, "like", item1.getId());
		UserEvent ev2 = new UserEvent(Instant.EPOCH, USERNAME_1, "like", item2.getId());
		UserEvent ev3 = new UserEvent(Instant.EPOCH, USERNAME_2, "like", item1.getId());
		UserEvent ev4 = new UserEvent(Instant.EPOCH, USERNAME_2, "like", item2.getId());
		UserEvent ev5 = new UserEvent(Instant.EPOCH, USERNAME_3, "like", item1.getId());
		UserEvent ev6 = new UserEvent(Instant.EPOCH, USERNAME_3, "like", item2.getId());
		
		UserEvent ev7 = new UserEvent(Instant.EPOCH, USERNAME_1, "comment", item1.getId());
		UserEvent ev8 = new UserEvent(Instant.EPOCH, USERNAME_1, "comment", item2.getId());
		UserEvent ev9 = new UserEvent(Instant.EPOCH, USERNAME_1, "comment", item2.getId());
		
		em.persist(ev1);
		em.persist(ev2);
		em.persist(ev3);
		em.persist(ev4);
		em.persist(ev5);
		em.persist(ev6);
		
		em.persist(ev7);
		em.persist(ev8);
		em.persist(ev9);
		
	}
	
	
	
	static void clear(TestEntityManager em) {
		EntityManager em2 = em.getEntityManager();
		em2.createQuery("DELETE FROM ItemLike").executeUpdate();
		em2.createQuery("DELETE FROM ItemComment").executeUpdate();
		em2.createQuery("DELETE FROM Item").executeUpdate();
		em2.createQuery("DELETE FROM UserEvent").executeUpdate();
	}
	
}
