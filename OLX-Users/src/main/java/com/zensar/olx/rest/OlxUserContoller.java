package com.zensar.olx.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.zensar.olx.bean.LoginResponse;
import com.zensar.olx.bean.LoginUser;
import com.zensar.olx.bean.OLXUser;
import com.zensar.olx.db.TokenStorage;
import com.zensar.olx.security.jwt.util.JwtUtil;
import com.zensar.olx.service.OlxUserService;

@RestController
public class OlxUserContoller {

	@Autowired
	OlxUserService olxUserService;

	@Autowired
	AuthenticationManager manager;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * This is the rest specification for authentication token for user details
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/user/authenticate")
	public LoginResponse login(@RequestBody LoginUser user) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				user.getUserName(), user.getPassword());

		System.out.println(user.getUserName());
		System.out.println(user.getPassword());

		try {
			manager.authenticate(authenticationToken);
			String token = jwtUtil.generateToken(user.getUserName());
			LoginResponse userResponse = new LoginResponse();
			userResponse.setJwt(token);
			//Use map for store token
			TokenStorage.storeToken(token, token);
			return userResponse;
		} catch (Exception e) {
			throw e;
		}

	}

	@PostMapping("/user")
	public OLXUser addOlxUser(@RequestBody OLXUser olxUser) {
		return this.olxUserService.addOlxUser(olxUser);
	}

	@GetMapping("/user/{uid}")
	public OLXUser findOlxUserById(@PathVariable(name = "uid") int id) {
		return this.olxUserService.findOlxUser(id);
	}

	@GetMapping("/user/find/{userName}")
	public OLXUser findOlxUserByName(@PathVariable(name = "userName") String name) {
		return this.olxUserService.findOlxUserByName(name);
	}
	
	@GetMapping("/token/validate")
	public ResponseEntity<Boolean> isValidateUser(@RequestHeader("Authorization") String authToken) {
		

		try {
			String  validateToken=jwtUtil.validateToken(authToken.substring(7));
			return new ResponseEntity<Boolean>(true,HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@DeleteMapping("/user/logout")
	public ResponseEntity<Boolean> logout(@RequestHeader("Authorization") String authToken){
		
		String token=authToken.substring(7);
		
		try {
			
			TokenStorage.removeToken(token);
			
			ResponseEntity<Boolean> responseEntity=new ResponseEntity<Boolean>(true,HttpStatus.OK);
			return responseEntity; 
			
		} catch (Exception e) {
			
			ResponseEntity<Boolean> responseEntity=new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
			return responseEntity;
		}
		
	}

}
