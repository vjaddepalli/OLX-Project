package com.zensar.olx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zensar.olx.jwt.filter.JWTAuthenticationFilter;



@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	//UserDeatilsService is an interface given by spring security
	//this interface has only one method loadUserByUserName()
	//this method is responsible for loading the user object from DB
	//if user object couldn't find in DB this method should throw exception.
	//It is responsiblity of developer to give implementation to this interface
	@Autowired
	private UserDetailsService userDetailsService;
	
	

	//HTTP-401 error=>wrong credentials by user
	//authentication
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(getPasswordEncoder());				
	}

	//HTTP-403(Forbidden) error=>User is authenticated but not authorized to access the resource
	//authorization
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
			.disable()
			.authorizeRequests()
			.antMatchers("/user/authenticate","/token/validate")
			.permitAll()	//This URL must be public so that user can login.
			.antMatchers(HttpMethod.OPTIONS,"/**")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.addFilter(new JWTAuthenticationFilter(authenticationManager()))
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);   // this is must for rest webservices
																		//REST shuld be stateless.
	}
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManager();
	}
	//following bean is used for password encoding
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
		return passwordEncoder;
	}
}
