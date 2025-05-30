package com.example.shopapp.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Override// như thế nào thì cho đi qua, như thể nào thì kiểm tra
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        if(isBypassToken(request)){
            filterChain.doFilter(request,response); // enable bypass: ai cũng cho đi qua
        }
            //filterChain.doFilter(request,response); // enable bypass: ai cũng cho đi qua
    }
    private  boolean isBypassToken(@NotNull HttpServletRequest request){
        final List<Pair<String, String>> bypassTokens= Arrays.asList(
                Pair.of("/api/v1/products","GET"),
                Pair.of("/api/v1/categories","GET"),
                Pair.of("/api/v1/users/register","POST"),
                Pair.of("/api/v1/user/login","POST")

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
