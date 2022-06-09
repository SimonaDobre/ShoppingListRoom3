package com.simona.shoppinglistroom3;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductViewModel extends AndroidViewModel {

    private ProductDAO productDaoViewModel;
    private RoomDBproducts productsDBinstanceViewModel;
    private LiveData<List<Product>> productsArrayViewModel;

    ExecutorService exec = Executors.newSingleThreadExecutor();
//    Handler han = new Handler(Looper.getMainLooper());

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productsDBinstanceViewModel = RoomDBproducts.getInstance(application);
        productDaoViewModel = productsDBinstanceViewModel.productDaoMethod();
        productsArrayViewModel = productDaoViewModel.getAllProductsFromTheDataBase();
    }

    public void insertWrapperViewModel(Product p) {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                productDaoViewModel.insertProduct(p);
            }
        });
    }

    LiveData<List<Product>> getProductsArrayViewModel() {
        return productsArrayViewModel;
    }

    public void modifyStatusProduct(Product p) {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                int id = p.getId();
                String name = p.getProductName();
                int qty = p.getQty();
                boolean status = p.isSolved();
                productDaoViewModel.updateStatusProduct(id, !status);
            }
        });
    }

    public void editAproduct(Product p) {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                int id = p.getId();
                String name = p.getProductName();
                int qty = p.getQty();
                productDaoViewModel.editAProduct(id, name, qty);
            }
        });
    }

    public void deleteOneProductViewModel(Product p) {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                productDaoViewModel.deleteProduct(p);
            }
        });
    }

    public void deleteAllProductsViewModel() {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                productDaoViewModel.deleteAllProducts(productsArrayViewModel.getValue());
            }
        });
    }

}
