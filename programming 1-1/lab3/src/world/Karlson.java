package world;

import actions.Flyable;
import actions.Food;
import actions.State;
import unit.*;

public class Karlson extends Person implements Flyable{
    public int fuelAmount = (int) (Math.random()*100);
    public int requiredFuel;
    private Food food;

    public Karlson() throws emptyFoodBankException {
        super.setName("Карлсон");
        super.setSex(Sex.MALE);
        super.setLocation("комната");
        super.setAlive(true);
        while (this.food == null) {
            try {
                this.food = new Food();
                System.out.println("Карлсон получил еду.");
            } catch (emptyFoodBankException e) {
                System.err.println(e.getMessage());
                System.out.println("Cоздаём новую банку...\n");
            }
        }
    }

    @Override
    public void flyAlone(Place destinationPlace) {
        requiredFuel = 20;
        if (this.fuelAmount >= requiredFuel) {
            Directions directionfly = new Directions(Directions.Type.TO, Directions.Prepositions.NONE, destinationPlace.getName());
            System.out.println(this.getName() + " полетел один "+ directionfly.getFullDirection());
            this.setLocation(destinationPlace.getName());
            this.fuelAmount -= requiredFuel;
            super.startFlying((int) (Math.random() * 100));
        } else {
            try {
                System.out.println(this.getName() + " имеет недостаток варенья.");
                this.refuel();
                this.flyAlone(destinationPlace);

            } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            System.out.println("Нужно заправить Карлсона!");
            }

        }
    }

    @Override
    public void flyWithSomeone(Person anotherPerson, Place destinationPlace) {
        requiredFuel = 40;
        if (this.getLocation().equals(anotherPerson.getLocation()) == false) {
            System.out.println(this.getName() + " и " + anotherPerson.getName() + " находятся в разных местах!");
            try {
                this.flyAlone(new Place(anotherPerson.getLocation().getName()));
                System.out.println(this.getName() + " вернулся назад за " + anotherPerson.getName());
                this.flyWithSomeone(anotherPerson, destinationPlace);
            } catch (Exception e) {
                 System.out.println("Ошибка: " + e.getMessage());
                 System.exit(0);
            }
        } else if (this.fuelAmount < requiredFuel) {
            try {
                this.flyAlone(destinationPlace);
            } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            System.out.println("Нужно заправить Карлсона!");
            System.exit(0);
            }
        } else{
            Directions directionfly = new Directions(Directions.Type.TO, Directions.Prepositions.NONE, destinationPlace.getName());
            System.out.println(this.getName() + " полетел с " + anotherPerson.getName() + " " + directionfly.getFullDirection());
            super.startFlying((int) (Math.random() * 100));
            anotherPerson.startFlying(this.intensity);
            this.setLocation(destinationPlace.getName());
            anotherPerson.setLocation(destinationPlace.getName());
            this.fuelAmount -= requiredFuel;
        }

        if (this.getLocation().equals(anotherPerson.getLocation()) == false) {
            System.out.println(this.getName() + " и " + anotherPerson.getName() + " находятся в разных местах!");
            try {
                this.flyAlone(new Place(anotherPerson.getLocation().getName()));
                System.out.println(this.getName() + " вернулся назад за " + anotherPerson.getName());
                this.flyWithSomeone(anotherPerson, destinationPlace);
            } catch (Exception e) {
                 System.out.println("Ошибка: " + e.getMessage());
                 System.exit(0);
            }
        }
    }
    public void tellTruth() {
        if ((int) (Math.random()*100) > 0.5){
            State statement = State.Right;
            this.correctState(statement);
            this.tell();
        } else {
            State statement = State.None;
            this.correctState(statement);
            this.tell();
        }


    }
    public void correctState(State statement){
        switch (statement) {
            case Right -> System.out.print(" так, как и ");
            case None -> System.out.print(" не так, как ");
        }
    }
    public void refuel() throws fuelException {
    boolean hasEaten = food.getFood(this);
    if (hasEaten) {
        this.fuelAmount = 100;
        System.out.println(this.getName() + " теперь заправлен полностью.");
    } else {
        throw new fuelException(this.getName() + " не нашел варенья для заправки!");
    }
}
    @Override
    public int hashCode() {
        return super.hashCode() + this.getName().hashCode();
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
    

