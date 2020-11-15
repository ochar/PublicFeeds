/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
