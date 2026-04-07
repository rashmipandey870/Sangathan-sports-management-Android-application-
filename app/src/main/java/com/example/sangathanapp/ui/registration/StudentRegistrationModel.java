package com.example.sangathanapp.ui.registration;


public class StudentRegistrationModel {

    private String _id;
    private String registrationId;

    private String studentName;
    private String enrollment;
    private String department;

    private String teamName;
    private String gender;

    private String phone;
    private String email;

    private int sportId;
    private String status;

    //  ADD THIS CONSTRUCTOR (VERY IMPORTANT)
    public StudentRegistrationModel(
            String registrationId,
            String studentName,
            String enrollment,
            String department,
            String teamName,
            String gender,
            String phone,
            String email,
            int sportId,
            String status
    ) {
        this.registrationId = registrationId;
        this.studentName = studentName;
        this.enrollment = enrollment;
        this.department = department;
        this.teamName = teamName;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.sportId = sportId;
        this.status = status;
    }

    // ===== GETTERS =====

    public String getId() {
        return _id;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public String getDepartment() {
        return department;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public int getSportId() {
        return sportId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }


}