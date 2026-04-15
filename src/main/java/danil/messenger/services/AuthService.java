package danil.messenger.services;

import danil.messenger.dto.AuthResponse;
import danil.messenger.dto.LoginRequest;
import danil.messenger.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

}
