package world;

import actions.Sick;
import actions.Turning;
import unit.Sex;

public class Malish extends Person{
    public Malish() {
        super.setName("Малыш");
        super.setLocation("комната");
        super.setSex(Sex.MALE);
    }

    public void understand() {
        if (super.isFly == true) {
            System.out.println(this.getName() + (super.isFemale ?" успела.":" успел.") + " опомниться.");
        } else {
            System.out.println(this.getName() + (super.isFemale ?" не успела.":" не успел.") + " опомниться.");
        }
    }
    public void feelSmth() {
        if (!super.isFly) {
            System.out.println(this.getName() + " не летит, поэтому ничего не чувствует.");
            System.exit(0);
            return;
        }
        
        if (super.intensity < 80) {
            Turning level = Turning.NotSharp;
            super.feel();
            this.sharpen(level);
            this.say("Бывало и получше..");
        } else {
            Turning level = Turning.IsSharp;
            super.feel();
            System.out.print(", что ");
            this.sharpen(level);
            this.say("Ого, это страшнее, чем чем на \"американских горках\".");
        }
        System.out.println();
    }

    public void sharpen(Turning level){
        switch (level) {
            case IsSharp:
                System.out.println(Sick.buzzingInEars + " и " + Sick.feelAWamble);
                break;
            case NotSharp:
                System.out.println(" ничего");
                break;
        }
    }
    @Override
    public void takeBreath() {
        System.out.println(this.getName() + "вздохнул");
    }
    @Override
    public String toString() {
        return "персонаж " + this.getName();
    }
}
