package kg.cs.mobileapp.ui.controller;

import kg.cs.mobileapp.exceptions.UserServiceException;
import kg.cs.mobileapp.service.UserService;
import kg.cs.mobileapp.shared.dto.UserDto;
import kg.cs.mobileapp.ui.model.request.UserDetailsRequestModel;
import kg.cs.mobileapp.ui.model.response.ErrorMessages;
import kg.cs.mobileapp.ui.model.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.beans.Beans;

@RestController
@RequestMapping("users")        // http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    // localhost:8080/users/user_id
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id) {
        UserRest returnValue = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);
        return returnValue;
    }

    // localhost:8080/users
    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        // initializing instance "returnValue" of class UserRest
        UserRest returnValue = new UserRest();

        if (userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        // if (userDetails.getFirstName().isEmpty()) throw new NullPointerException("The object is null");

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

    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails){
        // initializing instance "returnValue" of class UserRest
        UserRest returnValue = new UserRest();

        if (userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        // if (userDetails.getFirstName().isEmpty()) throw new NullPointerException("The object is null");

        // initializing instance "userDto" of class UserDto (shared Data Transfer Object)
        UserDto userDto = new UserDto();

        // transfer received values from "userDetails" obj into the "userDto" ojb
        BeanUtils.copyProperties(userDetails, userDto);

        // Create the new user in the DB and return the response back
        UserDto updatedUser = userService.updateUser(id, userDto);

        // transfer values from createdUser obj into the "returnValue" ojb
        BeanUtils.copyProperties(updatedUser, returnValue);

        return returnValue;
    }

    @DeleteMapping
    public String deleteUser(){
        return "Delete user method was called";
    }
}
