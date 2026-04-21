package danil.messenger.services.users.impls;

import danil.messenger.dto.UserDto;
import danil.messenger.dto.mapping.UserDtoMapper;
import danil.messenger.exceptions.AppException;
import danil.messenger.repositories.UserRepository;
import danil.messenger.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserDtoMapper userDtoMapper;

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(userDtoMapper::toDto).toList();
    }

    @Override
    public UserDto findOneByUsername(String username) {
        return userRepository.findByUsername(username).map(userDtoMapper::toDto).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

    @Override
    public UserDto findOne(int id) {
        return userRepository.findById(id).map(userDtoMapper::toDto).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

    @Override
    public UserDto findOneByUsernameContainsIgnoreCase(String username) {
        return userRepository.findByUsernameContainsIgnoreCase(username).map(userDtoMapper::toDto).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

    @Override
    public UserDto searchByUsername(String username) {
        return userRepository.searchByUsername(username).map(userDtoMapper::toDto).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }
}
