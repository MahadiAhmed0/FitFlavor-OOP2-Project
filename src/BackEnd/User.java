package BackEnd;

public class User {
    private String username, password;
    private int age;
    private double weight;

    public User(String username, String password, int age, double weight) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.weight = weight;
    }

    public boolean checkPassword(String insertPassword) {
        return this.password.equals(insertPassword);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
