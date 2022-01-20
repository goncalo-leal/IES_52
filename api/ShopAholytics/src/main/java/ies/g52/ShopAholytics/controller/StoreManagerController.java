package ies.g52.ShopAholytics.controller;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.email.Email;
import ies.g52.ShopAholytics.email.EmailConsts;
import ies.g52.ShopAholytics.email.EmailService;
import ies.g52.ShopAholytics.models.StoreManager;
import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.services.StoreManagerService;
import ies.g52.ShopAholytics.services.StoreService;
import ies.g52.ShopAholytics.services.UserService;
import ies.g52.ShopAholytics.services.UserStateService;



@RestController
@RequestMapping("/api/")
public class StoreManagerController {
    @Autowired
    private StoreManagerService StoreManagerServices;

    @Autowired
    private StoreService StoreServices;

    @Autowired
    private UserService serviceUser;

    @Autowired
    private UserStateService userStateService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/addStoreManager/{pid}/{store}")
    public StoreManager newStoreManager(@PathVariable(value = "pid") int pid, @PathVariable(value = "store") int store) {
        return StoreManagerServices.saveStoreManager(new StoreManager (serviceUser.getUserById(pid),StoreServices.getStoreById(store)));
    }

    @PostMapping("/addStoreManager/{store}")
    public StoreManager newShoppingManagerWithNewUser( @PathVariable(value = "store") int store,@RequestBody User m) {
        System.out.println("adding store mngr");
        String psw = genPass();
        Email e = new Email(EmailConsts.OUR_EMAIL ,m.getEmail());
        e.setSubject(EmailConsts.STORE_MANAGER_INVITE_SUBJECT);
        e.setText(EmailConsts.STORE_MANAGER_INVITE_CONTENT(m.getEmail(), StoreServices.getStoreById(store).getName(), "SHOPPING", psw));
        if (emailService.send(e)) {

            User user = new User(psw,m.getEmail(),m.getName(),m.getGender(),m.getBirthday(),userStateService.getUserStateById(1), "ROLE_STORE_MANAGER");
            serviceUser.saveUser(user);
            System.out.println("email success");
            return StoreManagerServices.saveStoreManager(new StoreManager (user,StoreServices.getStoreById(store)));
        }
        else {
            System.out.println("failed email");
            return null;
        }
    }
    

    @GetMapping("/StoreManagers")
    public List<StoreManager> findAllStoreManager() {
        List<StoreManager> a = StoreManagerServices.getStoreManagers();
        return a;
    }
    @GetMapping("/StoreManager")
    public StoreManager findStoreManagerById(@RequestParam(value = "id")  int id) {
        List<StoreManager> a = StoreManagerServices.getStoreManagers();
        
        for (StoreManager qu: a){
            if (qu.getId() == id ){
                return qu;
            }
        }
        return null;
        
    }

    // Os updates são feitos na no store e no user
    @CrossOrigin(origins = "http://localhost:8000")
    @PutMapping("/updateAcceptStoreManager/{user}")
    public User updateAcceptStoreManager(@PathVariable(value = "user") StoreManager user) {
        StoreManager a = StoreManagerServices.getStoreManagerById(user.getId());
        User u= a.getUser();
        u.setState(userStateService.getUserStateById(2));
        return serviceUser.updateUser(u);
    }

    @CrossOrigin(origins = "http://localhost:8000")
    @PutMapping("/updateBlockStoreManager/{user}")
    public User updateBlockStoreManager( @PathVariable(value = "user") StoreManager user) {
        StoreManager a = StoreManagerServices.getStoreManagerById(user.getId());
        User u= a.getUser();
        u.setState(userStateService.getUserStateById(3));
        return serviceUser.updateUser(u);
    }

    @DeleteMapping("/deleteStoreManager/{id}")
    public String deleteStoreManager(@PathVariable int id) {
        return StoreManagerServices.deleteStoreManager(id);
    }

    @GetMapping("/StoreManagerShopping/{id}")
    public List<StoreManager> findAllStoreManagersShopping(@PathVariable int id) {
        return StoreManagerServices.storeManagersOfShopping(id);
    }


    private String genPass() {
        byte[] arr = new byte[15];
        new Random().nextBytes(arr);
        return new String(arr,Charset.forName("UTF-8"));
    }
}

