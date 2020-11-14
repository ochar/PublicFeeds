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
import publicfeeds.application.internal.Service;
import publicfeeds.domain.Item;

/**
 *
 * @author io
 */
@RequestMapping("/saved")
@RestController
public class SavedItemResource {
	
	@Autowired private Service service;
	
	
	@GetMapping("/{id}")
	public ResponseEntity getById(@PathVariable String id) {
		Optional<Item> foundItem = service.getItemById(id);
		if (foundItem.isPresent()) {
			return new ResponseEntity(foundItem.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity(new HashMap<String, String>(0), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping
	public List<Item> getAll() {
		return service.getAllItems();
	}
	
	@GetMapping("/count")
	public Map<String, Integer> count() {
		Map<String, Integer> result = new HashMap<>(1);
		result.put("count", (int)service.countItems());
		return result;
	}
	
	@GetMapping("/clear")
	public ResponseEntity clear() {
		return deleteAll();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity deleteById(@PathVariable String id) {
		List<String> ids = new ArrayList<>(1);
		ids.add(id);
		
		service.deleteItemByIds(ids);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@DeleteMapping
	public ResponseEntity deleteAll() {
		service.deleteAllItems();
		return new ResponseEntity(HttpStatus.OK);
	}
	
}
