package ass.cashorsplash.huawei.Model;

public class User {
    String name, Last_name, email, cin, password;
    int point, money_win, money_lose;

    public User() {
    }

    public User(String name, String last_name, String email, String cin, String password) {
        this.name = name;
        Last_name = last_name;
        this.email = email;
        this.cin = cin;
        this.password = password;
    }

    public User(String name, String last_name, String email, String cin, String password, int point, int money_win, int money_lose) {
        this.name = name;
        Last_name = last_name;
        this.email = email;
        this.cin = cin;
        this.password = password;
        this.point = point;
        this.money_win = money_win;
        this.money_lose = money_lose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return Last_name;
    }

    public void setLast_name(String last_name) {
        Last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getMoney_win() {
        return money_win;
    }

    public void setMoney_win(int money_win) {
        this.money_win = money_win;
    }

    public int getMoney_lose() {
        return money_lose;
    }

    public void setMoney_lose(int money_lose) {
        this.money_lose = money_lose;
    }

}
