/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.interfaces.rest;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import publicfeeds.domain.Item;

/**
 *
 * @author io
 */
@ApplicationScope
@Component
public class ItemCache {

	public int size() {
		return itemCacheMap.size();
	}
	
	public Item get(String id) {
		return itemCacheMap.get(id);
	}
	
	public List<Item> getAll(List<String> ids) {
		return ids.stream()
				.map(id -> itemCacheMap.get(id))
				.filter(item -> item != null)
				.collect(toList());
	}

	public boolean containsKey(String id) {
		return itemCacheMap.containsKey(id);
	}

	public boolean save(Item item) {
		itemCacheMap.put(item.getId(), item);
		return true;
	}
	
	public boolean saveAll(Collection<Item> items) {
		items.forEach(i -> {
			itemCacheMap.put(i.getId(), i);
		});
		return true;
	}

	public boolean remove(Item item) {
		return itemCacheMap.remove(item.getId(), item);
	}

	public void clear() {
		itemCacheMap.clear();
	}
	
	private final Map<String, Item> itemCacheMap;
	{
		itemCacheMap = new LinkedHashMap<String, Item>(5000) {
			@Override
			public Item put(String key, Item value) {

				if (this.containsKey(key)) {
					Item prevVal = this.remove(key);
					super.put(key, value);
					return prevVal;
					
				} else {
					return super.put(key, value);
				}
			}

			@Override
			protected boolean removeEldestEntry(Map.Entry<String, Item> eldest) {
				return this.size() > 10000;
			}
		};
	}
	
	
}
