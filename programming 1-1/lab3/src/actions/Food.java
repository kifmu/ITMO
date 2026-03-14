package actions;

import java.util.ArrayList;
import unit.emptyFoodBankException;
import world.Person;

public class Food {
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 5;
    private final ArrayList<String> foodBank;

    public Food() throws emptyFoodBankException {
        int size = (int) (Math.random() * (MAX_VALUE - MIN_VALUE + 1)) + MIN_VALUE;
        this.foodBank = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            foodBank.add(Math.random() < 0.5 ? "варенье" : "пусто");
        }

        boolean hasJam = false;
        for (String item : foodBank) {
            if ("варенье".equals(item)) {
                hasJam = true;
                break;
            }
        }

        if (!hasJam) {
            throw new emptyFoodBankException("Во всей банке еды только \"пусто\" , варенья нет!");
        }
    }

    public boolean getFood(Person person) {
        int index = (int) (Math.random() * foodBank.size());
        String food = foodBank.get(index);

        if ("варенье".equals(food)) {
            person.isAlive = true;
            System.out.println(person.getName() + " съел " + food + " из ячейки " + index);
            return true;
        } else {
            System.out.println(person.getName() + " открыл ячейку " + index + ", там " + food);
            person.die(" умер от недостатка варенья в крови.");
            return false;
        }
    }
}
