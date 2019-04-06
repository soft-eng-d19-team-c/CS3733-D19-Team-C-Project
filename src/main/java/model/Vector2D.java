package model;

public class Vector2D {
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public Vector2D(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public EnumDirectionType getDirection(Vector2D v) {
        double dot = this.x2 * v.getX2() + this.y2 * v.getY2(); // dot product
        double det = this.x2 * v.getY2() - this.y2 * v.getX2(); // determinant

        double angle = Math.atan2(det, dot); // angle in radians from [-pi/2, pi/2]

        if (Math.abs(angle) <= Math.PI / 12) { // +/- 15 degrees
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
