package com.my.tictactoe.web;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.my.tictactoe.MyUserDetails;
import com.my.tictactoe.model.User;
import com.my.tictactoe.model.UserRepository;

@CrossOrigin(origins = "*")
@RestController
public class MainController {
	private static final Logger log =
			 LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private UserRepository urepository;
	
	@RequestMapping("/users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public @ResponseBody List<User> usersRest() {
		return (List<User>) urepository.findAll();
	}
	
	@RequestMapping(value="/api/addResult", method=RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> addResult(@RequestBody String result, Authentication auth) {
		log.info(auth.getPrincipal().getClass().toString());
		if (auth.getPrincipal().getClass().toString().equals("class com.my.tictactoe.MyUserDetails")) {
			MyUserDetails myUser = (MyUserDetails) auth.getPrincipal();
			Optional<User> optUser = urepository.findByUsername(myUser.getUsername());
			
			if (optUser.isPresent()) {
				User user = optUser.get();
				int games;
								
				if (result.equals("\"Crosses\"")) {
					games = user.getCross_wins();
					user.setCross_wins(games + 1);
				} else if (result.equals("\"Noughts\"")) {
					games = user.getNought_wins();
					user.setNought_wins(games + 1);
				} else {
					games = user.getDraws();
					user.setDraws(games + 1);
				}
				urepository.save(user);
				
				return new ResponseEntity<>("Adding the results went ok", HttpStatus.OK);
			} else { 
				return new ResponseEntity<>("Authentication problems", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>("Authentication problems", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/api/addBotResult", method=RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> addBotResult(@RequestBody String result, Authentication auth) {
		if (auth.getPrincipal().getClass().toString().equals("class com.my.tictactoe.MyUserDetails")) {
			MyUserDetails myUser = (MyUserDetails) auth.getPrincipal();
			Optional<User> optUser = urepository.findByUsername(myUser.getUsername());
			
			if (optUser.isPresent()) {
				User user = optUser.get();
				int games;
				
				log.info(result);
				
				if (result.equals("\"Bot\"")) {
					games = user.getBot_wins();
					user.setBot_wins(games + 1);
				} else if (result.equals("\"User\"")) {
					games = user.getUser_wins();
					user.setUser_wins(games + 1);
				} else {
					games = user.getBot_draws();
					user.setBot_draws(games + 1);
				}
				urepository.save(user);
				
				return new ResponseEntity<>("Adding the results went ok", HttpStatus.OK);
			} else { 
				return new ResponseEntity<>("Authentication problems", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>("Authentication problems", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/getResults/{userid}")
	@PreAuthorize("authentication.getPrincipal().getId() == #userId")
	public @ResponseBody User getResults(@PathVariable("userid") Long userId) {
		Optional<User> optUser = urepository.findById(userId);
		if (optUser.isPresent()) {
			return optUser.get();
		} else {
			return null;
		}
	}
	
	@RequestMapping(value="/api/resetStat/{userid}")
	@PreAuthorize("authentication.getPrincipal().getId() == #userId")
	public ResponseEntity<?> resetStatById(@PathVariable("userid") Long userId) {
		Optional<User> optUser = urepository.findById(userId);
		if (optUser.isPresent()) {
			User user = optUser.get();
			user.setBot_draws(0);
			user.setBot_wins(0);
			user.setUser_wins(0);
			user.setDraws(0);
			user.setCross_wins(0);
			user.setNought_wins(0);
			urepository.save(user);
			
			return new ResponseEntity<>("The statistics of the user was reset", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("The user does not exist", HttpStatus.BAD_REQUEST);
		}
	}
}
