package ies.g52.ShopAholytics.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.models.UserState;
import ies.g52.ShopAholytics.services.UserService;
import ies.g52.ShopAholytics.services.UserStateService;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
public class UserController {
    @Autowired
    private UserService serviceUser;

    @Autowired
    private UserStateService serviceUserState;

    
    @PostMapping("/addUser")
    public User newUser( @PathVariable(value = "pid") int pid, @Valid  @RequestBody User m) {
        return serviceUser.saveUser(new User (m.getPassword(),m.getEmail(),m.getName(),m.getGender(),m.getBirthday(),serviceUserState.getUserStateById(pid)));
    }

    @PostMapping("/addUserState")
    public UserState newUserState( @RequestBody UserState s) {
        return serviceUserState.saveUserState(new UserState (s.getDescription()));
    }

    
    @GetMapping("/Users")
    public List<User> findAllUsers() {
        List<User> a = serviceUser.getUsers();
        return a;
    }
    @GetMapping("/UserStates")
    public List<UserState> findAUserStates() {
        List<UserState> a = serviceUserState.getUserState();
        return a;
    }

    @GetMapping("/User")
    public User findUserById(@RequestParam(value = "id")  int id) {
        List<User> a = serviceUser.getUsers();
        
        for (User qu: a){
            if (qu.getId() == id ){
                return qu;
            }
        }
        return null;
        
    }



    @PutMapping("/updateUser")
    public User updateUser(@RequestBody User user) {
        return serviceUser.updateUser(user);
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id) {
        return serviceUser.deleteUser(id);
    }
}
