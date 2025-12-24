package org.example.onlinetestbackend.pojo;

public class User {
    private Integer userid; // 用户ID
    private String username; // 用户名
    private String email; // 用户邮箱
    private String phone; // 用户手机号
    private String password; // 用户密码
    private Integer admin; // 是否为管理员（0：否，1：是）

    // Getter 和 Setter 方法
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

    // 构造方法
    public User(Integer userid, String username, String phone, String email, String password, Integer admin) {
        this.userid = userid;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

    // 重写 toString 方法，方便打印对象内容
    @Override
    public String toString() {
        return "{\n" +
                "\"userid\":\"" + userid+ "\",\n" +
                "\"username\":\"" + username + "\",\n" +
                "\"phone\":" + phone + ",\n" +
                "\"email\":\"" + email + "\",\n" +
                "\"password\":\"" + password+ "\",\n" +
                "\"admin\":" + admin + "\n" +
                '}';
    }
}