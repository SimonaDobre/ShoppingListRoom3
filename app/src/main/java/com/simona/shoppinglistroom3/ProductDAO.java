package com.simona.shoppinglistroom3;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ProductDAO {

    @Insert(onConflict = REPLACE)
    void insertProduct(Product p);

    @Delete
    void deleteProduct(Product p);

    @Delete
    void deleteAllProducts(List<Product> allProducts);

    @Query("UPDATE allProducts SET solvedColumn =:status WHERE id =:idul")
    void updateStatusProduct(int idul, boolean status);

    @Query("UPDATE allProducts SET nameColumn =:name, qtyColumn =:qty, nameColumn =:name WHERE id =:idul")
    void editAProduct (int idul, String name, int qty);

    @Query("SELECT * FROM allProducts")
    LiveData<List<Product>> getAllProductsFromTheDataBase();

    @Query("SELECT * FROM allProducts WHERE id =:specificID")
    Product getSpecificProduct(int specificID);

}
