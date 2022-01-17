package ies.g52.ShopAholytics.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.enumFolder.UserStateEnum;
import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.models.UserState;
import ies.g52.ShopAholytics.services.UserService;
import ies.g52.ShopAholytics.services.UserStateService;

import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserStateService userStateService;

    
    @PostMapping("/addUser/{pid}")
    public User newUser( @PathVariable(value = "pid") int pid, @Valid  @RequestBody User m) {
        return userService.saveUser(new User (m.getPassword(),m.getEmail(),m.getName(),m.getGender(),m.getBirthday(),userStateService.getUserStateById(pid), m.getAuthority()));
    }

    @PostMapping("/addUserState")
    public UserState newUserState( @RequestBody UserState s) {
        if (s.getDescription().equalsIgnoreCase(UserStateEnum.APPROVED.toString())){
            return userStateService.saveUserState(new UserState (s.getDescription()));
        }
        else if(s.getDescription().equalsIgnoreCase(UserStateEnum.ARCHIVED.toString())){
            return userStateService.saveUserState(new UserState (s.getDescription()));
        }
        else if(s.getDescription().equalsIgnoreCase(UserStateEnum.NOT_ARCHIVED.toString())){
            return userStateService.saveUserState(new UserState (s.getDescription()));
        }
        return null;
    }

    
    @GetMapping("/Users")
    public List<User> findAllUsers() {
        List<User> a = userService.getUsers();
        return a;
    }
    @GetMapping("/UserStates")
    public List<UserState> findAUserStates() {
        List<UserState> a = userStateService.getUserState();
        return a;
    }

    @GetMapping("/User")
    public User findUserById(@RequestParam(value = "id")  int id) {
        return userService.getUserById(id);
        
    }


    @CrossOrigin(origins = "http://localhost:8000")
    @PutMapping("/updateUser")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }
}
