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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import publicfeeds.application.Service;
import publicfeeds.domain.Item;
import publicfeeds.domain.ItemComment;
import publicfeeds.domain.UserEvent;

/**
 * Rest endpoints serving {@link UserEvent} type object.
 *
 * @author io
 */
@RequestMapping("/user-event")
@RestController
public class UserEventResource {
	
	@Autowired Service service;
	
	
	/**
	 * Get {@link UserEvent} from an {@link Item} which has given id.
	 *
	 * @param itemId a query parameter, id of Item which event to be retrieved
	 * @return if Item is found, returns {@link ResponseEntity} with status 200
	 * OK and containing its related UserEvent, otherwise status 404 NOT FOUND
	 * and empty body
	 */
	@GetMapping("/item")
	public ResponseEntity getFromItemId(@RequestParam(name = "itemId") String itemId) {
		Optional<Item> foundItem = service.getItemById(itemId);
		if (foundItem.isPresent()) {
			return new ResponseEntity(service.getUserEventsByItemId(foundItem.get().getId()), HttpStatus.OK);
		} else {
			return new ResponseEntity(new HashMap<String, ItemComment>(0), HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Get all {@link UserEvent} from given username.
	 *
	 * @param username a query parameter, username which events to be retrieved
	 * @return list of events with given username, empty if not found
	 */
	@GetMapping("/user")
	public List<UserEvent> getFromUser(@RequestParam(name = "username") String username) {
		return service.getUserEventsByUsername(username);
	}
	
}
