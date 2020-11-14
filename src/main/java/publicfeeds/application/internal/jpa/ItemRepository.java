/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.application.internal.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import publicfeeds.domain.Item;

/**
 *
 * @author io
 */
public interface ItemRepository extends JpaRepository<Item, String> {
	
}
