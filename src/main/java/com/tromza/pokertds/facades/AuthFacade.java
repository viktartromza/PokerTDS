package com.tromza.pokertds.facades;

import com.tromza.pokertds.model.request.AuthRequest;
import com.tromza.pokertds.model.response.AuthResponse;

public interface AuthFacade {
    AuthResponse getTokenForUser(AuthRequest authRequest);
}
