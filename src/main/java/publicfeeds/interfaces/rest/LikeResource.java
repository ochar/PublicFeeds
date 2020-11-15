/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.interfaces.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import publicfeeds.application.Service;
import publicfeeds.domain.Item;
import publicfeeds.domain.ItemLike;

/**
 * Rest endpoints serving {@link ItemLike} type object. 
 *
 * @author io
 */
@RequestMapping("/like")
@RestController
public class LikeResource {
	
	@Autowired Service service;
	
	
	/**
	 * Get {@link ItemLike} from an {@link Item} which has given id.
	 *
	 * @param itemId a query parameter, id of Item which likes to be retrieved
	 * @return if Item is found, returns {@link ResponseEntity} with status 200
	 * OK and containing its ItemLike, otherwise status 404 NOT FOUND and empty
	 * body
	 */
	@GetMapping("/item")
	public ResponseEntity getFromItem(@RequestParam(name = "itemId") String itemId) {
		Optional<Item> foundItem = service.getItemById(itemId);
		if (foundItem.isPresent()) {		
			return new ResponseEntity(service.getLikesByItemId(foundItem.get().getId()), HttpStatus.OK);
		} else {
			return new ResponseEntity(new HashMap<String, ItemLike>(0), HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Get all {@link ItemLike} from given username.
	 *
	 * @param username a query parameter, username which likes to be retrieved
	 * @return list of likes with given username, empty if not found
	 */
	@GetMapping("/user")
	public List<ItemLike> getFromUser(@RequestParam(name = "username") String username) {
		return service.getLikesByUsername(username);
	}
	
	/**
	 * Creates a new like for an {@link Item} of given id and username
	 *
	 * @param itemId a query parameter, id of item which the like is for
	 * @param username a query parameter, username which the like is from
	 * @return if corresponding Item is found and like successfully created,
	 * returns {@link ResponseEntity} with status 200 OK and containing the
	 * ItemLike, otherwise status 404 NOT FOUND and empty body
	 */
	@PutMapping
	public ResponseEntity like(
			@RequestParam(name = "itemId") String itemId, 
			@RequestParam(name = "username") String username) {
		
		ItemLike like = service.likeAnItem(itemId, username);
		if (like != null) {
			return new ResponseEntity(like, HttpStatus.CREATED);
		} else {
			return new ResponseEntity(null, HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Deletes a like for an {@link Item} of given id and username
	 *
	 * @param itemId a query parameter, id of item which the like is for
	 * @param username a query parameter, username which the like is from
	 * @return if corresponding Item is found and like successfully deleted,
	 * returns {@link ResponseEntity} with status 200 OK and containing String
	 * true, otherwise status 404 NOT FOUND and empty body
	 */
	@DeleteMapping
	public ResponseEntity unlike(
			@RequestParam(name = "itemId") String itemId, 
			@RequestParam(name = "username") String username) {
		
		boolean success = service.unlikeAnItem(itemId, username);
		if (success) {
			return new ResponseEntity(true, HttpStatus.OK);
		} else {
			return new ResponseEntity(null, HttpStatus.NOT_FOUND);
		}
	}
	
}
