/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.application.internal;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;
import org.springframework.beans.factory.annotation.Autowired;
import publicfeeds.application.internal.jpa.AuthorRepository;
import publicfeeds.application.internal.jpa.ItemCommentRepository;
import publicfeeds.application.internal.jpa.ItemLikeReposistory;
import publicfeeds.application.internal.jpa.ItemRepository;
import publicfeeds.application.internal.jpa.UserEventRepository;
import publicfeeds.domain.Item;
import publicfeeds.domain.ItemComment;
import publicfeeds.domain.ItemLike;
import publicfeeds.domain.UserEvent;

/**
 *
 * @author io
 */
@org.springframework.stereotype.Service
public class Service {
	
	@Autowired private ItemRepository itemRepo;
	@Autowired private AuthorRepository authorRepo;
	@Autowired private ItemLikeReposistory likeRepo;
	@Autowired private ItemCommentRepository commentRepo;
	
	@Autowired private UserEventRepository eventRepo;
	
	
	public Optional<Item> getItemById(String id) {
		return itemRepo.findById(id);
	}
	
	public List<Item> getAllItems() {
		return itemRepo.findAll();
	}
	
	public long countItems() {
		return itemRepo.count();
	}
	
	public Item saveItem(Item item) {
		return itemRepo.save(item);
	}
	
	public List<Item> saveItems(List<Item> items) {
		return items.stream()
				.filter(item -> item != null)
				.map(itemRepo::save)
				.collect(toList());
	}
	
	public boolean deleteItemByIds(List<String> itemIds) {
		itemIds.stream()
				.filter(id -> id != null)
				.forEach(itemRepo::deleteById);
		return true;
	}
	
	public boolean deleteItems(List<Item> items) {
		itemRepo.deleteInBatch(items);
		return true;
	}
	
	public boolean deleteAllItems() {
		itemRepo.deleteAllInBatch();
		return true;
	}
	
	
	public List<Item> getItemsByAuthor(String authorId) {
		return itemRepo.findByAuthorId(authorId);
	}
	
	
	public List<ItemLike> getLikesByItemId(String itemId) {
		return likeRepo.findByItemId(itemId);
	}
	
	public List<ItemLike> getLikesByUsername(String username) {
		return likeRepo.findByUsername(username);
	}
	
	public ItemLike likeAnItem(String itemId, String username) {
		Optional<Item> foundItem = getItemById(itemId);
		
		if (foundItem.isPresent()) {
			List<ItemLike> foundLikes = likeRepo.findByItemId(foundItem.get().getId());
			Optional<ItemLike> foundAny = foundLikes.stream()
					.filter(like -> like.getUsername().equals(username))
					.findAny();
			
			if (foundAny.isPresent()) {
				return foundAny.get();
			} else {
				ItemLike saved = likeRepo.save(new ItemLike(foundItem.get(), username));
				createUserEvent(saved.getUsername(), saved.getItem().getId(), LIKE_EVENT);
				return saved;
			}
			
		} else {
			return null;
		}
	}
	
	public boolean unlikeAnItem(String itemId, String username) {
		Optional<Item> foundItem = getItemById(itemId);
		
		if (foundItem.isPresent()) {
			List<ItemLike> foundLikes = likeRepo.findByItemId(foundItem.get().getId());
			Optional<ItemLike> foundAny = foundLikes.stream()
					.filter(like -> like.getUsername().equals(username))
					.findAny();
			
			if (foundAny.isPresent()) {
				likeRepo.delete(foundAny.get());
				createUserEvent(username, itemId, UNLIKE_EVENT);
			}
			return true;
			
		} else {
			return false;
		}
	}
	
	public List<ItemComment> getCommentsByItemId(String itemId) {
		return commentRepo.findByItemId(itemId);
	}
	
	public List<ItemComment> getCommentsByUsername(String username) {
		return commentRepo.findByUsername(username);
	}
	
	public ItemComment postCommentToItem(String content, String itemId, String username) {
		Optional<Item> foundItem = getItemById(itemId);
		if (foundItem.isPresent()) {
			ItemComment ic = commentRepo.save(new ItemComment(content, foundItem.get(), username));
			createUserEvent(username, itemId, POST_COMMENT_EVENT);
			return ic;
		} else {
			return null;
		}
	}
	
	public ItemComment updateComment(String content, long commentId) {
		Optional<ItemComment> foundComment = commentRepo.findById(commentId);
		if (foundComment.isPresent()) {
			ItemComment ic = foundComment.get();
			ic.setContent(content);
			
			ItemComment saved = commentRepo.save(ic);
			createUserEvent(saved.getUsername(), saved.getItem().getId(), EDIT_COMMENT_EVENT);
			return saved;
			
		} else {
			return null;
		}
	}
	
	public boolean deleteComment(long commentId) {
		Optional<ItemComment> foundComment = commentRepo.findById(commentId);
		if (foundComment.isPresent()) {
			commentRepo.deleteById(commentId);
			
			ItemComment get = foundComment.get();
			createUserEvent(get.getUsername(), get.getItem().getId(), DELETE_COMMENT_EVENT);
			return true;
		} else {
			return false;
		}
	}
	
	
	private static final String LIKE_EVENT = " like item ";
	private static final String UNLIKE_EVENT = " unlike item ";
	private static final String POST_COMMENT_EVENT = " posted a comment on item ";
	private static final String EDIT_COMMENT_EVENT = " edited a comment on item ";
	private static final String DELETE_COMMENT_EVENT = " deleted a comment on item ";
	
	private UserEvent createUserEvent(String username, String itemId, String eventMessage) {
		return eventRepo.save(new UserEvent(Instant.now(), username,
				username + eventMessage + itemId, 
				itemId));
	}
	
	
	public List<UserEvent> getUserEventsByItemId(String itemId) {
		return eventRepo.findByItemId(itemId);
	}
	
	public List<UserEvent> getUserEventsByUsername(String username) {
		return eventRepo.findByUsername(username);
	}
	
}
