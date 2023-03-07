package com.sample.shop.security;

import java.util.Collection;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class DomainUserDetails extends User {

    private final Pair<String, String> userDets;

    public DomainUserDetails(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        Pair<String, String> userDets
    ) {
        super(username, password, authorities);
        this.userDets = userDets;
    }

    public Pair<String, String> getUserDets() {
        return userDets;
    }
}
