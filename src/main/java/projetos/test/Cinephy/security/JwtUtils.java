package projetos.test.Cinephy.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import projetos.test.Cinephy.repository.UserRepository;

import java.security.Key;
import java.util.Date;


@Component
public class JwtUtils {

 private final static Logger logger = LoggerFactory.getLogger(JwtUtils.class);


private final Key key;



private final long expiration;

    public JwtUtils( @Value("${jwt.secret}") String jwtSecret ,@Value("${jwt.validation}") long expiration) {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.expiration = expiration;
    }



    public String generateToken(UserDetails userDetails){
     String token  = Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime() + expiration))
            .signWith(key, SignatureAlgorithm.HS256).compact();
     logger.debug("Generated JWT token for user: {}", userDetails.getUsername());
     return token;
}

     public String getUserFromToken(String token){
        String username = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
                logger.debug("Extracted username '{}' from JWT token", username);
                return username;
     }

     public boolean validateToken(String authToken){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken);
                    logger.info("Jwt Token is valid");
                    return true;
        }catch(SecurityException err){
            logger.error("Assinatura jwt Invalida: '{}' ",err.getMessage());
        } catch (MalformedJwtException e){
            logger.error("Token jwt malformado '{}'",e.getMessage());
        }catch(ExpiredJwtException err){
            logger.error("Token jwt ja expirou '{}' ",err.getMessage());
        }catch(IllegalArgumentException err){
            logger.error("Reivindicação jwt invalida '{}'",err.getMessage());
        }catch(UnsupportedJwtException err){
            logger.error("Token jwt não suportado '{}'",err.getMessage());
        }

        return false;
     }

}
