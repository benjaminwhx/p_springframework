package com.jd.jr.bean;

import java.util.Date;

/**
 * User: 吴海旭
 * Date: 2017-07-02
 * Time: 下午5:57
 */
public class Inventor {

    private String name;
    private Date birthday;
    private String nationality;

    public Inventor() {
    }

    public Inventor(String name, Date birthday, String nationality) {
        this.name = name;
        this.birthday = birthday;
        this.nationality = nationality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        return "Inventor{" +
                "name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
