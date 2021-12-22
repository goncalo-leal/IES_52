package ies.g52.ShopAholytics;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ies.g52.ShopAholytics.models.Shopping;
import ies.g52.ShopAholytics.models.ShoppingManager;
import ies.g52.ShopAholytics.models.Store;
import ies.g52.ShopAholytics.models.StoreManager;
import ies.g52.ShopAholytics.models.UserState;
import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.services.ShoppingManagerService;
import ies.g52.ShopAholytics.services.ShoppingServices;
import ies.g52.ShopAholytics.services.StoreManagerService;
import ies.g52.ShopAholytics.services.StoreService;
import ies.g52.ShopAholytics.services.UserService;
import ies.g52.ShopAholytics.services.UserStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
@SpringBootApplication
public class ShopAholyticsApplication {
	@Autowired
    private static UserStateService userStateService;

	@Autowired
    private ShoppingServices shoppingService;

	@Autowired
    private StoreManagerService StoreManagerServices;

	@Autowired
    private UserService userService;

	@Autowired
    private StoreService storeService;

	@Autowired
    private ShoppingManagerService ShoppingManagerServices;

	public static void main(String[] args) {
		SpringApplication.run(ShopAholyticsApplication.class, args);
	}
		@Transactional
		@Bean
		public CommandLineRunner demo(StoreService storeService,ShoppingManagerService ShoppingManagerServices,UserService userService,UserStateService repository,ShoppingServices shoppingService) {
		  return (args) -> {
			if (repository.getUserState().size() == 0){
				repository.saveUserState(new UserState("Waiting approvement"));
				repository.saveUserState(new UserState("Approved"));

				repository.saveUserState(new UserState("Blocked"));

			}
			List<Shopping> shoppings = shoppingService.getShopping();
			

			if (shoppings.size() ==0){
				LocalTime abertura = LocalTime.of(7,00,00);  
				LocalTime fecho = LocalTime.of(00,00,00);  
				Shopping new_shopping= new Shopping("Aveiro","Glicinias",10000,abertura,fecho);
				shoppingService.saveShopping(new_shopping);
				
				LocalTime aberturav2 = LocalTime.of(9,00,00);  
				LocalTime fechov2 = LocalTime.of(22,00,00);  


				storeService.saveStore(new Store ("Piso 0, 3 loja da direita","Zara",500,aberturav2,fechov2,new_shopping));
				storeService.saveStore(new Store ("Piso 0, lado esquerdo","Jumbo",2500,abertura,fecho,new_shopping));
				storeService.saveStore(new Store ("Piso 1","H&M",125,aberturav2,fechov2,new_shopping));

				User user = new User("admin", "admin@ua.pt" ,"Administrador","male","1988-07-28",repository.getUserStateById(2));
				userService.saveUser(user);
				/*
				ShoppingManager manager=new ShoppingManager();
				System.out.println("1");
				manager.setUser(user);
				System.out.println("12");

				manager.setShopping(new_shopping);
				System.out.println("13");

				
				ShoppingManagerServices.saveShoppingManager(manager);*/
				//NÃO CONSIGO PERCEBER PORQUE QUE ISTO NÃO FUNCIONA
				//ShoppingManagerServices.saveShoppingManager(new ShoppingManager(userService.getUserById(1),new_shopping));


			


			}



		  };
	
	}

}
