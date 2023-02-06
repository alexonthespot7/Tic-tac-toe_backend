package com.my.tictactoe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.my.tictactoe.model.User;
import com.my.tictactoe.model.UserRepository;

@SpringBootApplication
public class TictactoeApplication {
	
	@Autowired
	private UserRepository urepository;

	public static void main(String[] args) {
		SpringApplication.run(TictactoeApplication.class, args);
	}

}
