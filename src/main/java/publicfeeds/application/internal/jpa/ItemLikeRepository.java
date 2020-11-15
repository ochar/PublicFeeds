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
import publicfeeds.domain.ItemLike;

/**
 * Spring Data JPA repository for accessing ItemLike type object.
 *
 * @author io
 */
public interface ItemLikeRepository extends JpaRepository<ItemLike, Long> {
	
	/**
	 * Retrieves ItemLikes from an Item.
	 * 
	 * @param itemId id of Item which item likes is to be retrieved.
	 * @return List of item likes found. 
	 */
	List<ItemLike> findByItemId(String itemId);
	
	/**
	 * Finds ItemLikes from user with username.
	 * 
	 * @param username username of user which item likes to be retrieved.
	 * @return List of item likes found. 
	 */
	List<ItemLike> findByUsername(String username);
	
}
