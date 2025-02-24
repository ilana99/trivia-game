package com.fsp.trivia.authentication;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fsp.trivia.config.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BearerTokenAuthFilter extends OncePerRequestFilter {

	private JWTService jwtService;

	private CustomUserDetailsService customUserDetailsService;

	public BearerTokenAuthFilter(JWTService jwtService, CustomUserDetailsService customUserDetailsService) {
		this.jwtService = jwtService;
		this.customUserDetailsService = customUserDetailsService;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {


		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			Optional<Cookie> jwtCookie = Arrays.stream(cookies)
					.filter(cookie -> "jwt".equals(cookie.getName()))
					.findFirst();

			if (jwtCookie.isPresent()) {
				String jwt = jwtCookie.get().getValue();

				if (jwtService.validateToken(jwt)) {
					String username = jwtService.checkUsername(jwt);
					UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
					String name = userDetails.getUsername();
					if (username.equals(name)) {
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								username, null, null);
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}
			}

		}

		filterChain.doFilter(request, response);
	}

}
