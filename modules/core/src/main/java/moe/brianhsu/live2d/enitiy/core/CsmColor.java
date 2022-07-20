package moe.brianhsu.live2d.enitiy.core;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * The JNA bridge for access CSM vector C structure.
 *
 * User should NEVER use this class directly.
 *
 */
public class CsmColor extends Structure {
    public static int SIZE = 16;
    public float red;
    public float green;
    public float blue;
    public float alpha;

    public CsmColor() { super(); }

    public CsmColor(Pointer p) {
        super(p);
	    this.autoRead();
    }

    public float getRed() {
        this.autoRead();
        return this.red;
    }

    public float getGreen() {
        this.autoRead();
        return this.green;
    }
    public float getBlue() {
        this.autoRead();
        return this.blue;
    }

    public float getAlpha() {
        this.autoRead();
        return this.alpha;
    }

    protected List<String> getFieldOrder() {
        return Arrays.asList("red", "green", "blue", "alpha");
    }

}
