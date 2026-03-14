package models;

import java.util.Date;
import java.util.Objects;

/**
 * Класс, представляющий человека с набором характеристик.
 * @author kifmu
 */
public class Person implements Comparable<Person> {
    private int id;
    private String name;
    private Coordinates coordinates;
    private Date creationDate;
    private int height;
    private String passportID;
    private Color eyeColor;
    private Country nationality;
    private Location location;
    public Person(String name, Coordinates coordinates,
                  int height, String passportID, Color eyeColor,
                  Country nationality, Location location) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть null или пустым.");
        }
        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть null.");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Рост должен быть больше 0.");
        }
        if (passportID != null && passportID.length() < 9) {
            throw new IllegalArgumentException("Длина номера паспорта должна быть не менее 9 символов.");
        }
        if (nationality == null) {
            throw new IllegalArgumentException("Национальность не может быть null.");
        }
        if (location == null) {
            throw new IllegalArgumentException("Локация не может быть null.");
        }
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.height = height;
        this.passportID = passportID;
        this.eyeColor = eyeColor;
        this.nationality = nationality;
        this.location = location;
    }
    public Person() {
        this.creationDate = new Date();
    }

    /**
     * Устанавливает уникальный ID.
     * @param id значение ID (> 0)
     * @throws IllegalArgumentException если id <= 0
     */
    public void setId(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID должен быть > 0");
        this.id = id;
    }
    /**
     * Устанавливает имя человека.
     * @param name имя (не null и не пустое)
     * @throws IllegalArgumentException если имя некорректно
     */
    public void setName(String name) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Имя не может быть пустым");
        this.name = name;
    }
    /**
     * Устанавливает координаты.
     * @param coordinates объект Coordinates
     * @throws IllegalArgumentException если coordinates == null
     */
    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) throw new IllegalArgumentException("Координаты не могут быть null");
        this.coordinates = coordinates;
    }
    /**
     * Устанавливает дату создания объекта.
     * @param creationDate дата
     * @throws IllegalArgumentException если дата == null
     */
    public void setCreationDate(Date creationDate) {
        if (creationDate == null) throw new IllegalArgumentException("Дата не может быть null");
        this.creationDate = creationDate;
    }
    /**
     * Устанавливает рост человека.
     * @param height рост в сантиметрах (> 0)
     * @throws IllegalArgumentException если height <= 0
     */
    public void setHeight(int height) {
        if (height <= 0) throw new IllegalArgumentException("Рост должен быть > 0");
        this.height = height;
    }
    /**
     * Устанавливает номер паспорта.
     * @param passportID номер паспорта (длина >= 9 или null)
     * @throws IllegalArgumentException если длина < 9 и значение не null
     */
    public void setPassportID(String passportID) {
        if (passportID != null && passportID.length() < 9) {
            throw new IllegalArgumentException("Длина паспорта должна быть >= 9");
        }
        this.passportID = passportID;
    }
    /**
     * Устанавливает цвет глаз.
     * @param eyeColor значение Color (может быть null)
     */
    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }
    /**
     * Устанавливает национальность.
     * @param nationality значение Country
     * @throws IllegalArgumentException если nationality == null
     */
    public void setNationality(Country nationality) {
        if (nationality == null) throw new IllegalArgumentException("Национальность не может быть null");
        this.nationality = nationality;
    }
    /**
     * Устанавливает локацию.
     * @param location объект Location
     * @throws IllegalArgumentException если location == null
     */
    public void setLocation(Location location) {
        if (location == null) throw new IllegalArgumentException("Локация не может быть null");
        this.location = location;
    }
    /**
     * Возвращает уникальный ID.
     * @return значение id
     */
    public int getId() { return id; }
    /**
     * Возвращает имя человека.
     * @return имя
     */
    public String getName() { return name; }
    /**
     * Возвращает координаты.
     * @return объект Coordinates
     */
    public Coordinates getCoordinates() { return coordinates; }
    /**
     * Возвращает дату создания объекта.
     * @return дата
     */
    public Date getCreationDate() { return creationDate; }
    /**
     * Возвращает рост человека.
     * @return рост в сантиметрах
     */
    public int getHeight() { return height; }
    /**
     * Возвращает номер паспорта.
     * @return номер паспорта или null
     */
    public String getPassportID() { return passportID; }
    /**
     * Возвращает цвет глаз.
     * @return значение Color или null
     */
    public Color getEyeColor() { return eyeColor; }
    /**
     * Возвращает национальность.
     * @return значение Country
     */
    public Country getNationality() { return nationality; }
    /**
     * Возвращает локацию.
     * @return объект Location
     */
    public Location getLocation() { return location; }

    @Override
    public int compareTo(Person other) {
        if (other == null) return 1;
        return Integer.compare(this.id, other.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && height == person.height &&
                Objects.equals(name, person.name) &&
                Objects.equals(coordinates, person.coordinates) &&
                Objects.equals(creationDate, person.creationDate) &&
                Objects.equals(passportID, person.passportID) &&
                eyeColor == person.eyeColor &&
                nationality == person.nationality &&
                Objects.equals(location, person.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, height, passportID, eyeColor, nationality, location);
    }

    @Override
    public String toString(){
        return "ID: " + id +
                "\nИмя: " + name +
                "\nКоординаты: " + coordinates.toString() +
                "\nДобавлен: " + creationDate +
                "\nРост: " + height +
                "\n№ паспорта: " + passportID +
                "\nЦвет глаз: " + eyeColor +
                "\nНациональность: " + nationality +
                "\nМестонахождение: " + location.toString();
    }
}