package com.zensar.olx.securitty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import com.zensar.olx.bean.OLXUser;
import com.zensar.olx.db.OlxUserDAO;

@Service
public class OlxUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private OlxUserDAO repo;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// TODO Talk to DB and fetch user details
		System.out.println("IN loadUserByUsername");
		OLXUser foundUser=repo.findByUserName(userName);
		System.out.println(foundUser);
		
		if(foundUser==null) {
			throw new UsernameNotFoundException(userName);
		}
		System.out.println("IN loadUserByUsername");
		//UserDetails is an interface given by spring security
		//we are free to implement interface but for simplicity spring security has given a class
		//to implements UserDetailsInterface
		//name of the class is User
		//in this method we need to create object of User and return it.
		//if(username.equals("zensar")) {
		String roles=foundUser.getRoles();
		
		User authenticatedUser=new User(foundUser.getUserName(),foundUser.getPassword(),
				AuthorityUtils.createAuthorityList(roles));
		
		//this.service.addZensarUser(new ZensarUser("zensar",passwordEncoder.encode("zensar")));
		
		return authenticatedUser;
		//}
		//throw new UsernameNotFoundException(username);
	}

}
