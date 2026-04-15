package danil.messenger.dto.mapping;

import danil.messenger.dto.AuthResponse;
import danil.messenger.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthResponseMapper {

    User toEntity(AuthResponse dto);

    AuthResponse toDto(User user);
}
