package com.springboot.testbackend.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;

	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("SELECT ID, PASSWORD, ENABLED FROM USERS WHERE ID = ?")
				.authoritiesByUsernameQuery(
						"SELECT USERS.ID, ROLES.NAME FROM USERS_ROLES,USERS,ROLES WHERE USERS_ROLES.USER = USERS.ID AND USERS_ROLES.ROLE = ROLES.ID AND USERS.ID = ?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests().antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')");
		http.authorizeRequests().antMatchers("/user/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')");
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
		http.authorizeRequests().and().formLogin().loginProcessingUrl("/j_spring_security_login").loginPage("/login")
				.defaultSuccessUrl("/user").failureUrl("/login?message=error").usernameParameter("username")
				.passwordParameter("password").and().logout().logoutUrl("/j_spring_security_logout")
				.logoutSuccessUrl("/login?message=logout");
	}
}