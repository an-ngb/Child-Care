package com.example.demo.configs;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.utils.Payload;
import com.example.demo.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RequiredArgsConstructor
public class JwtHandlerFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        if (request.getHeader("Authorization") == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("Authorization");
        assert token.startsWith("Bearer");
        token = token.split(" ")[1];
        try {
            Properties prop = Utils.loadProperties("jwt.setting.properties");
            assert prop != null;
            String secret = prop.getProperty("key");
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            Map<String, Claim> tokenClaims = decodedJWT.getClaims();
            assert tokenClaims != null;
            String email = tokenClaims.get("sub").asString();
            Map<String, Object> payloadMap = tokenClaims.get("user").asMap();
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            Payload payload = new Payload(Long.valueOf(payloadMap.get("id").toString()), (String) payloadMap.get("email"), (String) payloadMap.get("role"));
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, payload, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException | JWTDecodeException | UsernameNotFoundException e) {
            Map<String, String> message = new HashMap<>();
            message.put("errorCode", "401 - UNAUTHORIZED");
            message.put("message", e.getMessage());
            response.getWriter().write(String.valueOf(message));
        }
    }
}