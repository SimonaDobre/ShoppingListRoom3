package com.simona.shoppinglistroom3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EditProduct {

    private EditText mEditTextName;
    private TextView mTextViewAmount;
    Button increaseButton, decreaseButton, addButton, deleteAllButon;
    RecyclerView rv;
    Adapter myAdapter;
    View.OnClickListener listenerPtDiverse;
    ProductViewModel productViewModel;

    private int mAmount = 0;
    private int idSentToEdit;
    private boolean completedStatusSentToEdit;
    public static final String NAME_FOR_EDIT = "nfe";
    public static final int QTY_FOR_EDIT = 100;

    ActivityResultLauncher<Intent> launcherForEditProduct = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    resultReceivedAfterEdit(result);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        swipeToDeleteSwapToMove();
        displayNewListOfProducts();

    }


    void displayNewListOfProducts() {
        productViewModel.getProductsArrayViewModel().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                myAdapter.actualiseListOfProducts(products);
            }
        });
    }

    void initViews() {
        mEditTextName = findViewById(R.id.nameOfItemEditTextID);
        mTextViewAmount = findViewById(R.id.amountTextViewID);

        rv = findViewById(R.id.recyclerviewID);
        rv.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration did = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(did);
        myAdapter = new Adapter(this, this);
        rv.setAdapter(myAdapter);

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        listenerPtDiverse = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedID = view.getId();
                switch (clickedID) {
                    case R.id.increaseButtonID:
                        MainActivity.this.increaseQty();
                        break;
                    case R.id.decreaseButtonID:
                        MainActivity.this.decreaseQty();
                        break;
                    case R.id.addButton:
                        String productName = mEditTextName.getText().toString();
                        if (productName.trim().equals("") || productName.trim().equals(null)) {
                            return;
                        }
                        int productQty = Integer.parseInt(mTextViewAmount.getText().toString());
                        Product newProduct = new Product(productName, productQty, false);
                        productViewModel.insertWrapperViewModel(newProduct);
                        Toast.makeText(MainActivity.this, "am adaugat " + productName, Toast.LENGTH_SHORT).show();
                        mEditTextName.setText(null);
                        mTextViewAmount.setText("0");
                        mAmount = 0;
                        break;
                    case R.id.deleteAllButton:
                        productViewModel.deleteAllProductsViewModel();
                        break;
                }
            }
        };


        increaseButton = findViewById(R.id.increaseButtonID);
        decreaseButton = findViewById(R.id.decreaseButtonID);
        addButton = findViewById(R.id.addButton);
        deleteAllButon = findViewById(R.id.deleteAllButton);

        increaseButton.setOnClickListener(listenerPtDiverse);
        decreaseButton.setOnClickListener(listenerPtDiverse);
        addButton.setOnClickListener(listenerPtDiverse);
        deleteAllButon.setOnClickListener(listenerPtDiverse);


    }

    private void increaseQty() {
        mAmount++;
        mTextViewAmount.setText(String.valueOf(mAmount));
    }

    private void decreaseQty() {
        if (mAmount > 0) {
            mAmount--;
        }
        mTextViewAmount.setText(String.valueOf(mAmount));
    }

    private void swipeToDeleteSwapToMove() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Product productToBeDeleted = myAdapter.getProductAtPosition(viewHolder.getAdapterPosition());
                productViewModel.deleteOneProductViewModel(productToBeDeleted);
                Snackbar.make(rv, productToBeDeleted.getProductName() + " was deleted ", Snackbar.LENGTH_LONG)
                        .setAction("UNDO ?", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                productViewModel.insertWrapperViewModel(productToBeDeleted);
                            }
                        }).show();
            }
        }).attachToRecyclerView(rv);
    }

    @Override
    public void editThisProduct(Product productForEdit) {
        Intent intentToEditActivity = new Intent(MainActivity.this, EditProductActivity.class);
        idSentToEdit = productForEdit.getId();
        String numePtEditare = productForEdit.getProductName();
        int qtyPtEditare = productForEdit.getQty();
        completedStatusSentToEdit = productForEdit.isSolved();
        intentToEditActivity.putExtra(MainActivity.NAME_FOR_EDIT, numePtEditare);
        intentToEditActivity.putExtra(String.valueOf(MainActivity.QTY_FOR_EDIT), qtyPtEditare);
        launcherForEditProduct.launch(intentToEditActivity);
    }

    private void resultReceivedAfterEdit(ActivityResult ar) {
        if (ar.getResultCode() == RESULT_OK) {
            Intent receivedIntentAfterEdit = ar.getData();
            if (receivedIntentAfterEdit != null) {
                String modifiedName = receivedIntentAfterEdit.getStringExtra(EditProductActivity.EDITED_NAME);
                int modifiedQty = receivedIntentAfterEdit.getIntExtra(String.valueOf(EditProductActivity.EDITED_QTY), -1);
                Product modifiedProduct = new Product(modifiedName, modifiedQty, completedStatusSentToEdit);
                modifiedProduct.setId(idSentToEdit);
                productViewModel.editAproduct(modifiedProduct);
            }
        }
    }

    @Override
    public void clickForMarkAsCompleted(Product productToBeMarked) {
        productViewModel.modifyStatusProduct(productToBeMarked);
    }


}