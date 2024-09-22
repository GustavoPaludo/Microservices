package com.service.product.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.service.product.ApplicationConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.ArrayList;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final Key signingKey;

	@Autowired
	public JwtAuthenticationFilter() {
		this.signingKey = generateKey(ApplicationConstants.JWT_KEY);
	}

	@Override
	protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
			throws jakarta.servlet.ServletException, IOException {

		String token = getJwtFromRequest(request);

	    if (StringUtils.hasText(token)) {
	        Claims claims = validateJwt(token);

	        if (claims != null) {
	            String username = claims.getSubject();

				UserDetails userDetails = createDummyUser(username);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

	            SecurityContextHolder.getContext().setAuthentication(authentication);
	        }
	    } else {
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.getWriter().write("Invalid or missing JWT token");
	        return;
	    }

	    filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(jakarta.servlet.http.HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public Claims validateJwt(String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}
	}

	private Key generateKey(String baseValue) {
		try {
			byte[] salt = "fixed-salt".getBytes();
			PBEKeySpec spec = new PBEKeySpec(baseValue.toCharArray(), salt, 65536, 256);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			byte[] key = factory.generateSecret(spec).getEncoded();
			return Keys.hmacShaKeyFor(key);
		} catch (Exception e) {
			throw new RuntimeException("Error generating JWT key", e);
		}
	}

	private UserDetails createDummyUser(String username) {
		return new org.springframework.security.core.userdetails.User(username, "", new ArrayList<>());
	}
}
