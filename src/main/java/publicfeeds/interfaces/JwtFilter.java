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
package publicfeeds.interfaces;

import publicfeeds.application.internal.jpa.UserRepository;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author io
 */
@Component
public class JwtFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtBean jwtBean;
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private LoginBean loginBean;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = getToken(request);
		
		if (token != null) {
			try {
				String username = jwtBean.validateJWT(token);
				User foundUser = userRepo.findByUsername(username);
				if (foundUser != null) {
					loginBean.setUser(foundUser);
				}
			} catch (Exception e) {
			}
		}

		filterChain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header == null || header.trim().isEmpty() 
				|| header.startsWith("Bearer ") == false) {
			return null;
		}
		return header.replace("Bearer ", "");
	}
}
