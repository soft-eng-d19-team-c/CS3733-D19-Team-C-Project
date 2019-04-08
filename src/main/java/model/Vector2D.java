package model;

public class Vector2D {
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public Vector2D(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2 - x1;
        this.y2 = y2 - y1;
    }

    public EnumDirectionType getDirection(Vector2D v) {
        double dot = this.x2 * v.getX2() + this.y2 * v.getY2(); // dot product
        double det = this.x2 * v.getY2() - this.y2 * v.getX2(); // determinant

        double angle = Math.atan2(det, dot); // angle in radians from [-pi/2, pi/2]

//        System.out.println("x1: " + x2);
//        System.out.println("y1: " + y2);
//        System.out.println("x2: " + v.getX2());
//        System.out.println("y2: " + v.getY2());
//        System.out.println("Angle in radians: " + angle);
//        System.out.println("Angle in degrees: " + angle * 180 / Math.PI);

        if (Math.abs(angle) >= Math.PI  - Math.PI / 12) {
            return EnumDirectionType.STRAIGHT;
        } else if (angle < 0) {
            return EnumDirectionType.RIGHT;
        } else {
            return EnumDirectionType.LEFT;
        }
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }
}
