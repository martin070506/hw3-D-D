package EnemyTypes;

public class Trap extends Enemy{

    private int visibilityTime;
    private int invisibilityTime;
    private int ticksCount;
    private boolean invisible;


    public Trap(String name, int visibilityTime, int invisibilityTime){
        super(name);
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTime;
        this.ticksCount = 0;
        this.invisible = true;
    }
}
