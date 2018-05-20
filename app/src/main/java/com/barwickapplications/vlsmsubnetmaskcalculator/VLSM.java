package com.barwickapplications.vlsmsubnetmaskcalculator;

import java.util.HashMap;

class VLSM extends HashMap {

    private static HashMap<Integer, Integer> prefixAndNumberOfAddresses;
    private static HashMap<Integer, String> prefixAndSubnets;

    static {
        prefixAndNumberOfAddresses = new HashMap<Integer, Integer>();
        prefixAndNumberOfAddresses.put(8, 16777214);
        prefixAndNumberOfAddresses.put(9, 8388606);
        prefixAndNumberOfAddresses.put(10, 4194302);
        prefixAndNumberOfAddresses.put(11, 2097150);
        prefixAndNumberOfAddresses.put(12, 1048574);
        prefixAndNumberOfAddresses.put(13, 524286);
        prefixAndNumberOfAddresses.put(14, 262142);
        prefixAndNumberOfAddresses.put(15, 131070);
        prefixAndNumberOfAddresses.put(16, 65534);
        prefixAndNumberOfAddresses.put(17, 32766);
        prefixAndNumberOfAddresses.put(18, 16382);
        prefixAndNumberOfAddresses.put(19, 8190);
        prefixAndNumberOfAddresses.put(20, 4094);
        prefixAndNumberOfAddresses.put(21, 2046);
        prefixAndNumberOfAddresses.put(22, 1022);
        prefixAndNumberOfAddresses.put(23, 510);
        prefixAndNumberOfAddresses.put(24, 254);
        prefixAndNumberOfAddresses.put(25, 126);
        prefixAndNumberOfAddresses.put(26, 62);
        prefixAndNumberOfAddresses.put(27, 30);
        prefixAndNumberOfAddresses.put(28, 14);
        prefixAndNumberOfAddresses.put(29, 6);
        prefixAndNumberOfAddresses.put(30, 2);
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
    
    public static String getSubnetFromPrefix(int prefixLength) {
        return prefixAndSubnets.get(prefixLength);
    }

    public static int getNumberOfAddressesFromPrefix(int prefixLength) {
        return prefixAndNumberOfAddresses.get(prefixLength);
    }

    public static int getPrefixFromNumberOfAddresses(int numberOfAddresses) {
        int entryKey = 0;
        int closestHigherValue = 16777217;
        for (HashMap.Entry<Integer, Integer> entry : prefixAndNumberOfAddresses.entrySet())
        {
            int entryValue = entry.getValue();
            if (entryValue >= numberOfAddresses && entryValue < closestHigherValue) {
                closestHigherValue = entryValue;
                entryKey = entry.getKey();
            }
        }

        return entryKey;
    }

    public static int getPrefixFromSubnet(String subnet) {
        for (HashMap.Entry<Integer, String> entry : prefixAndSubnets.entrySet())
        {
            String entryValue = entry.getValue();
            if (entryValue.equals(subnet)) {
                return entry.getKey();
            }
        }
        return 0;
    }

    public static int getNumberOfAddressesFromSubnet(String subnet) {
        int prefix = getPrefixFromSubnet(subnet);
        return prefixAndNumberOfAddresses.get(prefix);
    }

    public static String getSubnetFromNumberOfAddresses(int numberOfAddresses) {
        int prefix = getPrefixFromNumberOfAddresses(numberOfAddresses);
        return prefixAndSubnets.get(prefix);
    }
}
