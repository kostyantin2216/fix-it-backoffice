/**
 * 
 */
package com.fixit.bo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/22 19:54:56 GMT+3
 */
@Configuration
@EnableWebSecurity
public class BackOfficeSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
          .withUser("fixxit").password("2r5eH0kEk3").roles("USER");
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable();
    	http.headers().frameOptions().sameOrigin();
    	http.authorizeRequests()
        	.antMatchers("/resources/**").permitAll()
            .anyRequest().authenticated()
            .and()
        .formLogin();
    }
    
}
