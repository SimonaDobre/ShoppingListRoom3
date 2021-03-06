package com.simona.shoppinglistroom3;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ProductViewHolder> {

    private Context context;
    private List<Product> productsArray;
    private EditProduct editProductInterface;

    public Adapter(Context context, EditProduct editProductInterface) {
        this.context = context;
        productsArray = new ArrayList<>();
        this.editProductInterface = editProductInterface;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productTV, qtyTV, idTV;
        EditProduct epi;
        ImageView editIV;
        LinearLayout wholeRow;

        public ProductViewHolder(@NonNull View itemView, EditProduct epi) {
            super(itemView);
            productTV = itemView.findViewById(R.id.productTextView);
            qtyTV = itemView.findViewById(R.id.qtyTextView);
            idTV = itemView.findViewById(R.id.idTextView);
            editIV = itemView.findViewById(R.id.editProductImageView);
            wholeRow = itemView.findViewById(R.id.itemViewLinearLayout);

            this.epi = epi;
            itemView.setOnClickListener(this);

            editIV.setOnClickListener(this);

//            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedID = view.getId();
            int clickedRow = getAdapterPosition();
            if (clickedRow != RecyclerView.NO_POSITION) {
                if (clickedID == R.id.editProductImageView) {
                    epi.editThisProduct(productsArray.get(clickedRow));
                } else {
                    epi.clickForMarkAsCompleted(productsArray.get(clickedRow));
                }
            }
        }
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View nvh = LayoutInflater.from(context).inflate(R.layout.product_row, parent, false);
        return new ProductViewHolder(nvh, editProductInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product currentProduct = productsArray.get(position);
        holder.productTV.setText(currentProduct.getProductName());
        holder.qtyTV.setText(String.valueOf(currentProduct.getQty()));
        holder.idTV.setText(String.valueOf(currentProduct.getId()));
        if (currentProduct.isSolved()) {
            holder.productTV.setPaintFlags(holder.productTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.qtyTV.setPaintFlags(holder.qtyTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.idTV.setPaintFlags((holder.idTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG));
        } else {
            // cand e false, nerezolvat, textul ramane netaiat
            holder.productTV.setPaintFlags(0);
            holder.qtyTV.setPaintFlags(0);
            holder.idTV.setPaintFlags(0);
        }
    }

    @Override
    public int getItemCount() {
        return productsArray.size();
    }

    public void actualiseListOfProducts(List<Product> newList) {
        productsArray = newList;
        notifyDataSetChanged();
    }

    public Product getProductAtPosition(int posi) {
        return productsArray.get(posi);
    }


}
