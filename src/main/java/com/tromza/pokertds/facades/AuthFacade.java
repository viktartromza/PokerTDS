package com.tromza.pokertds.facades;

import com.tromza.pokertds.request.AuthRequest;

public interface AuthFacade {
    String getTokenForUser(AuthRequest authRequest);
}
