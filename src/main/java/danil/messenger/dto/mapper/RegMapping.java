package danil.messenger.dto.mapper;

import danil.messenger.dto.RegDto;
import danil.messenger.dto.UserDto;
import danil.messenger.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegMapping {

    @Mapping(target="username", source="username")
    @Mapping(target="email", source="email")
    @Mapping(target="password", source="password")
    RegDto toRegDto(User user);

    @Mapping(target="username", source="username")
    @Mapping(target="email", source="email")
    @Mapping(target="password", source="password")
    User toEntity(RegDto dto);
}
