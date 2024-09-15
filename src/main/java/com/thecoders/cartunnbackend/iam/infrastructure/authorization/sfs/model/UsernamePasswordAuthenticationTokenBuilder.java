package com.thecoders.cartunnbackend.iam.infrastructure.authorization.sfs.model;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class UsernamePasswordAuthenticationTokenBuilder {
    public static UsernamePasswordAuthenticationToken build(
            UserDetails principal,
            HttpServletRequest request) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
        );
        return usernamePasswordAuthenticationToken;
    }
}
