package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class Vector2DTest {

    @Test
    public void testVectorThing() {
        Vector2D v1 = new Vector2D(0,0,1,0);
        Vector2D v2 = new Vector2D(0,0,0,1);
        assertEquals(EnumDirectionType.LEFT, v1.getDirection(v2));
    }
    @Test
    public void testVectorThingRight() {
        Vector2D v1 = new Vector2D(0,0,1,0);
        Vector2D v2 = new Vector2D(0,0,0,1);
        assertEquals(EnumDirectionType.RIGHT, v2.getDirection(v1));
    }


    @Test
    public void testVectorThing2() {
        Vector2D v1 = new Vector2D(0,0,10,0);
        Vector2D v2 = new Vector2D(0,0,0,10);
        assertEquals(EnumDirectionType.LEFT, v1.getDirection(v2));
    }
    @Test
    public void testVectorThingRight2() {
        Vector2D v1 = new Vector2D(0,0,10,0);
        Vector2D v2 = new Vector2D(0,0,0,10);
        assertEquals(EnumDirectionType.RIGHT, v2.getDirection(v1));
    }

    @Test
    public void testVectorThingStraightish() {
        Vector2D v1 = new Vector2D(0,0,1000,100);
        Vector2D v2 = new Vector2D(0,0,1000,98);
        assertEquals(EnumDirectionType.STRAIGHT, v2.getDirection(v1));
    }
}
