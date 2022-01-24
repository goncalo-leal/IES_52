package ies.g52.ShopAholytics.auth;

public class AuthConsts {

    // JWT PARAMS
    public static final String SECRET = "TESTE";
    public static final long EXPIRATION = 900_000 * 60;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_KEY = "Authorization";
    public static final String JWT_ROLE_CLAIM = "ROLE";

    // ROLE STRING REPRESENTATION
    public static final String SHOPPING_MANAGER = "ROLE_SHOPPING_MANAGER";
    public static final String STORE_MANAGER = "ROLE_STORE_MANAGER";

    // ENDPOINT MANAGEMENT
    public static final String LOG_IN_URL = "/auth/login";

    public static final String[] PUBLIC_ENDPOINTS = {
        "/api/shoppings/Shoppings",
        "/api/shoppings/Shopping",
        "/mq/*"
    };


    public static final String[] SHOPPING_MANAGER_PROTECTED_ENDPOINTS = {
        "/api/parks/*",

        "/api/users/addUser/{pid}",
        "/api/users/addUserState",
        "/api/users/Users",
        "/api/users/UserStates",
        "/api/users/deleteUser/{id}",
        "/api/users/User",

        "/api/storemanagers/addStoreManager/*",
        "/api/storemanagers/StoreManagers",
        "/api/storemanagers/StoreManagerShopping/{id}",
        "/api/storemanagers/deleteStoreManager/{id}",
        "/api/storemanagers/updateAcceptStoreManager/{user}",
        "api/updateBlockStoreManager/{user}",

        "/api/stores/addStore/{pid}",
        "/api/stores/Stores",

        "/api/shoppingmanangers/*",

        "/api/shoppings/updateShopping",
        "/api/shoppings/deleteShopping/{id}",

        "/api/sensorsstore/*",

        "/api/sensorsshopping/*",

        "/api/sensorspark/*",

        "/api/sensors/deleteSensor/{id}"
    };
}