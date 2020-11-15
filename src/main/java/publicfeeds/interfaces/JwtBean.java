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

import com.auth0.jwt.JWT;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author io
 */
@Component
public class JwtBean {
	
	@Value("${jwtSecret:973rerjvn[]=<>?}")
	private String TOKEN_SECRET = "973rerjvn[]=<>?";
	
	@Value("${jwtExprTime:7200000}")
	private long TOKEN_EXPR_TIME = 2 * 60 * 60 * 1000;
	
	
	public String createJWT(String username) {
		return JWT.create()
				.withSubject(username)
				.withIssuedAt(Date.from(Instant.now()))
				.withExpiresAt(Date.from(Instant.now().plusMillis(TOKEN_EXPR_TIME)))
				.sign(HMAC512(TOKEN_SECRET));
	}
	
	public String validateJWT(String token) {
//		try {
			DecodedJWT verified = JWT.require(HMAC512(TOKEN_SECRET)).build()
					.verify(token);
			return verified.getSubject();
//		} catch (TokenExpiredException ex) {
//
//		} catch (SignatureVerificationException ex) {
//
//		} catch (JWTVerificationException ex) {
//
//		}
	}
	
}
