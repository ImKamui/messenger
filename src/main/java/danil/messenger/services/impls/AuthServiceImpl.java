package danil.messenger.services.impls;

import danil.messenger.dto.AuthResponse;
import danil.messenger.dto.LoginRequest;
import danil.messenger.dto.RegisterRequest;
import danil.messenger.dto.mapping.AuthResponseMapper;
import danil.messenger.exceptions.AppException;
import danil.messenger.models.User;
import danil.messenger.repositories.UserRepository;
import danil.messenger.services.AuthService;
import danil.messenger.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final AuthResponseMapper authResponseMapper;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsernameContainsIgnoreCase(request.getUsername()))
        {
            throw new AppException(HttpStatus.CONFLICT, "Пользователь с таким именем уже зарегестрирован");
        }
        if (userRepository.existsByEmail(request.getEmail()))
        {
            throw new AppException(HttpStatus.CONFLICT, "Пользователь с таким email уже зарегестрирован");
        }
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        AuthResponse response = authResponseMapper.toDto(user);
        response.setToken(token);
        response.setMessage("Регистрация успешна");
        return response;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByUsernameContainsIgnoreCase(request.getUsername())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Неверный пароль");
        }
        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .username(user.getUsername())
                .message("Вход выполнен успешно")
                .build();
    }
}
