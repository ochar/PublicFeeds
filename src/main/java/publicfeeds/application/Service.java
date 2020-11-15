/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.application;

import java.util.List;
import java.util.Optional;
import publicfeeds.domain.Item;
import publicfeeds.domain.ItemComment;
import publicfeeds.domain.ItemLike;
import publicfeeds.domain.UserEvent;

/**
 * Service layer. 
 * Contains main application logic.
 *
 * @author io
 */
public interface Service {
	
	/**
	 * Returns an {@link Item} which has given id.
	 * 
	 * @param id item id to look for, must not be null
	 * @return {@link Optional} containing {@code Item} with given id, or empty 
	 * {@code Optional} if not found
	 */
	Optional<Item> getItemById(String id);
	
	/**
	 * Returns all saved items.
	 * 
	 * @return all saved items
	 */
	List<Item> getAllItems();
	
	/**
	 * Returns number of saved items.
	 * 
	 * @return number of saved items.
	 */
	long countItems();
	
	/**
	 * Save an {@link Item}.
	 * 
	 * @param item item to be saved, must not be null
	 * @return {@code Item} that has been saved, may be modified during saving
	 * process 
	 * @throws IllegalArgumentException if item is null
	 */
	Item saveItem(Item item);
	
	/**
	 * Save all {@link Item} given.
	 * 
	 * @param items list of item to be saved, must not be null
	 * @return list of {@code Item} that has been saved; item may be modified 
	 * during saving process 
	 * @throws NullPointerException if items is null
	 */
	List<Item> saveItems(List<Item> items);
	
	/**
	 * Delete saved {@code Item}s with given ids.
	 * 
	 * @param itemIds list of item ids which to be deleted, must not be null
	 * @return {@code true} if operation was successful
	 * @throws NullPointerException if itemIds is null
	 */
	boolean deleteItemByIds(List<String> itemIds);
	
	/**
	 * Delete saved {@code Item}.
	 * 
	 * @param items list of items which to be deleted, must not be null and must
	 * not contains any null value
	 * @return {@code true} if operation was successful
	 * @throws NullPointerException if items is null or contains null value
	 */
	boolean deleteItems(List<Item> items);
	
	/**
	 * Delete all saved {@link Item}.
	 * 
	 * @return {@code true} if operation was successful
	 */
	boolean deleteAllItems();
	
	
	/**
	 * Returns all {@link Item} with given author id.
	 * 
	 * @param authorId id of author which item to be searched
	 * @return list of {@code Item} with given author id
	 */
	List<Item> getItemsByAuthor(String authorId);
	
	
	/**
	 * Returns likes of an {@link Item} with given id.
	 * 
	 * @param itemId id of item which likes to be retrieved
	 * @return list containing {@code ItemLike} of the item if found, 
	 * otherwise empty
	 */
	List<ItemLike> getLikesByItemId(String itemId);
	
	/**
	 * Returns item likes from the given username.
	 * 
	 * @param username username of user who owns the likes
	 * @return list containing {@code ItemLike} of the user if found, 
	 * otherwise empty
	 */
	List<ItemLike> getLikesByUsername(String username);
	
	/**
	 * Create a like from the username for an item with given id.
	 * 
	 * @param itemId id of item to be liked
	 * @param username username of user the like is form
	 * @return if {@code Item} is found, create a new {@code ItemLike} with the
	 * given username, or if a like by the username already exist, return it. 
	 * Return null if {@code Item} is not found
	 */
	ItemLike likeAnItem(String itemId, String username);
	
	/**
	 * Delete a like from the username for an item with given id.
	 * 
	 * @param itemId id of item to be unlike
	 * @param username username of user the like is form
	 * @return if {@code Item} is found, delete an existing {@code ItemLike} 
	 * with the given username then return true, else return {@code false} if 
	 * {@code Item} is not found
	 */
	boolean unlikeAnItem(String itemId, String username);
	
	
	/**
	 * Returns comments of an {@link Item} with given id.
	 * 
	 * @param itemId id of item which comments to be retrieved
	 * @return list containing {@code ItemComment} of the item if found, 
	 * otherwise empty
	 */
	List<ItemComment> getCommentsByItemId(String itemId);
	
	/**
	 * Returns item comments from the given username.
	 * 
	 * @param username username of user whose comments belong
	 * @return list containing {@code ItemComment} of the user if found, 
	 * otherwise empty
	 */
	List<ItemComment> getCommentsByUsername(String username);
	
	/**
	 * Creates a new comment by username for item with given id.
	 * 
	 * @param content content of the comment
	 * @param itemId id of item which the comment for
	 * @param username username of user the comment is form
	 * @return if {@code Item} is found, create a new {@code ItemComment} with 
	 * the given username and then return it, otherwise return null
	 */
	ItemComment postCommentToItem(String content, String itemId, String username);
	
	/**
	 * Update an existing item comment.
	 * 
	 * @param content new content of the comment to be saved
	 * @param commentId id of the comment to be edited
	 * @return if {@code ItemComment} is found, update and return it, otherwise
	 * return null
	 */
	ItemComment updateComment(String content, long commentId);
	
	/**
	 * Delete an existing item comment.
	 * 
	 * @param commentId id of the comment to be deleted
	 * @return if {@code ItemComment} is found and delete operation succeeded,
	 * return {@code true}
	 */
	boolean deleteComment(long commentId);
	
	
	/**
	 * Returns all {@code UserEvent} related to {@link Item} with given id.
	 * 
	 * @param itemId
	 * @return list containing {@code UserEvent} related to the {@code Item},
	 * empty if {@code Item} is not found
	 */
	List<UserEvent> getUserEventsByItemId(String itemId);
	
	/**
	 * Returns all {@code UserEvent} related to user with given username.
	 * 
	 * @param username username of the user which related events to be retrieved
	 * @return list containing {@code UserEvent} related to the user, empty if 
	 * {@code Item} is not found
	 */
	List<UserEvent> getUserEventsByUsername(String username);
	
}
