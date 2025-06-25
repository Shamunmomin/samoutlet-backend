package com.sam.SamOutlet.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sam.SamOutlet.filter.JwtRequestFilter;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration  {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	

	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    try {
	        return http
	                .csrf(csrf -> csrf.disable())
	                .authorizeHttpRequests(auth -> auth
	                        .requestMatchers("/authenticate", "/signup","/user/**","/uploads/**").permitAll()     // Public access
	                        .requestMatchers("/admin/**").permitAll()             // Admin-only access
	                        .anyRequest().authenticated()                              // All other requests need authentication
	                )
	                .httpBasic(Customizer.withDefaults())
	                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
	                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .build();

	    } catch (Exception e) {
	        System.err.println("Error configuring SecurityFilterChain: " + e.getMessage());
	        e.printStackTrace();
	        throw new RuntimeException("Security configuration failed", e);
	    } finally {
	        System.out.println("SecurityFilterChain method executed.");
	    }
	}

	 
	    
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
}
