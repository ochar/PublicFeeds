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
import publicfeeds.domain.ItemComment;

/**
 * Spring Data JPA repository for accessing ItemComment type object.
 *
 * @author io
 */
public interface ItemCommentRepository extends JpaRepository<ItemComment, Long> {
	
	/**
	 * Retrieves ItemComments from an Item.
	 * 
	 * @param itemId id of Item which comments is to be retrieved.
	 * @return List of comments found.
	 */
	List<ItemComment> findByItemId(String itemId);
	
	/**
	 * Finds comments from user with username.
	 * 
	 * @param username username of user which comments to be retrieved.
	 * @return List of comments found.
	 */
	List<ItemComment> findByUsername(String username);
	
}