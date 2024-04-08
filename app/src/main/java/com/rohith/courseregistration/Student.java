package com.rohith.courseregistration;// Student.java

public class Student {
    private int id;
    private String name;
    private int studentID;
    private String priority;

    public Student(int id, String name, int studentID, String priority) {
        this.id = id;
        this.name = name;
        this.studentID = studentID;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStudentID() {
        return studentID;
    }

    public String getPriority() {
        return priority;
    }
}
