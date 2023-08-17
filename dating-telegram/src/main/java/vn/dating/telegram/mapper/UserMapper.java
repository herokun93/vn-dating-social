package vn.dating.telegram.mapper;

import org.modelmapper.ModelMapper;
import vn.dating.telegram.dto.UserDto;
import vn.dating.telegram.model.User;

public class UserMapper {

    public static UserDto userDto(User user){
        ModelMapper modelMapper = new ModelMapper();

        UserDto userDto = modelMapper.map(user,UserDto.class);
        return  userDto;
    }
}
