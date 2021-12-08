package ies.g52.ShopAholytics.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.models.UserState;
import ies.g52.ShopAholytics.repository.UserStateRepository;

@Service
public class UserStateService {
    @Autowired
    private UserStateRepository repository;

    public UserState saveUserState(UserState user) {
        return repository.save(user);
    }

    public List<UserState> saveUserState(List<UserState> user) {
        return repository.saveAll(user);
    }

    public List<UserState> getUserState() {
        return repository.findAll();
    }

    public UserState getUserStateById(int id) {
        return repository.findById((int)id).orElse(null);
    }

    public UserState getUserStateDescription(String description) {
        return repository.findByDescription(description);
    }

    public String deleteUserState(int id) {
        repository.deleteById(id);
        return "product removed !! " + id;
    }

    public UserState updateUser(UserState user) {
        UserState existingUser = repository.findById((int)user.getId()).orElse(null);
        existingUser.setDescription(user.getDescription());
        return repository.save(existingUser);
    }
}
