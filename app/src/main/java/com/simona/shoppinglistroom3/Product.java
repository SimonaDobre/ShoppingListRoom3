package com.simona.shoppinglistroom3;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "allProducts")
public class Product {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nameColumn")
    private String productName;

    @ColumnInfo(name = "qtyColumn")
    private int qty;

    @ColumnInfo(name = "solvedColumn")
    private boolean solved;

    public Product(String productName, int qty, boolean solved) {
        this.productName = productName;
        this.qty = qty;
        this.solved = solved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public int getQty() {
        return qty;
    }

    public boolean isSolved() {
        return solved;
    }


}
