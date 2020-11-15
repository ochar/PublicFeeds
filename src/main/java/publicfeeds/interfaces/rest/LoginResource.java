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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import publicfeeds.interfaces.JwtBean;
import publicfeeds.interfaces.LoginBean;
import publicfeeds.application.internal.jpa.UserRepository;
import publicfeeds.interfaces.User;

/**
 *
 * @author io
 */
@RestController
public class LoginResource {
	
	@Autowired
	private LoginBean loginBean;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JwtBean jwtBean;
	
	
	/**
	 * Checks if current session has user logged in
	 *
	 * @return if currently there user is logged in return true, otherwise false
	 */
	private boolean isLoggedIn() {
		return loginBean.getUser() != null;
	}
	
	@GetMapping("/user")
	public ResponseEntity getUser() {
		if (loginBean.isLoggedIn()) {
			return new ResponseEntity(loginBean.getUser(), HttpStatus.OK);
		} else {
			return new ResponseEntity(Collections.EMPTY_LIST, HttpStatus.OK);
		}
	}
	
	
	@GetMapping("/login/anon")
	public ResponseEntity loginAs() {
		if (isLoggedIn()) {
			return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
		} else {
			String username = "anon" + ThreadLocalRandom.current().nextInt(100000);
			while (userRepo.findByUsername(username) != null) {
				username = "anon" + ThreadLocalRandom.current().nextInt(100000);
			}
			User user = new User(username);
			userRepo.save(user);
			
			String token = jwtBean.createJWT(username);
			
			Map resp = new HashMap<String, String>(1);
			resp.put("token", token);
			
			return new ResponseEntity(resp, HttpStatus.CREATED);
		}
	}
	
	@GetMapping("/login")
	public ResponseEntity loginAs(@RequestParam(name = "username") String username) {
		if (isLoggedIn()) {
			return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
		} else {
			if (userRepo.findByUsername(username) != null) {
				return new ResponseEntity(null, HttpStatus.CONFLICT); 
			} else {
				User user = new User(username);
				userRepo.save(user);
				
				String token = jwtBean.createJWT(username);

				Map resp = new HashMap<String, String>(1);
				resp.put("token", token);

				return new ResponseEntity(resp, HttpStatus.CREATED);
			}
		}
	}
	
	@GetMapping("/logout")
	public ResponseEntity logout() {
		User user = loginBean.getUser();
		if (user != null) {
			loginBean.setUser(null);
			return new ResponseEntity(true, HttpStatus.OK);
		} else {
			return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
		}
	}
	
}
