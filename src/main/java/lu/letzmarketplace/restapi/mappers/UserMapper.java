package lu.letzmarketplace.restapi.mappers;

import lu.letzmarketplace.restapi.dto.UserDTO;
import lu.letzmarketplace.restapi.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends BaseMapper<UserDTO, User> {

    @Override
    public User toEntity(UserDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .emailVerified(dto.isEmailVerified())
                .build();
    }

    @Override
    public UserDTO toDto(User entity) {
        return UserDTO.builder()
                .username(entity.getUsername())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .emailVerified(entity.isEmailVerified())
                .build();
    }
}
