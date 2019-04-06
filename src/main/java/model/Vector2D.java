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

//    public double theta(Vector2D dest){
//        double dotProduct = this.x * dest.getX()  + this.y * dest.getY() + this.z * dest.getZ();
//        double magnitude = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z) * Math.sqrt(dest.getX() * dest.getX() + dest.getY() * dest.getY() + dest.getZ() * dest.getZ());
//        return Math.acos(dotProduct / magnitude);
//    }

    // get direction
    // signed length of cross product between 2 vectors
    // P = (x2 - x1)(y3 - y1) - (y2 - y1)(x3 - x1)
    // 3 => that.2
    // 2 = 1
    // 1 = 2
    // > 0 means go right
    // < 0 means go left

    // true if left
    public EnumDirectionType getSignedLengthOfCrossProduct2D(Vector2D v) {
        double result = (this.x2 - this.x1) * (v.getY2() - this.y1) - (this.y2 - this.y1) * (v.getX2() - this.x1);
        boolean isStraight = false;


        double m = (this.y2 - this.y1) / (this.x2 - this.x1);
        double b = m * (-this.x1) + this.y1;





//        double b = this.y2 / (m * (this.x2 - this.x1));



        if (isStraight) {
            return EnumDirectionType.STRAIGHT;
        } else if (result < 0) {
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
