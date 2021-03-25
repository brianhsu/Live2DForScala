package moe.brianhsu.live2d.core;

import com.sun.jna.Structure;
import com.sun.jna.Pointer;

import java.util.List;
import java.util.Arrays;

/**
 * The JNA bridge for access CSM vector C structure.
 *
 * User should NEVER use this class directly.
 *
 */
public class CsmVector extends Structure {
    public static int SIZE = 8;
    public float x;
    public float y;

    public CsmVector() { super(); }

    public CsmVector(Pointer p) { 
        super(p);
	    this.autoRead();
    }

    public float getX() {
        this.autoRead();
        return this.x;
    }

    public float getY() {
        this.autoRead();
        return this.y;
    }

    protected List<String> getFieldOrder() {
        return Arrays.asList("x", "y");
    }

}
