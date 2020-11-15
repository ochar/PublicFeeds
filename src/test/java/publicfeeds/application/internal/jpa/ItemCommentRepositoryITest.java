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

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static publicfeeds.application.internal.jpa.InitTestFixtures.*;
import publicfeeds.domain.Item;
import publicfeeds.domain.ItemComment;

/**
 *
 * @author io
 */
@DataJpaTest
public class ItemCommentRepositoryITest {
	
	@Autowired
	private ItemCommentRepository commentRepo;

	@Autowired
	private TestEntityManager em;

	@BeforeEach
	public void init() {
		InitTestFixtures.init(em);
	}

	@AfterEach
	public void postTest() {
		InitTestFixtures.clear(em);
	}
	
	
	
	@Test
	public void findByItemIdTest_Found() {

		List<ItemComment> foundComments = commentRepo.findByItemId(ITEM_ID_1);

		assertThat(foundComments).isNotEmpty()
				.hasSize(1)
				.first()
				.matches(like -> USERNAME_1.equals(like.getUsername()))
				.extracting(ItemComment::getItem)
				.extracting(Item::getId)
				.matches(itemId -> ITEM_ID_1.equals(itemId));
	}

	@Test
	public void findByItemIdTest_NotFound() {
		List<ItemComment> foundComments = commentRepo.findByItemId("some random id");

		assertThat(foundComments).isEmpty();
	}

	@Test
	public void findByUsernameTest_Found() {

		List<ItemComment> foundComments = commentRepo.findByUsername(USERNAME_1);

		assertThat(foundComments).isNotEmpty()
				.hasSize(3)
				.element(1)
				.matches(like -> USERNAME_1.equals(like.getUsername()))
				.extracting(ItemComment::getItem)
				.extracting(Item::getId)
				.matches(itemId -> ITEM_ID_2.equals(itemId));
	}

	@Test
	public void findByUsernameTest_NotFound() {
		List<ItemComment> foundComments = commentRepo.findByUsername("some random username");

		assertThat(foundComments).isEmpty();
	}
	
}
