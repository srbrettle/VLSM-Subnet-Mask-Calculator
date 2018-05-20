package com.barwickapplications.vlsmsubnetmaskcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etPrefix;
    private EditText etSubnet;
    private EditText etNumberOfAddresses;
    private TextView tvBlockSize;
    private RadioButton radPrefix;
    private RadioButton radSubnet;
    private RadioButton radNumberOfAddresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPrefix = findViewById(R.id.etPrefix);
        etSubnet = findViewById(R.id.etSubnet);
        etNumberOfAddresses = findViewById(R.id.etNumberOfAddresses);
        tvBlockSize = findViewById((R.id.tvBlockSize));
        radPrefix = findViewById(R.id.radPrefix);
        radSubnet = findViewById(R.id.radSubnet);
        radNumberOfAddresses = findViewById(R.id.radNumberOfAddresses);
    }

    public void calculateValues(View v) {
        // Triggered by Calculate button click, calculate non-input values
        if(radPrefix.isChecked())
        {
            calculateSubnetAndNumberOfAddresses();
        }
        else if(radSubnet.isChecked())
        {
            calculatePrefixAndNumberOfAddresses();
        }
        else if(radNumberOfAddresses.isChecked())
        {
            calculatePrefixAndSubnet();
        }
    }

    public void radPrefixSelected(View v) {
        // Triggered by Prefix radio button selection
        clearFields();
        etPrefix.setEnabled(true);
        etSubnet.setEnabled(false);
        etNumberOfAddresses.setEnabled(false);
        etPrefix.requestFocus();
    }

    public void radSubnetSelected(View v) {
        // Triggered by Subnet radio button selection
        clearFields();
        etPrefix.setEnabled(false);
        etSubnet.setEnabled(true);
        etNumberOfAddresses.setEnabled(false);
        etSubnet.requestFocus();
    }

    public void radNumberOfAddressesSelected(View v) {
        // Triggered by # Addresses radio button selection
        clearFields();
        etPrefix.setEnabled(false);
        etSubnet.setEnabled(false);
        etNumberOfAddresses.setEnabled(true);
        etNumberOfAddresses.requestFocus();
    }

    public void etPrefixSelected(View v) {
        radPrefix.performClick();
    }

    public void etSubnetSelected(View v) {
        radSubnet.performClick();
    }

    public void etNumberOfAddressesSelected(View v) {
        radNumberOfAddresses.performClick();
    }

    private void clearFields() {
        // Clears all EditText input fields
        etPrefix.setText("");
        etSubnet.setText("");
        etNumberOfAddresses.setText("");
        tvBlockSize.setText("Total Block Size used\n(Including Gateway and Broadcast Addresses):\nN/A");
    }

    private void calculateSubnetAndNumberOfAddresses() {
        // Calculates Subnet and # Addresses from Prefix Length input
        try {
            int prefixLength = Integer.parseInt(etPrefix.getText().toString());
            etSubnet.setText(VLSM.getSubnetFromPrefix(prefixLength));
            int numberOfAddresses = VLSM.getNumberOfAddressesFromPrefix(prefixLength);
            etNumberOfAddresses.setText(String.valueOf(numberOfAddresses));

            tvBlockSize.setText(String.format(getString(R.string.block_size),String.valueOf(numberOfAddresses + 2)));
        } catch (Exception e) {
            Toast.makeText(this, "Invalid prefix used (Use 8 - 30)", Toast.LENGTH_SHORT).show();
            clearFields();

        }
    }

    private void calculatePrefixAndNumberOfAddresses() {
        // Calculates Prefix Length and # Addresses from Subnet input
        try {
            String subnet = etSubnet.getText().toString();
            //Do Validation to ensure subnet
            etPrefix.setText(String.valueOf(VLSM.getPrefixFromSubnet(subnet)));
            int numberOfAddresses = VLSM.getNumberOfAddressesFromSubnet(subnet);
            etNumberOfAddresses.setText(String.valueOf(numberOfAddresses));

            tvBlockSize.setText(String.format(getString(R.string.block_size),String.valueOf(numberOfAddresses + 2)));
        } catch (Exception e) {
            Toast.makeText(this, "Invalid subnet used", Toast.LENGTH_SHORT).show();
            clearFields();
        }
    }

    private void calculatePrefixAndSubnet() {
        // Calculates Prefix Length and Subnet from # Addresses input
        try {
            int numberOfAddresses = Integer.parseInt(etNumberOfAddresses.getText().toString());
            int prefix = VLSM.getPrefixFromNumberOfAddresses(numberOfAddresses);
            etPrefix.setText(String.valueOf(prefix));
            etSubnet.setText(VLSM.getSubnetFromNumberOfAddresses(numberOfAddresses));

            int numberOfAddressesAvailable = VLSM.getNumberOfAddressesFromPrefix(prefix);

            tvBlockSize.setText(String.format(getString(R.string.block_size),String.valueOf(numberOfAddressesAvailable + 2)));
        } catch (Exception e) {
            Toast.makeText(this, "Invalid # Addresses used (Max is 16777214)", Toast.LENGTH_SHORT).show();
            clearFields();
        }
    }
}
