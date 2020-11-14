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
 *
 * @author io
 */
public interface ItemCommentRepository extends JpaRepository<ItemComment, Long> {
	
	List<ItemComment> findByItemId(String itemId);
	
	List<ItemComment> findByUsername(String username);
	
}