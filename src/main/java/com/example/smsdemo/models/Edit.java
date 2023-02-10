package com.example.smsdemo.models;

public class Edit {
    private static String ID, firstName, secondName, middleName, gender, birthDate, phone, email;
    private static Course course;
    private static boolean confirmed;

    public static String getID() {
        return ID;
    }

    public static void setID(String ID) {
        Edit.ID = ID;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        Edit.firstName = firstName;
    }

    public static String getSecondName() {
        return secondName;
    }

    public static void setSecondName(String secondName) {
        Edit.secondName = secondName;
    }

    public static String getMiddleName() {
        return middleName;
    }

    public static void setMiddleName(String middleName) {
        Edit.middleName = middleName;
    }

    public static String getGender() {
        return gender;
    }

    public static void setGender(String gender) {
        Edit.gender = gender;
    }

    public static String getBirthDate() {
        return birthDate;
    }

    public static void setBirthDate(String birthDate) {
        Edit.birthDate = birthDate;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        Edit.phone = phone;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Edit.email = email;
    }

    public static boolean isConfirmed() {
        return confirmed;
    }

    public static void setConfirmed(boolean confirmed) {
        Edit.confirmed = confirmed;
    }

    public static Course getCourse() {
        return course;
    }

    public static void setCourse(Course course) {
        Edit.course = course;
    }

    public static void setAllNull(){
        setID(null);
        setFirstName(null);
        setSecondName(null);
        setMiddleName(null);
        setGender(null);
        setBirthDate(null);
        setPhone(null);
        setEmail(null);
        setConfirmed(false);
    }

}
