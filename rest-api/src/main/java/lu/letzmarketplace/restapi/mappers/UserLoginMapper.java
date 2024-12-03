package lu.letzmarketplace.restapi.mappers;

import lu.letzmarketplace.restapi.dto.UserLoginRequestDTO;
import lu.letzmarketplace.restapi.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserLoginMapper extends BaseMapper<UserLoginRequestDTO, User> {

    @Override
    public User toEntity(UserLoginRequestDTO dto) {
        return User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }
}
