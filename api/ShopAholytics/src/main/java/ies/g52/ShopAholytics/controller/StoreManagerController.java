package ies.g52.ShopAholytics.controller;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.email.Email;
import ies.g52.ShopAholytics.email.EmailConsts;
import ies.g52.ShopAholytics.email.EmailService;
import ies.g52.ShopAholytics.auth.AuthConsts;
import ies.g52.ShopAholytics.models.StoreManager;
import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.services.StoreManagerService;
import ies.g52.ShopAholytics.services.StoreService;
import ies.g52.ShopAholytics.services.UserService;
import ies.g52.ShopAholytics.services.UserStateService;
import net.bytebuddy.utility.RandomString;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/storemanagers")
public class StoreManagerController {
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        String psw = genPass();
        Email e = new Email(EmailConsts.OUR_EMAIL ,m.getEmail());
        e.setSubject(EmailConsts.STORE_MANAGER_INVITE_SUBJECT);

        //TODO: (PARA TI OH ARQUITETO) MUDAR A STRING "SHOPPING" AQUI EM BAIXO PARA O NOME DO SHOPPING A QUE A LOJA PERTENCE

        e.setText(EmailConsts.STORE_MANAGER_INVITE_CONTENT(m.getEmail(), StoreServices.getStoreById(store).getName(), "SHOPPING", psw));


        if (emailService.send(e)) {
            User user = new User(psw,m.getEmail(),m.getName(),m.getGender(),m.getBirthday(),userStateService.getUserStateById(1), "ROLE_STORE_MANAGER");
            serviceUser.saveUser(user);
            return StoreManagerServices.saveStoreManager(new StoreManager (user,StoreServices.getStoreById(store)));
        }
        else {
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
        return StoreManagerServices.getStoreManagerById(id);
    }

    // Os updates são feitos na no store e no user
    @PutMapping("/updateAcceptStoreManager/{user}")
    public User updateAcceptStoreManager(@PathVariable(value = "user") StoreManager user) {
        StoreManager a = StoreManagerServices.getStoreManagerById(user.getId());
        User u= a.getUser();
        u.setState(userStateService.getUserStateById(2));
        return serviceUser.updateUser(u);
    }

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
        int len = 15;
        SecureRandom sr = new SecureRandom();
        String alnum = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(len);
        for (int i=0; i < len; i++) {
            sb.append(alnum.charAt(sr.nextInt(alnum.length())));
        }
        return sb.toString();
    }
}

