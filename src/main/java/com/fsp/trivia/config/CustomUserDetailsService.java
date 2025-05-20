package com.fsp.trivia.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.fsp.trivia.user.Users;
import com.fsp.trivia.user.UserRepository;



@Service
public class CustomUserDetailsService implements UserDetailsService {


	private final UserRepository userRepository;
	
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepository.findByUsername(username);
//		if (user == null) {
//			throw new UsernameNotFoundException("User not found");
//		} 
		System.out.println(user);
		return new  org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}

	
}
