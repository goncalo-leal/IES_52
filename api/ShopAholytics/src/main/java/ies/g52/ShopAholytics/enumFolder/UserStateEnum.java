package ies.g52.ShopAholytics.enumFolder;

public enum UserStateEnum {
    ARCHIVED("ARCHIVED"),NOT_ARCHIVED("NOT_ARCHIVED"),APPROVED("APPROVED");
    private final String name;  
    private UserStateEnum(String s) {
        this.name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false 
        return name.equals(otherName);
    }

    public String toString() {
       return this.name;
    }
}
