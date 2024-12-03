package lu.letzmarketplace.restapi.mappers;

import lu.letzmarketplace.restapi.dto.UserRegistrationRequestDTO;
import lu.letzmarketplace.restapi.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationMapper extends BaseMapper<UserRegistrationRequestDTO, User> {

    @Override
    public User toEntity(UserRegistrationRequestDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }
}
