/*
 * Copyright (C) 2020 io
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
import publicfeeds.application.Service;
import publicfeeds.domain.Item;
import publicfeeds.domain.ItemComment;

/**
 * Rest endpoints serving {@link ItemComment} type object.
 *
 * @author io
 */
@RequestMapping("/comment")
@RestController
public class CommentResource {
	
	@Autowired Service service;
	
	/**
	 * Get {@link ItemComment} from an {@link Item} which has given id.
	 *
	 * @param itemId a query parameter, id of Item which comment to be retrieved
	 * @return if Item is found, returns {@link ResponseEntity} with status 200
	 * OK and containing its ItemComment, otherwise status 404 NOT FOUND and 
	 * empty body
	 */
	@GetMapping("/item")
	public ResponseEntity getFromItemId(@RequestParam(name = "itemId") String itemId) {
		Optional<Item> foundItem = service.getItemById(itemId);
		if (foundItem.isPresent()) {
			return new ResponseEntity(service.getCommentsByItemId(foundItem.get().getId()), HttpStatus.OK);
		} else {
			return new ResponseEntity(new HashMap<String, ItemComment>(0), HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Get all {@link ItemComment} which has given username.
	 *
	 * @param username a query parameter, username which comments to be retrieved
	 * @return list of comments with given username, empty if not found
	 */
	@GetMapping("/user")
	public List<ItemComment> getFromUser(@RequestParam(name = "username") String username) {
		return service.getCommentsByUsername(username);
	}
	
	/**
	 * Creates a new comment for an {@link Item} which has given id.
	 *
	 * @param content a request body parameter, content of the comment to be
	 * created
	 * @param itemId a query parameter, id of item which the new comment is for
	 * @param username a query parameter, username which the comment is created
	 * for
	 * @return if corresponding Item is found and comment successfully created,
	 * returns {@link ResponseEntity} with status 200 OK and containing the
	 * ItemComment, otherwise status 404 NOT FOUND and empty body
	 */
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
	
	/**
	 * Updates a comment with given id.
	 *
	 * @param commentId a query parameter, id of the comment to be updated
	 * @param content a query parameter, new content of the comment
	 * @return if corresponding comment is found and comment successfully
	 * updated, returns {@link ResponseEntity} with status 200 OK and containing
	 * the ItemComment, otherwise status 404 NOT FOUND and empty body
	 */
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
	
	/**
	 * Deletes a comment with given id.
	 *
	 * @param commentId a query parameter, id of the comment to be deleted
	 * @return if corresponding comment is found and comment successfully
	 * deleted, returns {@link ResponseEntity} with status 200 OK and containing
	 * String true, otherwise status 404 NOT FOUND and empty body
	 */
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
