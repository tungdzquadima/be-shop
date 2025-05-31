package com.example.shopapp.filters;

import com.example.shopapp.components.JwtTokenUntil;
import com.example.shopapp.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUntil jwtTokenUntil;
    @Override// như thế nào thì cho đi qua, như thể nào thì kiểm tra
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        try{
            if(isBypassToken(request)){
                filterChain.doFilter(request,response);// enable bypass: ai cũng cho đi qua
                return;
            }

            final String authHeader = request.getHeader("authorization");

            if(authHeader==null || !authHeader.startsWith("Bearer ")){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthoried");
                return;
            }

            final String token=authHeader.substring(7);
            final String phoneNumber=jwtTokenUntil.extractPhoneNumber(token);
            if(phoneNumber != null  && SecurityContextHolder.getContext().getAuthentication()==null){
                User userDetails=(User) userDetailsService.loadUserByUsername(phoneNumber);
                if(jwtTokenUntil.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,
                            null,
                            userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request,response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorrized");// nếu token sai thì hiện lỗi 401
        }


            //filterChain.doFilter(request,response); // enable bypass: ai cũng cho đi qua

    }
    private  boolean isBypassToken(@NotNull HttpServletRequest request){
        final List<Pair<String, String>> bypassTokens= Arrays.asList(
                Pair.of("/api/v1/products","GET"),
                Pair.of("/api/v1/categories","GET"),
                Pair.of("/api/v1/users/register","POST"),
                Pair.of("/api/v1/users/login","POST")

        );
        for(Pair<String, String > bypassToken: bypassTokens){
            if (request.getServletPath().contains(bypassToken.getLeft())// getFirst
                    && request.getMethod().equals(bypassToken.getRight())){//getSecond
                return true;
            }
        }
        return false;
    }

}
