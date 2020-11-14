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
 *
 * @author io
 */
public interface ItemLikeReposistory extends JpaRepository<ItemLike, Long> {
	
	List<ItemLike> findByItemId(String itemId);
	
	List<ItemLike> findByUsername(String username);
	
}
