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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ies.g52.ShopAholytics.auth.AuthConsts;
import ies.g52.ShopAholytics.enumFolder.SensorEnum;
import ies.g52.ShopAholytics.models.Shopping;
import ies.g52.ShopAholytics.models.Park;
import ies.g52.ShopAholytics.models.Sensor;
import ies.g52.ShopAholytics.models.SensorPark;
import ies.g52.ShopAholytics.models.SensorShopping;
import ies.g52.ShopAholytics.models.SensorStore;
import ies.g52.ShopAholytics.models.ShoppingManager;
import ies.g52.ShopAholytics.models.Store;
import ies.g52.ShopAholytics.models.UserState;
import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.services.ParkService;
import ies.g52.ShopAholytics.services.SensorParkService;
import ies.g52.ShopAholytics.services.SensorService;
import ies.g52.ShopAholytics.services.SensorShoppingService;
import ies.g52.ShopAholytics.services.SensorStoreService;
import ies.g52.ShopAholytics.services.ShoppingManagerService;
import ies.g52.ShopAholytics.services.ShoppingServices;
import ies.g52.ShopAholytics.services.StoreService;
import ies.g52.ShopAholytics.services.UserService;
import ies.g52.ShopAholytics.services.UserStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
@SpringBootApplication
public class ShopAholyticsApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(ShopAholyticsApplication.class, args);
	}
		@Transactional
		@Bean
		public CommandLineRunner demo(ParkService parkService,SensorParkService SensorParkService,SensorStoreService SensorStoreServices,SensorService sensorService,SensorShoppingService SensorShoppingServices,StoreService storeService,ShoppingManagerService ShoppingManagerServices,UserService userService,UserStateService repository,ShoppingServices shoppingService) {
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

				Store zara = new Store("Piso 0, 3 loja da direita","Zara",500,aberturav2,fechov2,new_shopping);
				Store jumbo=new Store ("Piso 0, lado esquerdo","Jumbo",2500,abertura,fecho,new_shopping);
				Store hm=new Store ("Piso 1","H&M",125,aberturav2,fechov2,new_shopping);
				storeService.saveStore(zara);
				storeService.saveStore(jumbo);
				storeService.saveStore(hm);

				User user = new User(new BCryptPasswordEncoder().encode("admin"), "admin@ua.pt" ,"Administrador","male","1988-07-28",repository.getUserStateById(2), AuthConsts.SHOPPING_MANAGER);
				ShoppingManagerServices.saveShoppingManager(new ShoppingManager(user, shoppingService.getShoppingById(1)));
				userService.saveUser(user);

				Sensor zara_exit= new Sensor(SensorEnum.EXIT.toString(),"Sensor de saida da Zara");
				Sensor zara_enter = new Sensor(SensorEnum.ENTRACE.toString(), "Sensor de entrada da Zara");

				Sensor Jumbo_exit1= new Sensor(SensorEnum.EXIT.toString(),"Sensor de saida do Jumbo 1");
				Sensor Jumbo_enter1= new Sensor(SensorEnum.ENTRACE.toString(), "Sensor de entrada do Jumbo 1");
				Sensor Jumbo_exit2= new Sensor(SensorEnum.EXIT.toString(),"Sensor de saida do Jumbo 2");
				Sensor Jumbo_enter2= new Sensor(SensorEnum.ENTRACE.toString(), "Sensor de entrada do Jumbo 2");


				Sensor hm_exit= new Sensor(SensorEnum.EXIT.toString(),"Sensor de saida da hm");
				Sensor hm_enter = new Sensor(SensorEnum.ENTRACE.toString(), "Sensor de entrada da hm");


				Sensor gli_exit1= new Sensor(SensorEnum.EXIT.toString(),"Sensor de saida do glicinias 1");
				Sensor gli_enter1 = new Sensor(SensorEnum.ENTRACE.toString(), "Sensor de entrada do glicinias 1");

				Sensor gli_exit2= new Sensor(SensorEnum.EXIT.toString(),"Sensor de saida do glicinias 2");
				Sensor gli_enter2 = new Sensor(SensorEnum.ENTRACE.toString(), "Sensor de entrada do glicinias 2");

				Sensor gli_exit3= new Sensor(SensorEnum.EXIT.toString(),"Sensor de saida do glicinias 3");
				Sensor gli_enter3 = new Sensor(SensorEnum.ENTRACE.toString(), "Sensor de entrada do glicinias 3");

				SensorShoppingServices.saveSensorShopping(new SensorShopping( new_shopping,gli_exit1));
				SensorShoppingServices.saveSensorShopping(new SensorShopping( new_shopping,gli_exit2));
				SensorShoppingServices.saveSensorShopping(new SensorShopping( new_shopping,gli_exit3));
				SensorShoppingServices.saveSensorShopping(new SensorShopping( new_shopping,gli_enter1));
				SensorShoppingServices.saveSensorShopping(new SensorShopping( new_shopping,gli_enter2));
				SensorShoppingServices.saveSensorShopping(new SensorShopping( new_shopping,gli_enter3));
				sensorService.saveSensor(gli_exit1);
				sensorService.saveSensor(gli_exit2);
				sensorService.saveSensor(gli_exit3);
				sensorService.saveSensor(gli_enter1);
				sensorService.saveSensor(gli_enter2);
				sensorService.saveSensor(gli_enter3);

				SensorStoreServices.saveSensorStore(new SensorStore(zara,zara_exit));
				SensorStoreServices.saveSensorStore(new SensorStore(zara,zara_enter));
				SensorStoreServices.saveSensorStore(new SensorStore(jumbo,Jumbo_exit1));
				SensorStoreServices.saveSensorStore(new SensorStore(jumbo,Jumbo_exit2));
				SensorStoreServices.saveSensorStore(new SensorStore(jumbo,Jumbo_enter1));
				SensorStoreServices.saveSensorStore(new SensorStore(jumbo,Jumbo_enter2));
				SensorStoreServices.saveSensorStore(new SensorStore(hm,hm_exit));
				SensorStoreServices.saveSensorStore(new SensorStore(hm,hm_enter));
				sensorService.saveSensor(zara_exit);
				sensorService.saveSensor(zara_enter);
				sensorService.saveSensor(Jumbo_exit1);
				sensorService.saveSensor(Jumbo_exit2);
				sensorService.saveSensor(Jumbo_enter1);
				sensorService.saveSensor(Jumbo_enter2);
				sensorService.saveSensor(hm_exit);
				sensorService.saveSensor(hm_enter);

				Park subterraneo = new Park("Parque subterraneo", "Piso -1", 2000,abertura,fecho,new_shopping);
				parkService.savePark(subterraneo);

				Sensor subterraneo_exit1= new Sensor(SensorEnum.EXIT.toString(),"Sensor de saida do  park subterraneo 1");
				Sensor subterraneo_enter1= new Sensor(SensorEnum.ENTRACE.toString(), "Sensor de entrada do  park subterraneo 1");
				Sensor subterraneo_exit2= new Sensor(SensorEnum.EXIT.toString(),"Sensor de saida do  park subterraneo 2");
				Sensor subterraneo_enter2= new Sensor(SensorEnum.ENTRACE.toString(), "Sensor de entrada do  park subterraneo 2");

				SensorParkService.saveSensorPark(new SensorPark(subterraneo,subterraneo_exit1));
				SensorParkService.saveSensorPark(new SensorPark(subterraneo,subterraneo_enter1));
				SensorParkService.saveSensorPark(new SensorPark(subterraneo,subterraneo_exit2));
				SensorParkService.saveSensorPark(new SensorPark(subterraneo,subterraneo_enter2));

				sensorService.saveSensor(subterraneo_exit1);
				sensorService.saveSensor(subterraneo_enter1);
				sensorService.saveSensor(subterraneo_exit2);
				sensorService.saveSensor(subterraneo_enter2);
			}	
		  };
	
	}

}
