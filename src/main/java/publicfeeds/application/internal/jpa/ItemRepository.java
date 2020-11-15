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
import org.springframework.data.jpa.repository.JpaRepository;
import publicfeeds.domain.Item;

/**
 * Spring Data JPA repository for accessing Item type object.
 *
 * @author io
 */
public interface ItemRepository extends JpaRepository<Item, String> {
	
	/**
	 * Finds Items which has author with id.
	 * 
	 * @param authorId id of the author which Items to be found
	 * @return List of Items found. 
	 */
	List<Item> findByAuthorId(String authorId);
	
}
