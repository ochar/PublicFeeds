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
	
	
	Optional<Item> getItemById(String id);
	
	List<Item> getAllItems();
	
	long countItems();
	
	Item saveItem(Item item);
	
	List<Item> saveItems(List<Item> items);
	
	boolean deleteItemByIds(List<String> itemIds);
	
	boolean deleteItems(List<Item> items);
	
	boolean deleteAllItems();
	
	
	List<Item> getItemsByAuthor(String authorId);
	
	
	List<ItemLike> getLikesByItemId(String itemId);
	
	List<ItemLike> getLikesByUsername(String username);
	
	ItemLike likeAnItem(String itemId, String username);
	
	boolean unlikeAnItem(String itemId, String username);
	
	
	List<ItemComment> getCommentsByItemId(String itemId);
	
	List<ItemComment> getCommentsByUsername(String username);
	
	ItemComment postCommentToItem(String content, String itemId, String username);
	
	ItemComment updateComment(String content, long commentId);
	
	boolean deleteComment(long commentId);
	
	
	List<UserEvent> getUserEventsByItemId(String itemId);
	
	List<UserEvent> getUserEventsByUsername(String username);
	
}
