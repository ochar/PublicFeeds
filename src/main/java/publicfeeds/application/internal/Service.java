/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.application.internal;

import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;
import org.springframework.beans.factory.annotation.Autowired;
import publicfeeds.application.internal.jpa.AuthorRepository;
import publicfeeds.application.internal.jpa.ItemRepository;
import publicfeeds.domain.Item;

/**
 *
 * @author io
 */
@org.springframework.stereotype.Service
public class Service {
	
	@Autowired private ItemRepository itemRepo;
	@Autowired private AuthorRepository authorRepo;
	
	
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
	
	
}
