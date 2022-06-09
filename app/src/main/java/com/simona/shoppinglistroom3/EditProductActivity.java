package com.simona.shoppinglistroom3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProductActivity extends AppCompatActivity {

    EditText nameED, qtyED;
    Button saveBtn;
    View.OnClickListener clickLn;

    public static final String EDITED_NAME = "en";
    public static final int EDITED_QTY = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_productctivity);

        initViews();
        receivedDataForEdit();

    }

    void initViews() {
        nameED = findViewById(R.id.productNameEditText);
        qtyED = findViewById(R.id.productQtyEditText);
        saveBtn = findViewById(R.id.saveBackButton);

        clickLn = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndBackToMain();
            }
        };
        saveBtn.setOnClickListener(clickLn);
    }

    void receivedDataForEdit() {
        Intent receivedIntent = getIntent();
        String nameToBeEdited = receivedIntent.getStringExtra(MainActivity.NAME_FOR_EDIT);
        int qtyToBeEdited = receivedIntent.getIntExtra(String.valueOf(MainActivity.QTY_FOR_EDIT), -1);
        nameED.setText(nameToBeEdited);
        nameED.requestFocus();
        qtyED.setText(String.valueOf(qtyToBeEdited));
    }


    void saveAndBackToMain() {
        String editedName = nameED.getText().toString();
        int editedQty = Integer.parseInt(qtyED.getText().toString());
        if (editedQty < 0 ){
            return;
        }
        Intent backToMainIntent = new Intent(EditProductActivity.this, MainActivity.class);
        backToMainIntent.putExtra(EDITED_NAME, editedName);
        backToMainIntent.putExtra(String.valueOf(EDITED_QTY), editedQty);
        setResult(RESULT_OK, backToMainIntent);
        super.onBackPressed();
    }

}