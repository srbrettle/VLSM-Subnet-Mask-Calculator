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

import java.net.InetAddress;

public class SubnetCalculator  extends android.support.v4.app.Fragment {

    private Button btnCalculate;
    private EditText etPrefix;
    private EditText etSubnet;
    private EditText etGatewayAddress;
    private TextView tvBlockSize;
    private RadioButton radPrefix;
    private RadioButton radSubnet;
    private RadioButton radNumberOfAddresses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_subnet_calculator, container, false);

        btnCalculate = view.findViewById(R.id.btnCalculate);
        etPrefix = view.findViewById(R.id.etPrefix);
        etSubnet = view.findViewById(R.id.etSubnet);
        etGatewayAddress = view.findViewById(R.id.etGatewayAddress);
        tvBlockSize = view.findViewById((R.id.tvBlockSize));
        radPrefix = view.findViewById(R.id.radPrefix);
        radSubnet = view.findViewById(R.id.radSubnet);

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

        return view;
    }

    public void calculateValues() {
        // Triggered by Calculate button click, calculate non-input values
        if (!(etGatewayAddress.getText().toString().equals("")) && (etGatewayAddress.getText().toString().split(".").length == 4)) {
            if (radPrefix.isChecked()) {
                calculateSubnetAndNumberOfAddresses();
            } else if (radSubnet.isChecked()) {
                calculatePrefixAndNumberOfAddresses();
            }
        }
        else {
            Toast.makeText(getActivity(), "Please input a valid Gateway Address",Toast.LENGTH_LONG).show();
        }
    }

    public void radPrefixSelected() {
        // Triggered by Prefix radio button selection
        clearFields();
        etPrefix.setEnabled(true);
        etSubnet.setEnabled(false);
        etPrefix.requestFocus();
    }

    public void radSubnetSelected() {
        // Triggered by Subnet radio button selection
        clearFields();
        etPrefix.setEnabled(false);
        etSubnet.setEnabled(true);
        etSubnet.requestFocus();
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
        etGatewayAddress.setText("");
        tvBlockSize.setText(String.format("Gateway Address = {1}\nAvailable range: {2} - {3}\nBroadcast Addresses:\n{4}", etGatewayAddress.getText()));
    }

    private void calculateSubnetAndNumberOfAddresses() {
        // Calculates Subnet and # Addresses from Prefix Length input
        try {
            int prefixLength = Integer.parseInt(etPrefix.getText().toString());

            etSubnet.setText(VLSM.getSubnetFromPrefix(prefixLength));

            int numberOfAddresses = VLSM.getNumberOfAddressesFromPrefix(prefixLength);

            calculateAddressRange(numberOfAddresses);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error: Check Gateway Address or Prefix Length (use 8-30)", Toast.LENGTH_SHORT).show();
            clearFields();

        }
    }

    private void calculatePrefixAndNumberOfAddresses() {
        // Calculates Prefix Length and # Addresses from Subnet input
        try {
            String subnet = etSubnet.getText().toString();

            etPrefix.setText(String.valueOf(VLSM.getPrefixFromSubnet(subnet)));

            int numberOfAddresses = VLSM.getNumberOfAddressesFromSubnet(subnet);

            calculateAddressRange(numberOfAddresses);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error: Check Gateway Address or Subnet used", Toast.LENGTH_SHORT).show();
            clearFields();
        }
    }


    public void calculateAddressRange(int numberOfAddresses) {
        String[] strGatewayAddress = etGatewayAddress.getText().toString().split(".");

        int[] gatewayAddress = {
                Integer.parseInt(strGatewayAddress[0]),
                Integer.parseInt(strGatewayAddress[1]),
                Integer.parseInt(strGatewayAddress[2]),
                Integer.parseInt(strGatewayAddress[3]) };

        int[] firstAddress = gatewayAddress;
        firstAddress[3] = firstAddress[3] + 1;
        for (int i = 3; i >= 0; i--) {
            if (firstAddress[i] > 255) {
                firstAddress[i - 1] = firstAddress[i - 1] + (firstAddress[3] / 256);
                firstAddress[i] = firstAddress[i] % 256;
            }
        }
        String strFirstAddress = String.valueOf(firstAddress[0]) + "." +
                String.valueOf(firstAddress[1]) + "." +
                String.valueOf(firstAddress[2]) + "." +
                String.valueOf(firstAddress[3]);

        int[] finalAddress = gatewayAddress;
        finalAddress[3] = finalAddress[3] + numberOfAddresses;
        for (int i = 3; i >= 0; i--) {
            if (finalAddress[i] > 255) {
                finalAddress[i - 1] = finalAddress[i - 1] + (finalAddress[3] / 256);
                finalAddress[i] = finalAddress[i] % 256;
            }
        }
        String strFinalAddress = String.valueOf(finalAddress[0]) + "." +
                String.valueOf(finalAddress[1]) + "." +
                String.valueOf(finalAddress[2]) + "." +
                String.valueOf(finalAddress[3]);

        int[] broadcastAddress = finalAddress;
        broadcastAddress[3] = broadcastAddress[3] + 1;
        for (int i = 3; i >= 0; i--) {
            if (broadcastAddress[i] > 255) {
                broadcastAddress[i - 1] = broadcastAddress[i - 1] + (broadcastAddress[3] / 256);
                broadcastAddress[i] = broadcastAddress[i] % 256;
            }
        }
        String strBroadcastAddress = String.valueOf(broadcastAddress[0]) + "." +
                String.valueOf(broadcastAddress[1]) + "." +
                String.valueOf(broadcastAddress[2]) + "." +
                String.valueOf(broadcastAddress[3]);


        tvBlockSize.setText(String.format("Gateway Address = {1}\nAvailable range: {2} - {3}\nBroadcast Addresses:\n{4}", etGatewayAddress.getText(), strFirstAddress, strFinalAddress, strBroadcastAddress));
    }
}
