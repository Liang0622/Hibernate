package com.vic.pojo;

public class Teacher {
    private  Integer id; //教师编号
    private  String name; //教师姓名
    private  String address; //教师住址
    private  double sal; //教师薪水

    public Teacher() {
    }

    public Teacher(Integer id, String name, String address, double sal) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.sal = sal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getSal() {
        return sal;
    }

    public void setSal(double sal) {
        this.sal = sal;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", sal=" + sal +
                '}';
    }
}
