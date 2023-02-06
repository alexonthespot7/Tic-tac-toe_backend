package com.my.tictactoe;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.my.tictactoe.model.User;
import com.my.tictactoe.model.UserRepository;
import com.my.tictactoe.service.AuthenticationService;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private AuthenticationService jwtService;
	
	@Autowired
	private UserRepository urepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		String jws = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if (jws != null) {
			String user = jwtService.getAuthUser(request);
			
			Optional<User> curruser = urepository.findByUsername(user);
			
			Authentication authentication;
			
			if (curruser.isPresent()) {
				MyUserDetails myUser = new MyUserDetails(curruser.get().getId(), user, curruser.get().getPasswordHash(), true, true, true, true, AuthorityUtils.createAuthorityList(curruser.get().getRole()));
				authentication = new UsernamePasswordAuthenticationToken(myUser, null, AuthorityUtils.createAuthorityList(curruser.get().getRole()));
			} else {
				authentication = new UsernamePasswordAuthenticationToken(12, null, Collections.emptyList());
			}
			
			SecurityContextHolder.getContext().setAuthentication(authentication);

		}
		filterChain.doFilter(request, response);
	}
}
