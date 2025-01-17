package projetos.test.Cinephy.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import java.io.IOException;
import java.util.ArrayList;


public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;
    private final UserRepository repository;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserRepository repository) {
        this.jwtUtils = jwtUtils;
        this.repository = repository;
    }

    @Override
    protected  void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        String path = req.getRequestURI();

        if(path.startsWith("/api/auth") || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/webjars") || path.startsWith("/actuator")){
            filterChain.doFilter(req,res);
            return;
        }


        try {
            String jwt = parseJwt(req);
            if(jwt != null && jwtUtils.validateToken(jwt)){
                logger.debug("Token jtw encontrado {}",jwt);
                String email = jwtUtils.getUserFromToken(jwt);
                UserEntity user = repository.findUserByEmail(email);

                if(user == null){
                    throw new RuntimeException("Ocorreu um erro na autenticação do email");
                }

                UserDetails details = User.withUsername(user.getEmail()).password(user.getPassword()).authorities(new ArrayList<>())
                        .build();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(details,null,details.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.info("Email {} autenticado com sucesso",email);
            }
        } catch(Exception e) {
            logger.error("Houve um erro na autenticação", e);
            return;
        }

        filterChain.doFilter(req,res);
    }




    private String parseJwt(HttpServletRequest request){
        String header = request.getHeader("Authorization");

        if(StringUtils.hasText(header) && header.startsWith("Bearer ")){
           String token = header.substring(7);
           logger.debug("Authorization Header received: {}",token);
           return token;

        }

        return null;

} }

