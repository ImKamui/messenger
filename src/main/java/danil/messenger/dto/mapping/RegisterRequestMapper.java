package danil.messenger.dto.mapping;

import danil.messenger.dto.RegisterRequest;
import danil.messenger.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterRequestMapper {

    RegisterRequest toDto(User user);

    User toEntity(RegisterRequest dto);
}
