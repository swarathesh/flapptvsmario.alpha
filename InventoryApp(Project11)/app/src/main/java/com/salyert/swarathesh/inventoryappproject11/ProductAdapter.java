package com.salyert.swarathesh.inventoryappproject11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Context context, ArrayList<Product> products){
        super(context, 0, products);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        final Product currentProduct = getItem(position);
        TextView productNameView = (TextView)listItemView.findViewById(R.id.list_item_name);
        TextView productQuantityView = (TextView)listItemView.findViewById(R.id.list_item_quantity);
        TextView productPriceView = (TextView)listItemView.findViewById(R.id.list_item_price);
        Button saleButton = (Button)listItemView.findViewById(R.id.list_item_sale_btn);
        productNameView.setText(currentProduct.getProductName());
        productQuantityView.setText(Integer.toString(currentProduct.getQuantity()));
        productPriceView.setText(Integer.toString(currentProduct.getPrice()));
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductOpenHelper helper = ProductOpenHelper.getInstance(null);
                int quantity = currentProduct.getQuantity();
                if (quantity < 1){
                    Toast.makeText(getContext(), "There are no products to sell", Toast.LENGTH_SHORT).show();
                    return;
                }currentProduct.setQuantity(quantity - 1);
                helper.updateProduct(currentProduct);
            }
        });
        return listItemView;
    }
}
