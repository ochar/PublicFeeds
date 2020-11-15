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
package publicfeeds.application.internal.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import publicfeeds.interfaces.User;

/**
 * Spring Data JPA repository for accessing User type object.
 *
 * @author io
 */
public interface UserRepository extends JpaRepository<User, String> {
	
	/**
	 * Finds a User with username.
	 * 
	 * @param username username from User to be found.
	 * @return the User with given username, null if not found.
	 */
	User findByUsername(String username);
	
}
