package com.zensar.olx.jwt.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.zensar.olx.db.TokenStorage;
import com.zensar.olx.security.jwt.util.JwtUtil;

//this is the custom filter.
//you need to add this filter in spring security filter chain otherwise it is not executed.
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

	private String authHeader = "Authorization";
	private final String BEARER = "Bearer ";

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		JwtUtil jwtUtil = new JwtUtil();

		System.out.println("IN do filter internal");
		// 1. Check if user has passed token, we do that by fetching value from
		// authorization header.
		String authorizationHeaderValue = request.getHeader(authHeader);

		if (authorizationHeaderValue == null || !authorizationHeaderValue.startsWith(BEARER)) {
			chain.doFilter(request, response);
			return;
		}

		if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith(BEARER)) {
			String token = authorizationHeaderValue.substring(7).trim();
			System.out.println(token);

			String tokenExists = TokenStorage.getToken(token);

			if (tokenExists == null) {
				chain.doFilter(request, response);
				return;
			}

			try {
				// validate the token
				String encodedPayload = jwtUtil.validateToken(token);

				// if token is valid
				String payload = new String(Base64.getDecoder().decode(encodedPayload));

				// from this payload we need to fetch username
				JsonParser jsonParser = JsonParserFactory.getJsonParser();
				Map<String, Object> parseMap = jsonParser.parseMap(payload);
				String username = (String) parseMap.get("username");

				// create UsernamePasswordAuthenticationToken
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						username, null, AuthorityUtils.createAuthorityList("ROLE_USER"));

				// Authenticate user.
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			} catch (Exception e) {
				// if token is invalid
				e.printStackTrace();
			}
			// 2. if token not present ask to login
			// 3. If token present fetch and validate it
		}
		// }
		chain.doFilter(request, response);
	}

}
