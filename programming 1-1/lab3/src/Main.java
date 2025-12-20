
import unit.emptyFoodBankException;
import world.*;

public class Main {
    public static void main(String[] args) throws emptyFoodBankException {
        Karlson karlson = new Karlson();
        Malish malish = new Malish();
        Everything all = new Everything("Все");
        malish.setLocation("комната");
        Place balkony = new Place("балкончик");
        
        karlson.takeBreath();
        karlson.flyWithSomeone(malish, balkony);
        malish.feelSmth();
        karlson.happen(all.name());
        karlson.tellTruth();

    }
}
