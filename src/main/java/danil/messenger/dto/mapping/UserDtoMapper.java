package danil.messenger.dto.mapping;

import danil.messenger.dto.UserDto;
import danil.messenger.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    User toEntity(UserDto dto);

    UserDto toDto(User user);
}
