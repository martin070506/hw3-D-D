package EnemyTypes;

import Unit_Logic.Unit;

public class Enemy extends Unit {

    private int XP;

    public Enemy(String name, int maxHealth, int attack, int defense, int XP) {
        super(name, maxHealth, attack, defense);
        this.XP = XP;
    }

    public Enemy(Enemy enemy) {
        super(enemy.getName(), enemy.getMaxHealth(), enemy.getAttack(), enemy.getDefense());
        this.XP = enemy.getXP();
    }

    public Enemy(String name) { this(findEnemy(name)); }


    private static Enemy findEnemy(String name){
        return switch (name) {
            // Monsters
            case "Lannister Solider" -> new Enemy(name, 80, 8, 3, 25);
            case "Lannister Knight" -> new Enemy(name, 200, 14, 8, 50);
            case "Queen's Guard" -> new Enemy(name, 400, 20, 15, 100);
            case "Wright" -> new Enemy(name, 600, 30, 15, 100);
            case "Bear-Wright" -> new Enemy(name, 1000, 75, 30, 250);
            case "Giant-Wright" -> new Enemy(name, 1500, 100, 40, 500);
            case "White Walker" -> new Enemy(name, 2000, 150, 50, 1000);
            case "The Mountain" -> new Enemy(name, 1000, 60, 25, 500);
            case "Queen Cersei" -> new Enemy(name, 100, 10, 10, 1000);
            case "Night's King" -> new Enemy(name, 5000, 300, 150, 5000);

            // Traps
            case "Bonus Trap" -> new Enemy(name, 1, 1, 1, 250);
            case "Queen's Trap" -> new Enemy(name, 250, 50, 10, 100);
            case "Death Trap" -> new Enemy(name, 500, 100, 20, 250);

            default -> throw new IllegalArgumentException("Unknown enemy: " + name);
        };
    }

    public int getXP() { return XP; }
}
