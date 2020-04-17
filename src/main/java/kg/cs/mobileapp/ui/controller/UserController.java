package kg.cs.mobileapp.ui.controller;

import kg.cs.mobileapp.service.UserService;
import kg.cs.mobileapp.shared.dto.UserDto;
import kg.cs.mobileapp.ui.model.request.UserDetailsRequestModel;
import kg.cs.mobileapp.ui.model.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.beans.Beans;

@RestController
@RequestMapping("users")        // http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public String getUser(){
        return "get user method was called";
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
        // initializing instance "returnValue" of class UserRest
        UserRest returnValue = new UserRest();

        // initializing instance "userDto" of class UserDto (shared Data Transfer Object)
        UserDto userDto = new UserDto();

        // transfer received values from "userDetails" obj into the "userDto" ojb
        BeanUtils.copyProperties(userDetails, userDto);

        // Create the new user in the DB and return the response back
        UserDto createdUser = userService.createUser(userDto);

        // transfer values from createdUser obj into the "returnValue" ojb
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping
    public String updateUser(){
        return "update user method was called";
    }

    @DeleteMapping
    public String deleteUser(){
        return "Delete user method was called";
    }
}
