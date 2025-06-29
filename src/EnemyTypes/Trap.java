package EnemyTypes;

import Unit_Logic.UnitVisitor;

public class Trap extends Enemy{
    /// Fields
    private int visibilityTime;
    private int invisibilityTime;
    private int ticksCount;
    private boolean visible;



    /// Constructors
    public Trap(String name){
        this(name, getTrapStat(name));
    }

    private Trap(String name, int[] stat) {
        super(name, stat[0], stat[1], stat[2], stat[3]);
        this.visibilityTime = stat[5];
        this.invisibilityTime = stat[6];
        this.ticksCount = 0;
        this.visible = true;
    }



    /// Methods
    // Getters
    public int getTicks() { return ticksCount; }

    // Abstract Methods
    public void accept(UnitVisitor unitVisitor) { unitVisitor.visitTrap(this); }


    // Other Methods
    private static int[] getTrapStat(String name) {
        return switch (name) {
            case "Bonus Trap" -> new int[]{1, 1, 1, 250, 1, 5};
            case "Queen's Trap" -> new int[]{250, 50, 10, 100, 3, 7};
            case "Death Trap" -> new int[]{500, 100, 20, 250, 1, 10};

            default -> throw new IllegalArgumentException("Unknown Trap: " + name);
        };
    }

    public boolean isVisible() { return visible; }

    public void increaseTick() {
        ticksCount++;
        visible = ticksCount < visibilityTime;

        if (ticksCount == visibilityTime + invisibilityTime)
            ticksCount = 0;
    }
}
