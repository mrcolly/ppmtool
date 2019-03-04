package com.mrcolly.ppmtool.security;


import com.google.gson.Gson;
import com.mrcolly.ppmtool.exceptions.ExceptionResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest httpServletRequest,
	                     HttpServletResponse httpServletResponse,
	                     AuthenticationException e) throws IOException, ServletException {
		Map<String, String> errors = new HashMap<>();
		errors.put("username", "invalid");
		errors.put("password", "invalid");
		ExceptionResponse loginResponse = new ExceptionResponse("login invalid", errors);
		String jsonResponse = new Gson().toJson(loginResponse);
		
		httpServletResponse.setContentType("application/json");
		httpServletResponse.setStatus(401);
		httpServletResponse.getWriter().print(jsonResponse);
	}
}
