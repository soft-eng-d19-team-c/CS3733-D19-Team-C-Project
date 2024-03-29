package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class NodeTest {
    private Node n1;
    private Node n2;
    private Field f;


    @Before
    public void setUp() throws Exception {
        n1 = new Node("ACONF00102", 1580, 2538, "2", "BTM", "HALL", "Hall", "Hall");
        n2 = new Node("ACONF00103", 1648, 2968);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void toStringtest() {
        assertTrue(n1.toString().equals("Node{ID='ACONF00102\', x=1580, y=2538, floor='2\', building='BTM\', nodeType='HALL\', longName='Hall\', shortName='Hall\'}"));
        assertTrue(n2.toString().equals("Node{ID='ACONF00103\', x=1648, y=2968, floor='null\', building='null\', nodeType='null\', longName='null\', shortName='null\'}"));
    }

    @Test
    public void update() {
    }

    @Test
    public void getID() { assertTrue(n1.getID().equals("ACONF00102")); }

    @Test
    public void getX() { assertEquals(1580, n1.getX()); }

    @Test
    public void getY() { assertEquals(2538, n1.getY()); }

    @Test
    public void getFloor() { assertTrue(n1.getFloor().equals("2")); }

    @Test
    public void getBuilding() { assertTrue(n1.getBuilding().equals("BTM")); }

    @Test
    public void getNodeType() { assertTrue(n1.getNodeType().equals("HALL")); }

    @Test
    public void getLongName() { assertTrue(n1.getLongName().equals("Hall")); }

    @Test
    public void getShortName() { assertTrue(n1.getShortName().equals("Hall")); }

    @Test
    public void setFloor() throws NoSuchFieldException, IllegalAccessException {
        n2.setFloor("3");
        f = n2.getClass().getDeclaredField("floor");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "3", f.get(n2));

        n2.setFloor("L1");
        f = n2.getClass().getDeclaredField("floor");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "L1", f.get(n2));

        /**
         * If the developer tries to enter "l2" instead of "L2",
         * the setter should be able to recognize it and change it automatically
         */
        n2.setFloor("l2");
        f = n2.getClass().getDeclaredField("floor");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "L2", f.get(n2));

        /**
         * There is no floor 4, expect the setter to change nothing and give a notification
         */
        n1.setFloor("4");
        f = n1.getClass().getDeclaredField("floor");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "2", f.get(n1));

        /**
         * There is no floor 0, expect the setter to change nothing and give a notification
         */
        n1.setFloor("0");
        f = n1.getClass().getDeclaredField("floor");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "2", f.get(n1));

        /**
         * floor "-1" and "-2" is an unclear expression, expect the setter to change nothing and give a notification
         */
        n1.setFloor("-1");
        f = n1.getClass().getDeclaredField("floor");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "2", f.get(n1));
    }

    @Test
    public void setBuilding() throws NoSuchFieldException, IllegalAccessException {
        n2.setBuilding("BTM");
        f = n2.getClass().getDeclaredField("building");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "BTM", f.get(n2));

        n2.setBuilding("15 Francis");
        f = n2.getClass().getDeclaredField("building");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "15 Francis", f.get(n2));

        n2.setBuilding("45 Francis");
        f = n2.getClass().getDeclaredField("building");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "45 Francis", f.get(n2));

        n2.setBuilding("Shapiro");
        f = n2.getClass().getDeclaredField("building");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "Shapiro", f.get(n2));

        n2.setBuilding("Tower");
        f = n2.getClass().getDeclaredField("building");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "Tower", f.get(n2));

        /**
         * if the developer tries to enter a building that does not exist
         * there should be a notification
         * and the field should not change
         */
        n1.setBuilding("46 Francis");
        f = n1.getClass().getDeclaredField("building");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "BTM", f.get(n1));
    }

    @Test
    public void setNodeType() throws NoSuchFieldException, IllegalAccessException {
        n2.setNodeType("BATH");
        f = n2.getClass().getDeclaredField("nodeType");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "BATH", f.get(n2));

        n2.setNodeType("CONF");
        f = n2.getClass().getDeclaredField("nodeType");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "CONF", f.get(n2));

        n2.setNodeType("DEPT");
        f = n2.getClass().getDeclaredField("nodeType");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "DEPT", f.get(n2));

        n2.setNodeType("ELEV");
        f = n2.getClass().getDeclaredField("nodeType");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "ELEV", f.get(n2));

        n2.setNodeType("EXIT");
        f = n2.getClass().getDeclaredField("nodeType");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "EXIT", f.get(n2));

        n2.setNodeType("HALL");
        f = n2.getClass().getDeclaredField("nodeType");
        f.setAccessible(true);
        assertEquals("Fields didn't match", "HALL", f.get(n2));
    }

    @Test
    public void setLongName() throws NoSuchFieldException, IllegalAccessException {
        n2.setLongName("BTM Conference Center");
        final Field field = n2.getClass().getDeclaredField("longName");
        field.setAccessible(true);
        assertEquals("Fields didn't match", field.get(n2), "BTM Conference Center");

        n2.setLongName("BTM Conference Center");
        final Field field1 = n2.getClass().getDeclaredField("longName");
        field1.setAccessible(true);
        assertEquals("Fields didn't match", field1.get(n2), "BTM Conference Center");
    }

    @Test
    public void setShortName() throws NoSuchFieldException, IllegalAccessException {
        n2.setShortName("BTM Conference");
        final Field field = n2.getClass().getDeclaredField("shortName");
        field.setAccessible(true);
        assertEquals("Fields didn't match", field.get(n2), "BTM Conference");

        n2.setShortName("BTM Conference");
        final Field field1 = n2.getClass().getDeclaredField("shortName");
        field1.setAccessible(true);
        assertEquals("Fields didn't match", field1.get(n2), "BTM Conference");
    }

    @Test
    public void setX() {
    }

    @Test
    public void setY() {
    }
}