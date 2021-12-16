package ies.g52.ShopAholytics;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ies.g52.ShopAholytics.models.UserState;
import ies.g52.ShopAholytics.services.UserStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
@SpringBootApplication
public class ShopAholyticsApplication {
	@Autowired
    private static UserStateService userStateService;

	public static void main(String[] args) {
		SpringApplication.run(ShopAholyticsApplication.class, args);
	}

		@Bean
		public CommandLineRunner demo(UserStateService repository) {
		  return (args) -> {
			List<UserState> a = repository.getUserState();
			List<String>  estados = new ArrayList<>();
			for (UserState user : a){
				if(! estados.contains(user.getDescription())){
					estados.add(user.getDescription());
				}
			}
			if (! estados.contains("Waiting approvement")){
				repository.saveUserState(new UserState("Waiting approvement"));
			}
			if (! estados.contains("Approved")){
				repository.saveUserState(new UserState("Approved"));
			}if (! estados.contains("Blocked")){
				repository.saveUserState(new UserState("Blocked"));
			}
			
			

		  };
	
	}

}
