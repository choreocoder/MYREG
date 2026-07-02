package models;

public abstract class Person {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String residentialArea;
    private String personRole;

    protected Person(int id, String firstName, String lastName, String email,
                     String phoneNumber, String residentialArea, String personRole) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.residentialArea = residentialArea;
        this.personRole = personRole;
    }

    protected Person(String firstName, String lastName, String email,
                     String phoneNumber, String residentialArea, String personRole) {
        this(0, firstName, lastName, email, phoneNumber, residentialArea, personRole);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Id cannot be negative");
        }
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty() || email.length() > 100) {
            throw new IllegalArgumentException("Invalid email: cannot be empty or exceed 100 characters.");
        }
        this.email = email;
    } 

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty() || phoneNumber.length() < 10 || phoneNumber.length() > 20) {
            throw new IllegalArgumentException("Phone number cannot be empty and must be between 10 and 20 characters long.");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getResidentialArea() {
        return residentialArea;
    }

    public void setResidentialArea(String residentialArea) {
        if (residentialArea == null || residentialArea.trim().isEmpty()) {
            throw new IllegalArgumentException("Residential Area cannot be null or empty");
        }
        this.residentialArea = residentialArea;
    }

    public String getPersonRole() {
        return personRole;
    }

    public void setPersonRole(String personRole) {
        if (personRole == null || personRole.trim().isEmpty()) {
            throw new IllegalArgumentException("models.Person Role cannot be null or empty");
        }
        this.personRole = personRole;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}