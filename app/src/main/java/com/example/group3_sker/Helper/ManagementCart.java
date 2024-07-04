package com.example.group3_sker.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.group3_sker.Domain.Product;

import java.util.ArrayList;

public class ManagementCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagementCart(Context context) {
        this.context = context;
        tinyDB = new TinyDB(context);
    }

    public void insertFood(Product item) {
        ArrayList<Product> listPop = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < listPop.size(); i++) {
            if (listPop.get(i).getName().equals(item.getName())) {
                existAlready = true;
                n = i;
                break;
            }
        }
        if (existAlready) {
            // Update number in cart if item exists
            listPop.get(n).setNumberInCart(listPop.get(n).getNumberInCart() + item.getNumberInCart());
        } else {
            // Add new item to cart
            listPop.add(item);
        }
        tinyDB.putListObject("CartList", listPop);
        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Product> getListCart() {
        ArrayList<Product> cartList = tinyDB.getListObject("CartList");
        if (cartList == null) {
            cartList = new ArrayList<>();
        }
        return cartList;
    }

    public void clearCart() {
        tinyDB.remove("CartList");
    }

    public void minusNumberItem(ArrayList<Product> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listItem.get(position).getNumberInCart() == 1) {
            listItem.remove(position);
        } else {
            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart() - 1);
        }
        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.change();
    }

    public void plusNumberItem(ArrayList<Product> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.change();
    }

    public Double getTotalFee() {
        ArrayList<Product> listItem = getListCart();
        double fee = 0;
        for (int i = 0; i < listItem.size(); i++) {
            fee += listItem.get(i).getPrice() * listItem.get(i).getNumberInCart();
        }
        return fee;
    }
}
