package com.fsp.trivia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fsp.trivia.authentication.BearerTokenAuthFilter;
import com.fsp.trivia.authentication.JWTService;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private CustomUserDetailsService customUserDetailsService;
	private final JWTService jwtService;


	public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, JWTService jwtService) {
		this.customUserDetailsService = customUserDetailsService;
		this.jwtService = jwtService;
	}



	@Bean
	BearerTokenAuthFilter bearerTokenAuthFilter() {
		return new BearerTokenAuthFilter(jwtService, customUserDetailsService);
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/login", "/register").permitAll()
				.anyRequest().authenticated()
				)
				.csrf((csrf) -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.httpBasic(withDefaults())

				.addFilterAfter(bearerTokenAuthFilter(), BasicAuthenticationFilter.class);
		return http.build();
	}



	@Bean
	AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(customUserDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());

		ProviderManager providerManager = new ProviderManager(authenticationProvider);
		providerManager.setEraseCredentialsAfterAuthentication(false);

		return providerManager;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
	@Bean
	UrlBasedCorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
	    configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
	    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "Access-Control-Allow-Origin"));
	    configuration.setAllowCredentials(true);
	    configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}
}
