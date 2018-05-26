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

import java.util.Arrays;

public class VlsmAllocation extends android.support.v4.app.Fragment {

    private Button btnCalculate;
    private EditText etPrefix;
    private EditText etSubnet;
    private EditText etGatewayAddress;
    private TextView tvResult;
    private RadioButton radPrefix;
    private RadioButton radSubnet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_vlsm_allocation, container, false);

        btnCalculate = view.findViewById(R.id.btnCalculate);
        etPrefix = view.findViewById(R.id.etPrefix);
        etSubnet = view.findViewById(R.id.etSubnet);
        etGatewayAddress = view.findViewById(R.id.etGatewayAddress);
        tvResult = view.findViewById((R.id.tvResult));
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
        etGatewayAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etGatewayAddressSelected();
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

    private void calculateValues() {
        // Triggered by Calculate button click, calculate non-input values
        if (gatewayIsValid(etGatewayAddress)) {
            if (radPrefix.isChecked()) {
                calculateSubnetAndNumberOfAddresses();
            } else if (radSubnet.isChecked()) {
                calculatePrefixAndNumberOfAddresses();
            }
        }
        else {
            Toast.makeText(getActivity(), "Please input a valid Gateway Address (e.g 192.168.1.0)",Toast.LENGTH_LONG).show();
        }
    }

    private void radPrefixSelected() {
        // Triggered by Prefix radio button selection
        clearFields(null);
        etPrefix.setEnabled(true);
        etSubnet.setEnabled(false);
        etPrefix.requestFocus();
    }

    private void radSubnetSelected() {
        // Triggered by Subnet radio button selection
        clearFields(null);
        etPrefix.setEnabled(false);
        etSubnet.setEnabled(true);
        etSubnet.requestFocus();
    }

    private void etGatewayAddressSelected() {
        // Clears non gateway EditText input fields
        clearFields(etGatewayAddress);
    }

    private void etPrefixSelected() {
        // Clears non prefix EditText input fields
        clearFields(etPrefix);
        tvResult.setText(getString(R.string.result_default));
    }

    private void etSubnetSelected() {
        // Clears non subnet input fields
        clearFields(etSubnet);
        tvResult.setText(getString(R.string.result_default));
    }

    private void clearFields(EditText etDoNotClear) {
        // Clears EditText input fields
        if (etPrefix != etDoNotClear) {
            etPrefix.setText("");
        }
        if (etSubnet != etDoNotClear) {
            etSubnet.setText("");
        }
        if (etGatewayAddress != etDoNotClear) {
            etGatewayAddress.setText("");
        }
        tvResult.setText(getString(R.string.result_default));
    }

    private void calculateSubnetAndNumberOfAddresses() {
        // Calculates Subnet and # Addresses from Prefix Length input
        try {
            int prefixLength = Integer.parseInt(etPrefix.getText().toString());

            etSubnet.setText(VLSM.getSubnetFromPrefix(prefixLength));

            int numberOfAddresses = VLSM.getNumberOfAddressesFromPrefix(prefixLength);

            calculateAddressRange(numberOfAddresses);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error: Check Gateway Address or Prefix Length (use 1-30)", Toast.LENGTH_SHORT).show();
            clearFields(null);

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
            clearFields(null);
        }
    }


    private void calculateAddressRange(int numberOfAddresses) {
        String[] strGatewayAddress = etGatewayAddress.getText().toString().split("[.]");

        int[] gatewayAddress = {
                Integer.parseInt(strGatewayAddress[0]),
                Integer.parseInt(strGatewayAddress[1]),
                Integer.parseInt(strGatewayAddress[2]),
                Integer.parseInt(strGatewayAddress[3]) };

        int[] firstAddress = Arrays.copyOf(gatewayAddress, gatewayAddress.length);
        firstAddress[3] = firstAddress[3] + 1;
        for (int i = 3; i > 0; i--) {
            if (firstAddress[i] > 255) {
                firstAddress[i - 1] = firstAddress[i - 1] + (firstAddress[i] / 256);
                firstAddress[i] = firstAddress[i] % 256;
            }
        }
        String strFirstAddress = String.valueOf(firstAddress[0]) + "." +
                String.valueOf(firstAddress[1]) + "." +
                String.valueOf(firstAddress[2]) + "." +
                String.valueOf(firstAddress[3]);

        int[] finalAddress = Arrays.copyOf(gatewayAddress, gatewayAddress.length);
        finalAddress[3] = finalAddress[3] + numberOfAddresses;
        for (int i = 3; i > 0; i--) {
            if (finalAddress[i] > 255) {
                finalAddress[i - 1] = finalAddress[i - 1] + (finalAddress[i] / 256);
                finalAddress[i] = finalAddress[i] % 256;
            }
        }
        String strFinalAddress = String.valueOf(finalAddress[0]) + "." +
                String.valueOf(finalAddress[1]) + "." +
                String.valueOf(finalAddress[2]) + "." +
                String.valueOf(finalAddress[3]);

        int[] broadcastAddress = Arrays.copyOf(finalAddress, finalAddress.length);
        broadcastAddress[3] = broadcastAddress[3] + 1;
        for (int i = 3; i > 0; i--) {

            if (broadcastAddress[i] > 255) {
                broadcastAddress[i - 1] = broadcastAddress[i - 1] + (broadcastAddress[i] / 256);
                broadcastAddress[i] = broadcastAddress[i] % 256;
            }
        }
        String strBroadcastAddress = String.valueOf(broadcastAddress[0]) + "." +
                String.valueOf(broadcastAddress[1]) + "." +
                String.valueOf(broadcastAddress[2]) + "." +
                String.valueOf(broadcastAddress[3]);

        if (checkAddressValid(firstAddress) && checkAddressValid(finalAddress) && checkAddressValid(broadcastAddress)) {
            tvResult.setText(String.format(getString(R.string.result_values), etGatewayAddress.getText(), strFirstAddress, strFinalAddress, strBroadcastAddress));
        }
        else {
            Toast.makeText(getActivity(),"IP Address out of bounds, ensure network range is large enough", Toast.LENGTH_LONG).show();
        }
    }

    private boolean gatewayIsValid(EditText inputGateway) {
        String[] splitInputGateway = (etGatewayAddress.getText().toString().split("[.]"));

        return (!(inputGateway.getText().toString().equals("")) &&
                (splitInputGateway.length == 4)
                && (splitInputGateway[3].equals("0")) &&
                (Integer.parseInt(splitInputGateway[0]) <= 255) &&
                (Integer.parseInt(splitInputGateway[1]) <= 255) &&
                (Integer.parseInt(splitInputGateway[2]) <= 255));

    }

    private boolean checkAddressValid(int[] ipAddress) {
        for (int i = 0; i <= 3; i++) {

            if (ipAddress[i] > 255) {
                return false;
            }
        }
        return true;
    }
}
