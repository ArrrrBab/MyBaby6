package com.example.mybaby6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.mybaby6.ui.dashboard.DashboardFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class inShop extends AppCompatActivity implements MyInterface {
    int price = 0;
    ArrayList<String> inShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_shop);
        Intent intent = getIntent();
        String num = intent.getStringExtra("num");

        ImageButton imageButton = findViewById(R.id.imageButton);
        //go back to homefragment
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Main_Home_Page.class);
                startActivity(intent);
            }
        };
        imageButton.setOnClickListener(onClickListener);
        final ListView list = findViewById(R.id.list);
        final ListView list2 = findViewById(R.id.zameny);
        inShop = DashboardFragment.inShoP(num);
        ArrayList<String> zamen = zamena(inShop);
        ArrayList<String > inShopQuant = quant(inShop);
        final ArrayAdapter[] adapter = {new ArrayAdapter(inShop.this, R.layout.list_item_layout, inShopQuant)};
        list.setAdapter(adapter[0]);
        final ArrayAdapter[] adapter2 = {new ArrayAdapter(inShop.this, R.layout.list_item_layout, zamen)};
        list2.setAdapter(adapter2[0]);
        TextView textView = findViewById(R.id.textView5);
        textView.setText("Цена: " + x.get(x.size() - 1).get(shops.indexOf(num))  + " р.");


        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                price = 0;
                String s = (String) adapter2[0].getItem(position);
                String typeNow = s.split(" ")[1];
                String s1 = "";
                for (String in: inShop){
                    if (in.split(" ")[1].equals(typeNow)) s1 = in;
                }
                if (s1.equals("")) {
                    s1 = s;
                    zamen.remove(s1);
                    inShop.add(s1);
                }
                else {
                    zamen.set(position, s1);
                    inShop.set(inShop.indexOf(s1), s);
                }
                adapter[0] = new ArrayAdapter(inShop.this, R.layout.list_item_layout, inShop);
                list.setAdapter(adapter[0]);

                adapter2[0] = new ArrayAdapter(inShop.this, R.layout.list_item_layout, zamen);
                list2.setAdapter(adapter2[0]);
                for (String ourBasket: inShop) {
                    String[] m = ourBasket.split(" ");
                    price += Integer.parseInt(m[m.length - 1]);
                }
                textView.setText("Цена: " + Integer.toString(price) + " р.");
            }
        });
        Button button = findViewById(R.id.link);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setLink();
            }
        });
    }
    public ArrayList<String> zamena(ArrayList<String> inShop){
        ArrayList<String> a = new ArrayList<>(), b = new ArrayList<>(), dif = new ArrayList<>();
        String shop = "";
        for (String givenProd: inShop) {
            String[] m = givenProd.split(" ");
            shop = m[0];
            for (ArrayList<String> prod : allProductsPrice) {
                if (m[0].equals(prod.get(0)) &&
                        m[1].equals(prod.get(1).split(" ")[0]) &&
                        Integer.parseInt(m[m.length - 1]) > Integer.parseInt(prod.get(prod.size() - 1))
                        && !a.contains(String.join(" ", prod)) && !inShop.contains(String.join(" ", prod))) {
                    a.add(String.join(" ", prod));
                }
            }
        }
        for (int i = 0; i < inShop.size(); i++){
            b.add(String.join(" ", Arrays.copyOfRange(inShop.get(i).split(" "), 1, inShop.get(i).split(" ").length - 1)));
        }
        //если товара нет в этом магазине, то все равно предложи альтераниву на него
        for (int i = 0; i < basket.size(); i++){
            if (!b.contains(basket.get(i))){
                dif.add(basket.get(i));
                String givenProd = basket.get(i);
                String[] m = givenProd.split(" ");
                for (ArrayList<String> prod : allProductsPrice) {
                    if (shop.equals(prod.get(0)) &&
                            m[0].equals(prod.get(1).split(" ")[0])
                            && !a.contains(String.join(" ", prod)) && !inShop.contains(String.join(" ", prod))) {
                        a.add(String.join(" ", prod));
                    }
                }
            }
        }

        return a;
    }
    public ArrayList<String> quant(ArrayList<String> inShopppp){
        ArrayList<String> quanty = new ArrayList<>();

        for (String prodInBasket: DashboardFragment.a) {
            for (String prod : inShopppp) {
                if (prod.contains(String.join(" ", Arrays.copyOfRange(prodInBasket.split(" "), 0,prodInBasket.split(" ").length - 1)))){
                    quanty.add(prod + " " + prodInBasket.split(" ")[prodInBasket.split(" ").length - 1] + " шт.");
                }
            }
        }
        return quanty;
    }
    public void setLink(){
        for (ArrayList<String> k: allProductsLink){
            for (String p: inShop){
                if (String.join(" ", k).contains(p)){
                    doSelenium(k.get(k.size() - 1));
                }
            }
        }
    }
    public void doSelenium(String link){
        // 1. Start the Python instance if it isn't already running.
        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(inShop.this));
        }

// 2. Obtain the python instance
        Python py = Python.getInstance();

// 3. Declare some Python code that will be interpreted
// In our case, the fibonacci sequence


// 4. Obtain the system's input stream (available from Chaquopy)
        PyObject pyo = py.getModule("main");
        PyObject obj = pyo.callAttr("linkk");
// Obtain the interpreter.py module

    }
}
