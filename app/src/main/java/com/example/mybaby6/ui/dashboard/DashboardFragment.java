package com.example.mybaby6.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mybaby6.MyInterface;
import com.example.mybaby6.databinding.FragmentDashboardBinding;
import com.example.mybaby6.inShop;
import com.example.mybaby6.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class DashboardFragment extends Fragment implements MyInterface {
    static ImageView imageView2;
    static View root;
    public static ArrayList<String> basket = new ArrayList<>();
    boolean ifContainsAll = true;
    public static ArrayList<String> basketFinal = new ArrayList<>();
    public static ArrayList<ArrayList<String>> pricesToDisplay = new ArrayList<>();
    //basketFinal contains "shop" "product name" "price"
    //
    public static ArrayList<String> a;
    static ListView list, list2;
    private FragmentDashboardBinding binding;
    public static ArrayList<ArrayList<String>> allProductsPrice;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        list = binding.ListOfBasket;
        list2 = binding.ShopLists;
        a = HomeFragment.basket;
        allProductsPrice = HomeFragment.allProductsPrice;



        if (basketFinal.size() == 0 || basketFinal.size() / shops.size() != basket.size()){
            //forming a list with quantities
            for (String i: a){
                String[] mm = i.split(" ");
                basket.add(String.join(" ", Arrays.copyOfRange(mm, 0, mm.length - 1)));
            }
            basketFinal = getPrice(basket);
            getResult(basketFinal);
            ArrayList<String> m = new ArrayList<>();
            for (int i = 0; i < shops.size(); i++) {
                ArrayList<String > b = inShoP(shops.get(i));
                if (b.size() == basket.size()) {
                   m.add("Магазин: " + shops.get(i) + ".\nЦена: " + x.get(x.size() - 1).get(i) + ".");
                }
                else{
                    m.add("Магазин: " + shops.get(i) + ".\nЦена: " + x.get(x.size() - 1).get(i) + ".\n" + "Не все выбранные продукты");
                }
            }
            pricesToDisplay.add(m);
        }
        ListViewAdapter adapter = new ListViewAdapter(getActivity(), basket);
        ListViewAdapter adapter2 = new ListViewAdapter(getActivity(), pricesToDisplay.get(pricesToDisplay.size() - 1));
        list2.setAdapter(adapter2);
        list.setAdapter(adapter);
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String s = (String) adapter2.getItem(position);
                Intent intent = new Intent(getActivity(), inShop.class);
                intent.putExtra("num", HomeFragment.shops.get(position));
                startActivity(intent);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                removeItem(i);
                return false;
            }
        });
        return root;
    }
    // function to remove an item given its index in the grocery list.
    public static String removeItem(int i) {
        String k;
        String [] m1 = a.get(i).split(" ");
        if (Integer.parseInt(m1[m1.length - 1]) == 1){
            basket.remove(i);
            a.remove(i);
            k = "0";
        }
        else {
            a.set(i, String.join(" ", Arrays.copyOfRange(m1, 0, m1.length - 1)) + " " + (-1 + Integer.parseInt(m1[m1.length - 1])));
            k = a.get(i).split(" ")[m1.length - 1];
        }
        basketFinal.remove(basketFinal.size() - 1);
        basketFinal = getPrice(basket);
        getResult(basketFinal);
        ArrayList<String> m = new ArrayList<>();
        for (int j = 0; j < shops.size(); j++) {
            m.add("Магазин: " + shops.get(j) + ".\nЦена: " + x.get(x.size() - 1).get(j) + ".");
        }
        pricesToDisplay.add(m);

        ListViewAdapter adapter = new ListViewAdapter((Activity) root.getContext(), basket);
        ListViewAdapter adapter2 = new ListViewAdapter((Activity) root.getContext(), pricesToDisplay.get(pricesToDisplay.size() - 1));
        list.setAdapter(adapter);
        list2.setAdapter(adapter2);

        return k;
    }
    //form a basket with prices
    public static ArrayList<String> getPrice(ArrayList<String> basket) {
        basketFinal = new ArrayList<String>();
        for (String product: basket){
            for (ArrayList<String> priceWprod: allProductsPrice){
                if (product.equals(priceWprod.get(1)) && !basketFinal.contains(String.join(" ", priceWprod))){
                    basketFinal.add(String.join(" ", priceWprod));
                }
            }
        }
        return basketFinal;
    }

    //forms an arraylist with prices in each shop
    public static void getResult(ArrayList<String> basketFinal){
        if (a == null) return;
        ArrayList<String> prices = new ArrayList<>();
        for (String shop: HomeFragment.shops){
            int price = 0;
            for (String ourBasket: basketFinal) {
                if (ourBasket.contains(shop)) {
                    String[] m = ourBasket.split(" ");
                    int k = Integer.parseInt(a.get(basket.indexOf(String.join(" ", Arrays.copyOfRange(m, 1, m.length - 1)))).split(" ")[m.length - 2]);
                    price += Integer.parseInt(m[m.length - 1]) * k;
                }
            }
            prices.add(String.valueOf(price));
        }
        if (prices.size()!= 0) x.add(prices);
    }
    //функция создает список с продуктами и их ценами при нажатии на название магазина
    public static ArrayList<String> inShoP(String num){
        ArrayList<String> inShop = new ArrayList<>();
        for (String i: basketFinal) {
            if (i.contains(num)) {
                inShop.add(i);
            }
        }
        return inShop;
    }
    // метод изменяет параметры страницы (пересчитывает и показывает стоимости) когда пользователь выбирает еще товар
    public static String adding(int i){
        String [] m1 = a.get(i).split(" ");
        a.set(i, String.join(" ", Arrays.copyOfRange(m1, 0, m1.length - 1)) + " " + (1 + Integer.parseInt(m1[m1.length - 1])));
        getResult(basketFinal);
        ArrayList<String> m = new ArrayList<>();
        for (int j = 0; j < shops.size(); j++) {
            m.add("Магазин: " + shops.get(j) + ".\nЦена: " + x.get(x.size() - 1).get(j) + ".");
        }
        pricesToDisplay.add(m);

        ListViewAdapter adapter = new ListViewAdapter((Activity) root.getContext(), basket);
        ListViewAdapter adapter2 = new ListViewAdapter((Activity) root.getContext(), pricesToDisplay.get(pricesToDisplay.size() - 1));
        list.setAdapter(adapter);
        list2.setAdapter(adapter2);

        String k = a.get(i).split(" ")[m1.length - 1];
        return k;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}