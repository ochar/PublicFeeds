/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
