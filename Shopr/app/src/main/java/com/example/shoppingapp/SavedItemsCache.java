package com.example.shoppingapp;

import java.util.ArrayList;
import java.util.List;
//This class will keep the cache of the items that are uploaded when user first logs in
//getLocal
//geFromDB
public class SavedItemsCache {
    private List<Upload> dbUploads;
    private List<Upload> dbCache;
    //when user logs in / or when user clicks refresh
    SavedItemsCache(List<Upload> currentUploads){
        dbCache = new ArrayList<>(dbUploads);
    }
    public List<Upload> getDataBaseUploads(){
        return dbUploads;
    }
    public List<Upload> getDbCache(){
        return dbCache;
    }





}
