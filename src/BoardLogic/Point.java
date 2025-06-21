package BoardLogic;

public class Point {
    /// Fields
    private int x;
    private int y;



    ///  Constructors
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }



    /// Methods
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }


    // Setters
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }


    // Help Methods
    public double distance(Point p) {
        return Math.sqrt(Math.pow(this.x - p.getX(), 2) + Math.pow(this.y - p.getY(), 2));
    }
    public boolean equals(Point p)
    {
        return (this.x==p.x & y==p.y);
    }


    // ToString
    @Override
    public String toString() { return "(" + x + ", " + y + ")"; }

}
