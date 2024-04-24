package Entities;

public class Person {
    private int id;
    private String lastName,FirstName;

    public Person(int id, String lastName, String firstName) {
        this.id = id;
        this.lastName = lastName;
        FirstName = firstName;
    }

    public Person(String lastName, String firstName) {
        this.lastName = lastName;
        FirstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

  @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", FirstName='" + FirstName + '\'' +
                '}';
    }
}
