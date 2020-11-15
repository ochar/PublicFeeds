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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import publicfeeds.domain.Item;

/**
 * Cache for feed item. Used for selecting items to save.
 * Collection is backed by {@link LinkedHashedMap} which acts as an LRU cache.
 * 
 * @see LinkedHashMap
 * @author io
 */
@ApplicationScope
@Component
public class ItemCache {

	/**
	 * Returns the number of items in this cache.
	 *
	 * @return the number of items in this cache
	 * @see LinkedHashMap#size
	 */
	public int size() {
		return itemCacheMap.size();
	}
	
	/**
	 * Returns an {@link Item} from this cache which has given id.
	 *
	 * @param id id of Item to look for
	 * @return the item in this cache which has given id, or {@code null} if not
	 * found
	 */
	public Item get(String id) {
		return itemCacheMap.get(id);
	}
	
	/**
	 * Returns list of {@link Item} from this cache which has id matching one of
	 * the given ids.
	 *
	 * @param ids list of ids of Item to look for
	 * @return the items in this cache which has given id, or an empty list if
	 * none is found
	 */
	public List<Item> getAll(List<String> ids) {
		return ids.stream()
				.map(id -> itemCacheMap.get(id))
				.filter(item -> item != null)
				.collect(toList());
	}

	/**
	 * Returns {@code true} if this cache contains an item which has the given
	 * id.
	 *
	 * @param id id of item whose presence in this cache is to be tested
	 * @return {@code true} if this cache contains an item which has the given
	 * id, {@code false} otherwise
	 */
	public boolean containsKey(String id) {
		return itemCacheMap.containsKey(id);
	}

	/**
	 * Save an item to this cache. If this cache already contains an item with
	 * same id, it is replaced.
	 *
	 * @param item item to be saved
	 * @return {@code true} if the item is successfully saved to the cache
	 */
	public boolean save(Item item) {
		itemCacheMap.put(item.getId(), item);
		return true;
	}
	
	/**
	 * Save all of items to this cache. If this cache already contains any item
	 * with the same id, it will be replaced.
	 *
	 * @param items items to be saved in this cache
	 * @return {@code true} if any of the items is successfully saved to the
	 * cache
	 * @throws NullPointerException if the specified items is null
	 */
	public boolean saveAll(Collection<Item> items) {
		items.forEach(i -> {
			itemCacheMap.put(i.getId(), i);
		});
		return true;
	}

	/**
	 * Removes an item from this cache.
	 *
	 * @param item item to be removed
	 * @return {@code true} if the item is found and removed
	 */
	public boolean remove(Item item) {
		return itemCacheMap.remove(item.getId(), item);
	}

	/**
	 * Removes all items in this cache.
	 */
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
