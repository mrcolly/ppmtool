package com.mrcolly.ppmtool.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		securedEnabled = true,
		jsr250Enabled = true,
		prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	private JwtAuthEntryPoint unauthorizedHandler;
	
	public SecurityConfiguration(JwtAuthEntryPoint unauthorizedHandler) {
		this.unauthorizedHandler = unauthorizedHandler;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers(
					"/",
					"/favicon.ico",
					"/**/*.html",
					"/**/*.css",
					"/**/*.js",
					"/**/*.png",
					"/**/*.jpg"
			).permitAll()
			.antMatchers("/api/users/**").permitAll()
			.anyRequest().authenticated();
	}
}
