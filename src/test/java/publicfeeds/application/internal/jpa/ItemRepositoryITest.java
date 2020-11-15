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
import org.springframework.test.context.ActiveProfiles;
import static publicfeeds.application.internal.jpa.InitTestFixtures.*;
import publicfeeds.domain.Item;

/**
 *
 * @author io
 */
//@ActiveProfiles("test-jpa")
@DataJpaTest
public class ItemRepositoryITest {

	@Autowired
	private ItemRepository itemRepo;
	
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
	public void findByAuthorIdTest_Found() {
		
		List<Item> foundItems = itemRepo.findByAuthorId(AUTHOR_ID_1);
		
		assertThat(foundItems).isNotEmpty()
				.first().extracting(Item::getId)
				.matches(itemId -> ITEM_ID_1.equals(itemId));
		
		
		foundItems = itemRepo.findByAuthorId(AUTHOR_ID_2);
		
		assertThat(foundItems).isNotEmpty()
				.first().extracting(Item::getId)
				.matches(itemId -> ITEM_ID_2.equals(itemId));
	}
	
	@Test
	public void findByAuthorIdTest_NotFound() {
		List<Item> foundItems = itemRepo.findByAuthorId("some random id");
		
		assertThat(foundItems).isEmpty();
	}

}
