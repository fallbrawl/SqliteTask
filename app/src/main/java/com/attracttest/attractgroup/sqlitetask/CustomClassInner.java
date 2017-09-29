package com.attracttest.attractgroup.sqlitetask;

import java.io.Serializable;

/**
 * Created by nexus on 26.09.2017.
 */
public class CustomClassInner implements Serializable {
    private String field1;
    private String field2;
    private String field3;
    private String id;

    public CustomClassInner(String id, String field1, String field2, String field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.id = id;
    }

    public String getField1() {
        return field1;
    }

    public String getField2() {
        return field2;
    }

    public String getField3() {
        return field3;
    }

    public String getId() {
        return id;
    }
}
