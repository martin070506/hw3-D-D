package EnemyTypes;

import Unit_Logic.UnitVisitor;

public class Trap extends Enemy{
    /// Fields
    private int visibilityTime;
    private int invisibilityTime;
    private int ticksCount;
    private boolean invisible;



    /// Constructors
    public Trap(String name, int visibilityTime, int invisibilityTime){
        this(name, visibilityTime, invisibilityTime, getTrapStat(name));
    }

    private Trap(String name, int visibilityTime, int invisibilityTime, int[] stat) {
        super(name, stat[0], stat[1], stat[2], stat[3]);
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTime;
        this.ticksCount = 0;
        this.invisible = true;
    }



    /// Methods
    // Abstract Methods
    public void accept(UnitVisitor unitVisitor) { unitVisitor.visitTrap(this); }


    // Other Methods
    private static int[] getTrapStat(String name) {
        return switch (name) {
            case "Bonus Trap" -> new int[]{1, 1, 1, 250};
            case "Queen's Trap" -> new int[]{250, 50, 10, 100};
            case "Death Trap" -> new int[]{500, 100, 20, 250};

            default -> throw new IllegalArgumentException("Unknown Trap: " + name);
        };
    }
}
