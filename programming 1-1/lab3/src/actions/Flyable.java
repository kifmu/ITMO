package actions;

import world.Person;
import world.Place;

public interface Flyable {
    public void flyAlone(Place destinationPlace);
    public void flyWithSomeone(Person anotherPerson, Place destinationPlace);
}
