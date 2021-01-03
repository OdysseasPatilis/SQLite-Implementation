package com.example.sqliteimplementation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etName, etAge;
    Switch swActive;
    Button btnViewAll, btnAdd;
    ListView lvCustomerList;

    ArrayAdapter customerArrayAdapter;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        swActive = findViewById(R.id.swActive);
        btnAdd = findViewById(R.id.btnAdd);
        btnViewAll = findViewById(R.id.btnViewAll);
        lvCustomerList = findViewById(R.id.lvCustomerList);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        ShowCustomersOnListView(dataBaseHelper);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomerModel customerModel;
                try {
                    customerModel = new CustomerModel(-1,etName.getText().toString(),Integer.parseInt(etAge.getText().toString()),swActive.isChecked());
                    //Toast.makeText(MainActivity.this, customerModel.toString(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Error creating customer"+e, Toast.LENGTH_SHORT).show();
                    customerModel = new CustomerModel(-1,"error: ",0,false);

                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                boolean success = dataBaseHelper.addOne(customerModel);
                Toast.makeText(MainActivity.this, "Success: "+success, Toast.LENGTH_SHORT).show();
                ShowCustomersOnListView(dataBaseHelper);
            }
        });

        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                ShowCustomersOnListView(dataBaseHelper);
            }
        });

        lvCustomerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerModel clickedCustomers = (CustomerModel) parent.getItemAtPosition(position);
                dataBaseHelper.deleteOne(clickedCustomers);
                ShowCustomersOnListView(dataBaseHelper);
                Toast.makeText(MainActivity.this, "Deleted "+clickedCustomers.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowCustomersOnListView(DataBaseHelper dataBaseHelper2) {
        customerArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper2.getEveryone());
        lvCustomerList.setAdapter(customerArrayAdapter);
    }
}