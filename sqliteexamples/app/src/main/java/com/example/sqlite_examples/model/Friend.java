package com.example.sqlite_examples.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Friend implements Serializable {
    private int id;
    private final String name;
    private final LocalDate birthday;
    private final String address;
    private final String phone;

    public Friend(int id, String name, LocalDate birthday, String address, String phone) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.address = address;
        this.phone = phone;
    }

    public Friend(String name, LocalDate birthday, String address, String phone) {
        this.name = name;
        this.birthday = birthday;
        this.address = address;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend friend = (Friend) o;
        return id == friend.id && name.equals(friend.name) && Objects.equals(birthday, friend.birthday) && Objects.equals(address, friend.address) && Objects.equals(phone, friend.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, birthday, address, phone);
    }

    @NonNull
    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
