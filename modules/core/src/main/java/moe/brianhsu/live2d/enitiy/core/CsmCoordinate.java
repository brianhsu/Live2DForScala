package moe.brianhsu.live2d.enitiy.core;

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
public class CsmCoordinate extends Structure {
    public static int SIZE = 8;
    public float x;
    public float y;

    public CsmCoordinate() { super(); }

    public CsmCoordinate(Pointer p) {
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
