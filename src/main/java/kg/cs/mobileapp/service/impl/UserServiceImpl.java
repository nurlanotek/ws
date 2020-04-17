package kg.cs.mobileapp.service.impl;

import kg.cs.mobileapp.io.repositories.UserRepository;
import kg.cs.mobileapp.io.entity.UserEntity;
import kg.cs.mobileapp.service.UserService;
import kg.cs.mobileapp.shared.Utils;
import kg.cs.mobileapp.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) {
        // check if the user is already in the database. If yes, throw the Runtime exception, else continue
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Record already exist");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        // populating required fields because they part of the UserEntity class
         String publicUserId = utils.generateUserId(30);
         userEntity.setUserId(publicUserId);
//        userEntity.setUserId("129387461928376412376419237");

        // encrypting the user password and saving it inside UserEntity
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        // save new user to the database
        UserEntity storedUserDetails = userRepository.save(userEntity);

        // create return object "returnValue"
        UserDto returnValue = new UserDto();

        // copy values from storedUserDetails into the object "returnValue" of type UserDto
        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }

    // add in step number 7: to get user id.
    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}