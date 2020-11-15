/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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