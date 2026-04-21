package danil.messenger.dto.mapping;

import danil.messenger.dto.message.MessageResponse;
import danil.messenger.models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(source = "user.id", target = "senderId")
    @Mapping(source = "user.username", target = "senderUsername")
    @Mapping(source = "chat.id", target = "chatId")
    MessageResponse toDto(Message message);

    Message toEntity(MessageResponse dto);
}
