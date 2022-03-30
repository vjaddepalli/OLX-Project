package com.zensar.olxapigateway.filter;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;



/**
* GlobalFilter is an interface given by ApiGateway
* @author DC65846
*
*/

//@Configuration
public class CustomeFilter implements GlobalFilter{



/**
* Following method is an opportunity for us to do pre-processing
* This method automatically executes
*/
@Override
public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
System.out.println("In filter->Doing pre-processing");

ServerHttpRequest request=exchange.getRequest();
HttpHeaders headers=request.getHeaders();


List<String> list =headers.get("Authorization");
if(list!=null)
{
	String authorizationValaue=list.get(0);
	
	if(authorizationValaue==null) {
		ServerHttpResponse response=exchange.getResponse();
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		return response.setComplete();
		
	}
	
	
}
else {
	ServerHttpResponse response=exchange.getResponse();
	response.setStatusCode(HttpStatus.UNAUTHORIZED);
	return response.setComplete();
}

//after successful pre-processing this method must call filter() on chain obj
return chain.filter(exchange);
}

}