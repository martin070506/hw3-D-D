package EnemyTypes;

import Unit_Logic.Unit;

public abstract class Enemy extends Unit {
    /// Fields
    private int XP;



    /// Constructors
    public Enemy(String name, int maxHealth, int attack, int defense, int XP) {
        super(name, maxHealth, attack, defense);
        this.XP = XP;
    }



    /// Methods
    // Getters
    public int getXP() { return XP; }
}
