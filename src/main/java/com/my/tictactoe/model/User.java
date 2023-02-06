package com.my.tictactoe.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;
	
	@Column
	private String username;
	
	@Column
	private String passwordHash;
	
	@Column
	private int nought_wins;
	
	@Column
	private int cross_wins;
	
	@Column
	private int draws;
	
	@Column
	private int bot_wins;
	
	@Column
	private int user_wins;
	
	@Column
	private int bot_draws;
	
	@Column
	private String role;
	
	public User() {}

	public User(String username, String passwordHash, int nought_wins, int cross_wins, int draws, int bot_wins,
			int user_wins, int bot_draws, String role) {
		super();
		this.username = username;
		this.passwordHash = passwordHash;
		this.nought_wins = nought_wins;
		this.cross_wins = cross_wins;
		this.draws = draws;
		this.bot_wins = bot_wins;
		this.user_wins = user_wins;
		this.bot_draws = bot_draws;
		this.role = role;
	}

	public User(String username, String passwordHash) {
		super();
		this.username = username;
		this.passwordHash = passwordHash;
		this.nought_wins = 0;
		this.cross_wins = 0;
		this.draws = 0;
		this.bot_wins = 0;
		this.user_wins = 0;
		this.bot_draws = 0;
		this.role = "USER";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public int getNought_wins() {
		return nought_wins;
	}

	public void setNought_wins(int nought_wins) {
		this.nought_wins = nought_wins;
	}

	public int getCross_wins() {
		return cross_wins;
	}

	public void setCross_wins(int cross_wins) {
		this.cross_wins = cross_wins;
	}

	public int getDraws() {
		return draws;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}

	public int getBot_wins() {
		return bot_wins;
	}

	public void setBot_wins(int bot_wins) {
		this.bot_wins = bot_wins;
	}

	public int getUser_wins() {
		return user_wins;
	}

	public void setUser_wins(int user_wins) {
		this.user_wins = user_wins;
	}

	public int getBot_draws() {
		return bot_draws;
	}

	public void setBot_draws(int bot_draws) {
		this.bot_draws = bot_draws;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
