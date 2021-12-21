package ies.g52.ShopAholytics.enumFolder;


public enum SensorEnum {
    ENTRACE("Entrace"),EXIT("Exit");
    private final String name;  
    private SensorEnum(String s) {
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

