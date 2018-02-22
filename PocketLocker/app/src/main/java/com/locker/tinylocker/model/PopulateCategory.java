package com.locker.tinylocker.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Prasanna on 12/21/2016.
 */
public class PopulateCategory {
    public static PopulateCategory getInstance() {
        return new PopulateCategory();
    }

    public HashMap<String, ArrayList<String>> getPopulatedMap() {
        HashMap<String, ArrayList<String>> map = new HashMap<>();

        map.put("Tap to choose...", null);
        map.put("ATM card", getATMCardDetails());
        map.put("Email", getEmailDetails());
        map.put("Other login", getOtherLogin());
        map.put("IRCTC", getIrctc());
        map.put("Gmail", getGmail());
        map.put("Facebook", getFB());
        map.put("Skype", getSkype());
        map.put("Office computer", getSystemInfo());
        map.put("Office laptop", getSystemInfo());
        map.put("Home computer", getSystemInfo());
        map.put("Home laptop", getSystemInfo());
        map.put("Broadband", getBroadbandInfo());
        map.put("Internet banking", getNetBanking());
        map.put("Adhaar card",getAadhar());
        map.put("Passport",getPassport());
        map.put("PAN card",getPAN());
        map.put("Driving licence",getLiscence());

        return map;
    }

    private ArrayList<String> getLiscence() {
        ArrayList<String> obj = new ArrayList<>();
        obj.add("Driving licence number");

        return obj;
    }

    private ArrayList<String> getPAN() {
        ArrayList<String> obj = new ArrayList<>();
        obj.add("PAN number");

        return obj;
    }

    private ArrayList<String> getAadhar() {
        ArrayList<String> obj = new ArrayList<>();
        obj.add("Aadhaar number");

        return obj;
    }

    private ArrayList<String> getNetBanking() {
        ArrayList<String> obj = new ArrayList<>();
        obj.add("Username");
        obj.add("Password");
        obj.add("Transaction password");

        return obj;
    }

    private ArrayList<String> getBroadbandInfo() {
        ArrayList<String> obj = new ArrayList<>();
        obj.add("UserID or Username");
        obj.add("Password");
        obj.add("Configuration IP");

        return obj;
    }

    private ArrayList<String> getSystemInfo() {
        ArrayList<String> obj = new ArrayList<>();
        obj.add("System userID");
        obj.add("Password");
        obj.add("PIN");

        return obj;
    }

    private ArrayList<String> getSkype() {
        ArrayList<String> obj = new ArrayList<>();
        obj.add("Skype email or Phone");
        obj.add("Password");

        return obj;
    }

    private ArrayList<String> getFB() {
        ArrayList<String> obj = new ArrayList<>();
        obj.add("Email or Phone");
        obj.add("Password");

        return obj;
    }

    private ArrayList<String> getGmail() {
        ArrayList<String> obj = new ArrayList<>();
        obj.add("Email ID");
        obj.add("Password");

        return obj;
    }

    private ArrayList<String> getIrctc() {
        ArrayList<String> obj = new ArrayList<>();
        obj.add("UserID");
        obj.add("Password");

        return obj;
    }

    private ArrayList<String> getOtherLogin() {
        ArrayList<String> obj = new ArrayList<>();
        obj.add("Username");
        obj.add("Password");

        return obj;
    }

    private ArrayList<String> getEmailDetails() {
        ArrayList<String> obj = new ArrayList<>();
        obj.add("Email");
        obj.add("Password");

        return obj;
    }

    private ArrayList<String> getATMCardDetails() {
        ArrayList<String> obj = new ArrayList<>();
        obj.add("Card number");
        obj.add("Card expiry");
        obj.add("ATM PIN");
        obj.add("Transaction password");
        obj.add("CVV");

        return obj;
    }

    private ArrayList<String> getPassport() {
        ArrayList<String> obj = new ArrayList<>();
        obj.add("Passpory number");
        obj.add("Expiry date");
        obj.add("Place of issue");

        return obj;
    }
}