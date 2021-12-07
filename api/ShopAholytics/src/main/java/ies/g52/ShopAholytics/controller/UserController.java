package ies.g52.ShopAholytics.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.models.UserState;
import ies.g52.ShopAholytics.services.UserService;
import ies.g52.ShopAholytics.services.UserStateService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService serviceUser;

    @Autowired
    private UserStateService serviceUserState;

    
    @PostMapping("/addUser")
    public User newUser( @RequestBody User m , @RequestBody UserState s) {
        return serviceUser.saveUser(new User (m.getPassword(),m.getEmail(),m.getName(),m.getGender(),m.getBirthday(),s));
    }

    @PostMapping("/addUserState")
    public UserState newUserState( @RequestBody UserState s) {
        return serviceUserState.saveUserState(new UserState (s.getDescription()));
    }

    
    @GetMapping("/quote")
    public Quote findAllQuotes() {
        List<Quote> a = service.getQuotes();
        Quote ret;
        if (a.size() >0 ){
             Random r = new Random();
		int low = 0;
		int high = a.size()-1;
		int result = r.nextInt(a.size());
         ret = a.get(result);
        }
        else{
            ret = new Quote();
        }
        return ret;
    }

    @GetMapping("/quotes")
    public Quote findQuoteById(@RequestParam(value = "show")  int id) {
        List<Quote> a = service.getQuotes();
        List<Quote> rets= new ArrayList<>();
        for (Quote qu: a){
            if (qu.getOrder().getId() == id ){
                rets.add(qu);
            }
        }
        Random r = new Random();
		int low = 0;
		int high = rets.size()-1;
		int result = r.nextInt(rets.size());
        Quote ret = rets.get(result);
        return ret;
    }



    @PutMapping("/updateQuote")
    public Quote updateQuote(@RequestBody Quote product) {
        return service.updateQuote(product);
    }

    
    @PostMapping("/addMovie")
    public Movie addMovie(@RequestBody Movie m) {
        return service2.saveMovie(m);
    }

   

    @GetMapping("/shows")
    public List<Movie> findAllMovies() {
        return service2.getMovies();
    }

   

    

    @PutMapping("/update")
    public Movie updateProduct(@RequestBody Movie product) {
        return service2.updateMovie(product);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteMovie(@PathVariable int id) {
        return service2.deleteMovie(id);
    }
}
