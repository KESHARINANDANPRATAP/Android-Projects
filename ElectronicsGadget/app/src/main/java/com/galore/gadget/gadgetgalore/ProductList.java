package com.galore.gadget.gadgetgalore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.galore.gadget.gadgetgalore.Interface.ItemClickListener;
import com.galore.gadget.gadgetgalore.Model.Product;
import com.galore.gadget.gadgetgalore.ViewHolder.ProdcutViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductList extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference productList;

    String categoryId="";

    FirebaseRecyclerAdapter<Product,ProdcutViewHolder>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        //firebase
        database = FirebaseDatabase.getInstance();
        productList= database.getReference("Products");


        recyclerView = (RecyclerView)findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get intent here

        if(getIntent()!=null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId != null){
            loadListProduct(categoryId);
        }
    }

    private void loadListProduct(String categoryId) {
            adapter = new FirebaseRecyclerAdapter<Product, ProdcutViewHolder>(Product.class,
                    R.layout.product_item,
                    ProdcutViewHolder.class,
                    productList.orderByChild("MenuId").equalTo(categoryId) // like select * from product where menuId =

            ) {
                @Override
                protected void populateViewHolder(ProdcutViewHolder viewHolder, Product model, int position) {
                    viewHolder.product_name.setText(model.getName());
                    Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.product_image);

                    final Product local = model;
                     viewHolder.setItemClickListener(new ItemClickListener() {
                         @Override
                         public void onClick(View view, int position, boolean isLongClick) {
                            //start new activity
                             Intent prodcutDetail = new Intent(ProductList.this,ProductDetail.class);
                            prodcutDetail.putExtra("ProdcutId",adapter.getRef(position).getKey()); //send Product id to new activity
                             startActivity(prodcutDetail);

                         }
                     });
                }
            };


        recyclerView.setAdapter(adapter);

    }
}
