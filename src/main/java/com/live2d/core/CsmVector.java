package com.live2d.core;

import com.sun.jna.Structure;
import com.sun.jna.Pointer;

import java.util.List;
import java.util.Arrays;

public class CsmVector extends Structure {
    public static int SIZE = 8;
    public float x;
    public float y;

    protected CsmVector() { super(); }

    public CsmVector(Pointer p) { 
         super(p);
	 this.autoRead();
    }

    protected List getFieldOrder() {
        return Arrays.asList(new String[] {"x", "y"});
    }

}
