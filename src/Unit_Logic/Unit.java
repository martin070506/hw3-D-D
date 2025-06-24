package Unit_Logic;

public abstract class Unit {
    /// Fields
    private String name;
    private int maxHealth;
    private int health;
    private int attack;
    private int defense;



    /// Constructors
    public Unit(String name, int maxHealth, int attack, int defense) {

        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attack = attack;
        this.defense = defense;
    }



    /// Methods
    // Getters
    public String getName() { return name; }
    public int getMaxHealth() { return maxHealth; }
    public int getHealth() { return health; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }

    // Setters
    public  void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth;}
    public void setHealth(int health) {

        if (health > maxHealth)
            health = maxHealth;
        if (health < 0)
            health = 0;

        this.health = health;
    }

    public void setAttack(int attack) { this.attack = attack; }
    public void setDefense(int defense) { this.defense = defense; }


    // Abstract Methods
    public abstract void accept(UnitVisitor unitVisitor);

    // Other Methods
    public void takeDamage(int damage) {

        if (damage > 0)
            setHealth(health - damage);
    }
}
