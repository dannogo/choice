package com.elantix.dopeapp;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by oleh on 5/11/16.
 */
public class ChainLink{
    MainActivity.Page fragment;
    ArrayList<Object> data = new ArrayList<>();
    HashMap<String, Object> bundleData = new HashMap();

    public ChainLink(MainActivity.Page fragment) {
        this.fragment = fragment;
    }
}