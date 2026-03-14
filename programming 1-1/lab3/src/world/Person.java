package world;
import unit.Sex;

public abstract class Person{
    protected boolean isSick = false;
    public String name;
    private Sex sex;
    protected boolean isMale;
    protected boolean isFemale;
    private Place location;
    public boolean isAlive = true;
    public boolean isFly = false;
    public int intensity = 0;
    protected boolean somethingHappened = false;

    public Person(String name, boolean isAlive, Sex sex) {
        this.name = name;
        this.sex = sex;
        this.isAlive = isAlive;
        checkAliveStatus();
        this.isFly = false;
        this.intensity = 0; 
    }
    public Person() {
    }
    public void setAlive(boolean alive) {
        boolean wasAlive = this.isAlive;
        this.isAlive = alive;
        
        if (wasAlive && !alive) {
            die("умер");
        }
    }
    public void startFlying(int intensity) {
        this.isFly = true;
        this.intensity = intensity;
    }
    
    public  void stopFlying() {
        this.isFly = false;
        this.intensity = 0;
    }
    public void die(String reason) {
        this.isAlive = false;
        System.out.println(this.name + " " + reason);
        System.exit(0);

    }
    private void checkAliveStatus() {
        if (!isAlive) {
            die((this.isFemale ?" умерла":" умер"));
        }
    }
    public Sex getSex() {
        return sex;
    }
    public void setSex(Sex sex){
        this.sex = sex;
    }

    public Place getLocation() {
        return location;
    }

    public void setLocation(String locationName) {
        this.location = new Place(locationName);
    }

    public abstract void takeBreath();

    public void happen(String name) {
        this.somethingHappened = true;
        System.out.print(name + " произошло");
    }
    public void feel() {
        System.out.print(this.getName() + (this.isFemale ?" почувствовала":" почувствовал"));
    }
    public void say(String text) {
        System.out.print(this.getName() + (this.isFemale ?" сказала":" сказал")+ ':' + '"' + text + '"');
    }
    public void tell() {
        System.out.println(this.getName() + (this.isFemale ?" сказала":" сказал"));
    }
    public String getName() {
        return name == null ? "..." : name;
    }
    public void setName(String newName) {
        this.name = newName;
    }
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode()+this.getSex().hashCode() + this.getLocation().hashCode();
    }
    
    @Override
    public String toString() {
        return "персонаж " + this.getName();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.hashCode() == this.hashCode();
    }
}
