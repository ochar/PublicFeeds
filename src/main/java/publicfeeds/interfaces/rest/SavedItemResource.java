/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.interfaces.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import publicfeeds.application.Service;
import publicfeeds.domain.Item;

/**
 * Rest endpoints serving already saved {@link Item} type object.
 *
 * @author io
 */
@RequestMapping("/saved")
@RestController
public class SavedItemResource {
	
	@Autowired private Service service;
	
	
	/**
	 * Get an {@link Item} which has given id.
	 *
	 * @param id a path variable, id of Item to be retrieved
	 * @return if Item is found, returns {@link ResponseEntity} with status 200
	 * OK and containing it, otherwise status 404 NOT FOUND and empty body
	 */
	@GetMapping("/{id}")
	public ResponseEntity getById(@PathVariable String id) {
		Optional<Item> foundItem = service.getItemById(id);
		if (foundItem.isPresent()) {
			return new ResponseEntity(foundItem.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity(new HashMap<String, String>(0), HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Get all saved {@link Item}.
	 *
	 * @return list of saved items, empty if there isn't any
	 */
	@GetMapping
	public List<Item> getAll() {
		return service.getAllItems();
	}
	
	/**
	 * Returns the number of saved {@link Item}.
	 *
	 * @return a map containing entry with key: "count" and value: saved items 
	 * count
	 */
	@GetMapping("/count")
	public Map<String, Integer> count() {
		Map<String, Integer> result = new HashMap<>(1);
		result.put("count", (int)service.countItems());
		return result;
	}
	
	/**
	 * Removes all saved items.
	 * Delegates to {@link #deleteAll}.
	 * 
	 * @return if successful, {@link ResponseEntity} with status 200 OK
	 * @see #deleteAll()
	 */
	@GetMapping("/clear")
	public ResponseEntity clear() {
		return deleteAll();
	}
	
	/**
	 * Removes a saved {@link Item} with given id.
	 *
	 * @param id a path variable, id of saved item to be deleted
	 * @return if item is found and successfully deleted, {@link ResponseEntity}
	 * with status 200 OK
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity deleteById(@PathVariable String id) {
		List<String> ids = new ArrayList<>(1);
		ids.add(id);
		
		service.deleteItemByIds(ids);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	/**
	 * Removes all saved items.
	 * 
	 * @return if successful, {@link ResponseEntity} with status 200 OK
	 */
	@DeleteMapping
	public ResponseEntity deleteAll() {
		service.deleteAllItems();
		return new ResponseEntity(HttpStatus.OK);
	}
	
}
