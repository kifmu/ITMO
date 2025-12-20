package world;

public class Place {
    String name;
    
    public Place(String name) {
        this.name = name;
    }

    
    public String getName() {
        return name == null ? "..." : name;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.getName().hashCode();
    }

    @Override
    public String toString() {
        return "место " + this.getName();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Place place = (Place) obj;
        return name != null ? name.equals(place.name) : place.name == null;
    }
}