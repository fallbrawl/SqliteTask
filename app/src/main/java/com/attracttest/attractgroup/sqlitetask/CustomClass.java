package com.attracttest.attractgroup.sqlitetask;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by nexus on 20.09.2017.
 */
public class CustomClass implements Serializable {
    private String name;
    private String surname;
    private String date;
    private String desc;
    private String misc;
    private int id;
    private CustomClassInner customClassInner;

    public CustomClass(int id, String name, String surname, String date, String desc, String misc, CustomClassInner customClassInner) {
        this.name = name;
        this.surname = surname;
        this.date = date;
        this.desc = desc;
        this.misc = misc;
        this.id = id;
        this.customClassInner = customClassInner;
    }

    public static ArrayList<CustomClass> init() {
        ArrayList<CustomClass> result = new ArrayList<>();

        Random rnd;
        Date dt;
        long ms;

        for(int i = 0;i < 73;i++){

    // Get a new random instance, seeded from the clock
            rnd = new Random();

    // Get an Epoch value roughly between 1940 and 2010
    // -946771200000L = January 1, 1940
    // Add up to 70 years to it (using modulus on the next long)
            ms = -946771200000L + (Math.abs(rnd.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));

    // Construct a date
            dt = new Date(ms);
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
            String date = DATE_FORMAT.format(dt);

            CustomClassInner customClassInner = new CustomClassInner(i, "field1 " + i, "filed2 " + i,"field3 " + i);

            result.add(new CustomClass(i, "name " + i, "surname " + i, date, "description " + i, "misc " + i, customClassInner));

        }
        return result;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getDate() {
        return date;
    }

    public String getDesc() {
        return desc;
    }

    public String getMisc() {
        return misc;
    }

    public int getId() {
        return id;
    }

    public CustomClassInner getCustomClassInners() { return customClassInner; }

}
