package com.example.snrs_01;

public class StudentModel {
    private int id;             // Identifiant de l'étudiant
    private String name;        // Nom de l'étudiant
    private String email;       // Adresse e-mail de l'étudiant
    private String phone;       // Numéro de téléphone de l'étudiant
    private String dob;         // Date de naissance de l'étudiant
    private String created_at;  // Date de création de l'enregistrement de l'étudiant

    // Constructeur avec tous les attributs
    public StudentModel(int id, String name, String email, String phone, String dob, String created_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.created_at = created_at;
    }

    // Constructeur par défaut nécessaire pour les opérations avec SQLite
    public StudentModel() {
    }

    // Méthodes getters et setters pour tous les attributs

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
