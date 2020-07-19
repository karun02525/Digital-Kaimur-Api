package com.digital.kaimur.security;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtTokenFilter jwtFilter;
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
	 
		http.csrf().disable().authorizeRequests()
		.antMatchers("/authenticate/login").permitAll()
		.antMatchers("/authenticate/get-user/**").permitAll()
		.antMatchers("/authenticate/register").permitAll()
		.antMatchers("/authenticate/forgot_password").permitAll()
	//	.antMatchers("/authenticate/register-device").permitAll()
		.antMatchers("/admin/**").permitAll()
		.antMatchers("/category/**").permitAll()
		.antMatchers("/store/all-store/**").permitAll()
		.antMatchers("/authenticate/image-profile/**").permitAll()
		.antMatchers("/store/image-store/**").permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.exceptionHandling()
		.authenticationEntryPoint((request, response, e) -> {
	        response.setContentType("application/json;charset=UTF-8");
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        try {
				response.getWriter().write(new JSONObject()
				        .put("status",false)
				        .put("message", "401 Unauthorized")
				        .toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
	    })
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
	  
    web.ignoring().antMatchers("/v2/api-docs")//
        .antMatchers("/swagger-resources/**")//
        .antMatchers("/swagger-ui.html")//
        .antMatchers("/configuration/**")//
        .antMatchers("/webjars/**")//
        .antMatchers("/public")
        .and()
        .ignoring()
        .antMatchers("/h2-console/**/**");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

}
