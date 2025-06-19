package Unit_Logic;

public class Unit {

    private String name;
    private int maxHealth;
    private int health;
    private int attack;
    private int defense;


    public Unit(String name, int maxHealth, int attack, int defense) {

        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attack = attack;
        this.defense = defense;
    }

    public String getName() { return name; }
    public int getMaxHealth() { return maxHealth; }
    public int getHealth() { return health; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }

    public void setHealth(int health) {

        if (health > maxHealth)
            health = maxHealth;

        this.health = health;
    }

    public void setAttack(int attack)
    {
        this.attack=attack;
    }
    public void setDefense(int defense){this.defense=defense;}
    public  void setMaxHealth(int maxHealth){this.maxHealth=maxHealth;}

}
