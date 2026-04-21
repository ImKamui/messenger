package danil.messenger.services.users;

import danil.messenger.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();
    UserDto findOneByUsername(String username);
    UserDto findOne(int id);
    UserDto findOneByUsernameContainsIgnoreCase(String username);
    UserDto searchByUsername(String username);

}
