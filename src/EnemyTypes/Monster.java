package EnemyTypes;

import Unit_Logic.UnitVisitor;

public class Monster extends Enemy {
    /// Fields
    private int visionRange;



    /// Constructors
    public Monster(String name, int visionRange) {
        this(name, visionRange, getMonsterStat(name));
    }

    private Monster(String name, int visionRange, int[] stat) {
        super(name, stat[0], stat[1], stat[2], stat[3]);
        this.visionRange = visionRange;
    }



    /// Methods
    // Getters
    public int getVisionRange() { return visionRange; }

    // Setters
    public void setVisionRange(int visionRange) { this.visionRange = visionRange; }


    // Abstract Methods
    public void accept(UnitVisitor unitVisitor) { unitVisitor.visitMonster(this); }


    // Other Methods
    private static int[] getMonsterStat(String name) {
        return switch (name) {
            case "Lannister Solider" -> new int[]{80, 8, 3, 25};
            case "Lannister Knight" -> new int[]{200, 14, 8, 50};
            case "Queen's Guard" -> new int[]{400, 20, 15, 100};
            case "Wright" -> new int[]{600, 30, 15, 100};
            case "Bear-Wright" -> new int[]{1000, 75, 30, 250};
            case "Giant-Wright" -> new int[]{1500, 100, 40, 500};
            case "White Walker" -> new int[]{2000, 150, 50, 1000};
            case "The Mountain" -> new int[]{1000, 60, 25, 500};
            case "Queen Cersei" -> new int[]{100, 10, 10, 1000};
            case "Night's King" -> new int[]{5000, 300, 150, 5000};

            default -> throw new IllegalArgumentException("Unknown Monster: " + name);
        };
    }
}
