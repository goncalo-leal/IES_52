package ies.g52.ShopAholytics.email;

public class EmailConsts {
    public static final String OUR_EMAIL = "shopaholytics@gmail.com";
    public static final String PSW_EMAIL = "lodoserras";

    public static final String STORE_MANAGER_INVITE_SUBJECT = "ShopAholytics Store Management Invite";

    public static String STORE_MANAGER_INVITE_CONTENT(String email, String store, String shopping, String password) {
        return String.format("<h2>Hello, %s</h2><br>"
                            + "<p>You are invited to be the manager of <b>%s</b> at <b>%s</b></p>"
                            + "<p>To accept the invite, simply click <a href='http://192.168.160.238:8000/login.html'>here</a> and login with this email and the password:</p>"
                            + "<h3>%s</h3>"
                            , email, store, shopping, password);
    }  
}
