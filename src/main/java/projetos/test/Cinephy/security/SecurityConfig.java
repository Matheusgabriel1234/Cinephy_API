package projetos.test.Cinephy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import projetos.test.Cinephy.repository.UserRepository;

@Component
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final UserRepository repository;
    private final JwtUtils util;



    public SecurityConfig( UserRepository repository, JwtUtils util, JwtUtils util1) {
        this.util = util1;
        this.repository = repository;

    }

    @Bean
    public SecurityFilterChain config (HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**","/api/movies/search").permitAll().anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(util,repository);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
