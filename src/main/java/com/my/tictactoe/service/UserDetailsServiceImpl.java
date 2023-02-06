package com.my.tictactoe.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.my.tictactoe.MyUserDetails;
import com.my.tictactoe.model.User;
import com.my.tictactoe.model.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository urepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = urepository.findByUsername(username);
		
		MyUserDetails myUser = null;
		
		if (user.isPresent()) {
			User currentUser = user.get();
			
			myUser = new MyUserDetails(currentUser.getId(), username,
					currentUser.getPasswordHash(), true, true, true, true,
					AuthorityUtils.createAuthorityList(currentUser.getRole()));
			
		} else {
			throw new UsernameNotFoundException("User not found");
		}
		
		return myUser;
	}
}
