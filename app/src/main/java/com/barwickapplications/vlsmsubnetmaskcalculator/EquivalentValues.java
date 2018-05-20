package com.barwickapplications.vlsmsubnetmaskcalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class EquivalentValues extends android.support.v4.app.Fragment {
    private Button btnCalculate;
    private EditText etPrefix;
    private EditText etSubnet;
    private EditText etNumberOfAddresses;
    private TextView tvBlockSize;
    private RadioButton radPrefix;
    private RadioButton radSubnet;
    private RadioButton radNumberOfAddresses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_equivalent_values, container, false);

        btnCalculate = view.findViewById(R.id.btnCalculate);
        etPrefix = view.findViewById(R.id.etPrefix);
        etSubnet = view.findViewById(R.id.etSubnet);
        etNumberOfAddresses = view.findViewById(R.id.etNumberOfAddresses);
        tvBlockSize = view.findViewById((R.id.tvBlockSize));
        radPrefix = view.findViewById(R.id.radPrefix);
        radSubnet = view.findViewById(R.id.radSubnet);
        radNumberOfAddresses = view.findViewById(R.id.radNumberOfAddresses);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateValues();
            }
        });
        radPrefix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radPrefixSelected();
            }
        });
        radSubnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radSubnetSelected();
            }
        });
        radNumberOfAddresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radNumberOfAddressesSelected();
            }
        });
        etPrefix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPrefixSelected();
            }
        });
        etSubnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSubnetSelected();
            }
        });
        etNumberOfAddresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNumberOfAddressesSelected();
            }
        });

        return view;
    }

    public void calculateValues() {
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

    public void radPrefixSelected() {
        // Triggered by Prefix radio button selection
        clearFields();
        etPrefix.setEnabled(true);
        etSubnet.setEnabled(false);
        etNumberOfAddresses.setEnabled(false);
        etPrefix.requestFocus();
    }

    public void radSubnetSelected() {
        // Triggered by Subnet radio button selection
        clearFields();
        etPrefix.setEnabled(false);
        etSubnet.setEnabled(true);
        etNumberOfAddresses.setEnabled(false);
        etSubnet.requestFocus();
    }

    public void radNumberOfAddressesSelected() {
        // Triggered by # Addresses radio button selection
        clearFields();
        etPrefix.setEnabled(false);
        etSubnet.setEnabled(false);
        etNumberOfAddresses.setEnabled(true);
        etNumberOfAddresses.requestFocus();
    }

    public void etPrefixSelected() {
        radPrefix.performClick();
    }

    public void etSubnetSelected() {
        radSubnet.performClick();
    }

    public void etNumberOfAddressesSelected() {
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
            Toast.makeText(getActivity(), "Invalid prefix used (Use 8 - 30)", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Invalid subnet used", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Invalid # Addresses used (Max is 16777214)", Toast.LENGTH_SHORT).show();
            clearFields();
        }
    }
}
