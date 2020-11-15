/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
