package com.my.tictactoe.web;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.my.tictactoe.model.AccountCredentials;
import com.my.tictactoe.model.User;
import com.my.tictactoe.model.UserRepository;
import com.my.tictactoe.service.AuthenticationService;

@RestController
public class UserController {
	private static final Logger log =
			 LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository urepository;
	
	@Autowired
	private AuthenticationService jwtService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials) {
		Optional<User> optUser = urepository.findByUsername(credentials.getUsername());
		
		UsernamePasswordAuthenticationToken creds;
		
		if (optUser.isPresent()) {
			creds = new UsernamePasswordAuthenticationToken(optUser.get().getUsername(), credentials.getPassword());
		} else {
			return new ResponseEntity<>("Bad credentials", HttpStatus.BAD_REQUEST);
		}
		
		Authentication auth = authenticationManager.authenticate(creds);
		
		String jwts = jwtService.getToken(auth.getName());
		
		Optional<User> currentUser = urepository.findByUsername(auth.getName());
		if (currentUser.isPresent()) {
			return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
					.header(HttpHeaders.ALLOW, currentUser.get().getRole())
					.header(HttpHeaders.HOST, currentUser.get().getId().toString())
					.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization, Allow", "Host").build();
		} else {
			return new ResponseEntity<>("Bad credentials", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping(value="/signup", method = RequestMethod.POST)
	public ResponseEntity<?> signUp(@RequestBody AccountCredentials credentials, HttpServletRequest request) {
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		String hashPwd = bc.encode(credentials.getPassword());
		
		if (urepository.findByUsername(credentials.getUsername()).isPresent()) {
			return new ResponseEntity<>("Username is already in use", HttpStatus.BAD_REQUEST);
		} else {
			User newUser = new User(credentials.getUsername(), hashPwd);
			urepository.save(newUser);
			
			UsernamePasswordAuthenticationToken creds = new UsernamePasswordAuthenticationToken(newUser.getUsername(), credentials.getPassword());
			
			Authentication auth = authenticationManager.authenticate(creds);
			
			String jwts = jwtService.getToken(auth.getName());
			
			return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
					.header(HttpHeaders.ALLOW, newUser.getRole())
					.header(HttpHeaders.HOST, newUser.getId().toString())
					.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization, Allow", "Host").build();
		}
	}
}
