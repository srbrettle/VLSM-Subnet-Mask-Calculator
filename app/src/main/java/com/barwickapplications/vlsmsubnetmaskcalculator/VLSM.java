package com.barwickapplications.vlsmsubnetmaskcalculator;

import android.util.Size;

import java.util.HashMap;

public class VLSM extends HashMap {

    public static HashMap<Integer, Integer> prefixAndNumberOfAddresses;
    public static HashMap<Integer, String> prefixAndSubnets;

    static {
        prefixAndNumberOfAddresses = new HashMap<Integer, Integer>();
        prefixAndNumberOfAddresses.put(8, 16777216);
        prefixAndNumberOfAddresses.put(9, 8388608);
        prefixAndNumberOfAddresses.put(10, 4194304);
        prefixAndNumberOfAddresses.put(11, 2097152);
        prefixAndNumberOfAddresses.put(12, 1048576);
        prefixAndNumberOfAddresses.put(13, 524288);
        prefixAndNumberOfAddresses.put(14, 262144);
        prefixAndNumberOfAddresses.put(15, 131072);
        prefixAndNumberOfAddresses.put(16, 65536);
        prefixAndNumberOfAddresses.put(17, 32768);
        prefixAndNumberOfAddresses.put(18, 16384);
        prefixAndNumberOfAddresses.put(19, 8192);
        prefixAndNumberOfAddresses.put(20, 4096);
        prefixAndNumberOfAddresses.put(21, 2048);
        prefixAndNumberOfAddresses.put(22, 1024);
        prefixAndNumberOfAddresses.put(23, 512);
        prefixAndNumberOfAddresses.put(24, 256);
        prefixAndNumberOfAddresses.put(25, 128);
        prefixAndNumberOfAddresses.put(26, 64);
        prefixAndNumberOfAddresses.put(27, 32);
        prefixAndNumberOfAddresses.put(28, 16);
        prefixAndNumberOfAddresses.put(29, 8);
        prefixAndNumberOfAddresses.put(30, 4);
    }

    static {
        prefixAndSubnets = new HashMap<Integer, String>();
        prefixAndSubnets.put(8, "255.0.0.0");
        prefixAndSubnets.put(9, "255.128.0.0");
        prefixAndSubnets.put(10, "255.192.0.0");
        prefixAndSubnets.put(11, "255.224.0.0");
        prefixAndSubnets.put(12, "255.240.0.0");
        prefixAndSubnets.put(13, "255.248.0.0");
        prefixAndSubnets.put(14, "255.252.0.0");
        prefixAndSubnets.put(15, "255.254.0.0");
        prefixAndSubnets.put(16, "255.255.0.0");
        prefixAndSubnets.put(17, "255.255.128.0");
        prefixAndSubnets.put(18, "255.255.192.0");
        prefixAndSubnets.put(19, "255.255.224.0");
        prefixAndSubnets.put(20, "255.255.240.0");
        prefixAndSubnets.put(21, "255.255.248.0");
        prefixAndSubnets.put(22, "255.255.252.0");
        prefixAndSubnets.put(23, "255.255.254.0");
        prefixAndSubnets.put(24, "255.255.255.0");
        prefixAndSubnets.put(25, "255.255.255.128");
        prefixAndSubnets.put(26, "255.255.255.192");
        prefixAndSubnets.put(27, "255.255.255.224");
        prefixAndSubnets.put(28, "255.255.255.240");
        prefixAndSubnets.put(29, "255.255.255.248");
        prefixAndSubnets.put(30, "255.255.255.252");
    }
    
    public String getSubnetFromPrefix(int prefixLength) {
        return prefixAndSubnets.get(prefixLength);
    }

    public int getNumberOfAddressesFromPrefix(int prefixLength) {
        return prefixAndNumberOfAddresses.get(prefixLength);
    }

    public int getPrefixFromNumberOfAddresses(int numberOfAddresses) {
        int closestHigherValue = 16777217;
        for (HashMap.Entry<Integer, Integer> entry : prefixAndNumberOfAddresses.entrySet())
        {
            int entryValue = entry.getValue();
            if (entryValue > numberOfAddresses && entryValue < closestHigherValue) {
                return entry.getKey();
            }
        }
        return 0;
    }

    public int getPrefixFromSubnet(String subnet) {
        for (HashMap.Entry<Integer, String> entry : prefixAndSubnets.entrySet())
        {
            String entryValue = entry.getValue();
            if (entryValue == subnet) {
                return entry.getKey();
            }
        }
        return 0;
    }

    public int getNumberOfAddressesFromSubnet(String subnet) {
        int prefix = getPrefixFromSubnet(subnet);
        return prefixAndNumberOfAddresses.get(prefix);
    }

    public String getSubnetFromumberOfAddresses(int numberOfAddresses) {
        int prefix = getPrefixFromNumberOfAddresses(numberOfAddresses);
        return prefixAndSubnets.get(prefix);
    }
}
