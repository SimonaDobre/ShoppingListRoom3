package com.simona.shoppinglistroom3;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class RoomDBproducts extends RoomDatabase {

    public abstract ProductDAO productDaoMethod();

    private static RoomDBproducts productsDataBase;
    private static String DB_NAME = "productsDBname";

    static synchronized RoomDBproducts getInstance(Context context){
        if (productsDataBase == null){
            productsDataBase = Room.databaseBuilder(context,
                    RoomDBproducts.class,
                    DB_NAME).build();
        }
        return productsDataBase;
    }

}
