package EnemyTypes;

public class Monster extends Enemy {

    private int visionRange;


    public Monster(String name, int visionRange){
        super(name);
        this.visionRange = visionRange;
    }


    public int getVisionRange() { return visionRange; }
}
