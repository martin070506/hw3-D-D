package EnemyTypes;

import BoardLogic.GameBoardCallback;
import BoardLogic.Point;
import Player_Types.HeroicUnit;
import Unit_Logic.UnitVisitor;

public class Boss extends Monster implements HeroicUnit {
    /// Fields
    private GameBoardCallback callback;
    private int abilityFrequency;
    private int combatTicks;



    /// Constructors
    public Boss(String name, GameBoardCallback callback) {
        super(name);
        int[] stats = getBossStat(name);
        abilityFrequency = stats[0];
        combatTicks = stats[1];
        this.callback = callback;
    }



    /// Methods
    // Setters
    public void setCombatTicks(int combatTicks) {
        if (combatTicks > abilityFrequency)
            combatTicks = abilityFrequency;
        this.combatTicks = combatTicks;
    }

    // Abstract Methods
    @Override
    public void accept(UnitVisitor unitVisitor) { unitVisitor.visitBoss(this, null); }

    @Override
    public boolean castAbility(Point location) {
        if (combatTicks < abilityFrequency) {
            setCombatTicks(combatTicks++);
            return false;
        }

        combatTicks = 0;
        callback.enemyAttack(this, location);
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "    Ability Charge: " + combatTicks + '/' +abilityFrequency;
    }

    // Other Methods
    private static int[] getBossStat(String name) {
        return switch (name) {
            case "The Mountain" -> new int[]{6, 5};
            case "Queen Cersei" -> new int[]{1, 8};
            case "Night’s King" -> new int[]{8, 3};

            default -> throw new IllegalArgumentException("Unknown Boss: " + name);
        };
    }
}
