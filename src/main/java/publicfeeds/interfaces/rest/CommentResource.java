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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;
import publicfeeds.application.internal.Service;
import publicfeeds.domain.Item;
import publicfeeds.domain.ItemComment;

/**
 *
 * @author io
 */
@RequestMapping("/comment")
@RestController
public class CommentResource {
	
	@Autowired Service service;
	
	
	@GetMapping("/item")
	public ResponseEntity getFromItemId(@RequestParam(name = "itemId") String itemId) {
		Optional<Item> foundItem = service.getItemById(itemId);
		if (foundItem.isPresent()) {
			return new ResponseEntity(service.getCommentsByItemId(foundItem.get().getId()), HttpStatus.OK);
		} else {
			return new ResponseEntity(new HashMap<String, ItemComment>(0), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/user")
	public List<ItemComment> getFromUser(@RequestParam(name = "username") String username) {
		return service.getCommentsByUsername(username);
	}
	
	@PostMapping
	public ResponseEntity postComment(
			@RequestBody String content, 
			@RequestParam(name = "itemId") String itemId, 
			@RequestParam(name = "username") String username) {
		
		String escapedContent = HtmlUtils.htmlEscape(content);
		
		ItemComment comment = service.postCommentToItem(escapedContent, itemId, username);
		if (comment != null) {
			return new ResponseEntity(comment, HttpStatus.CREATED);
		} else {
			return new ResponseEntity(null, HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/{commentId}")
	public ResponseEntity editComment(@PathVariable long commentId, 
			@RequestBody String content) {
		String escapedContent = HtmlUtils.htmlEscape(content);
		
		ItemComment updateComment = service.updateComment(escapedContent, commentId);
		if (updateComment != null) {
			return new ResponseEntity(updateComment, HttpStatus.OK);
		} else {
			return new ResponseEntity(null, HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/{commentId}")
	public ResponseEntity deleteComment(@PathVariable long commentId) {
		boolean success = service.deleteComment(commentId);
		if (success) {
			return new ResponseEntity(true, HttpStatus.OK);
		} else {
			return new ResponseEntity(null, HttpStatus.NOT_FOUND);
		}
	}
	
}
