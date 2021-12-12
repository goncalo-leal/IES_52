package ies.g52.ShopAholytics.views;

import java.util.List;

import ies.g52.ShopAholytics.models.Park;
import ies.g52.ShopAholytics.models.Store;

public interface ShoppingStoresParksView {
    int getId();
    String getName();
    List<Park> getParks();
    List<Store> getStores();
}
