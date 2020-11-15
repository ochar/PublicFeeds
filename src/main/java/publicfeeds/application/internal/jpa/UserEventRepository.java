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
import org.springframework.data.jpa.repository.Query;
import publicfeeds.domain.UserEvent;

/**
 * Spring Data JPA repository for accessing UserEvent type object.
 *
 * @author io
 */
public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
	
	/**
	 * Finds events which related to Item
	 * 
	 * @param itemId id of Item which related to the event.
	 * @return List of events found.
	 */
	@Query("SELECT e FROM UserEvent e WHERE e.itemId = ?1 ORDER BY e.time DESC")
	List<UserEvent> findByItemId(String itemId);
	
	/**
	 * Finds events which related to user
	 * 
	 * @param username username of user which related to the event.
	 * @return List of events found.
	 */
	@Query("SELECT e FROM UserEvent e WHERE e.username = ?1 ORDER BY e.time DESC")
	List<UserEvent> findByUsername(String username);
	
}
