package com.example.snrs_01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1; // Version de la base de données
    private static final String DATABASE_NAME = "student_db"; // Nom de la base de données
    private static final String TABLE_NAME = "students"; // Nom de la table
    private static final String ID = "id"; // Colonne ID
    private static final String NAME = "name"; // Colonne nom
    private static final String EMAIL = "email"; // Colonne email
    private static final String DOB = "dob"; // Colonne date de naissance
    private static final String PHONE = "phone"; // Colonne téléphone
    private static final String CREATED_AT = "created_at"; // Colonne date de création

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); // Constructeur pour initialiser la base de données
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Méthode appelée lors de la création de la base de données
        String table_query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" +
                ID + " INTEGER PRIMARY KEY," +
                NAME + " TEXT," +
                EMAIL + " TEXT," +
                PHONE + " TEXT," +
                DOB + " TEXT," +
                CREATED_AT + " TEXT" +
                ")";
        db.execSQL(table_query); // Exécute la requête pour créer la table si elle n'existe pas déjà
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Méthode appelée lorsqu'une mise à jour de la base de données est nécessaire
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // Supprime la table existante si elle existe
        onCreate(db); // Recrée la table en appelant onCreate()
    }

    // Méthode pour insérer un nouvel étudiant dans la base de données
    public void insertStudent(StudentModel studentModel) {
        SQLiteDatabase db = this.getWritableDatabase(); // Récupère la base de données en mode écriture

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, studentModel.getName());
        contentValues.put(EMAIL, studentModel.getEmail());
        contentValues.put(PHONE, studentModel.getPhone());
        contentValues.put(DOB, studentModel.getDob());
        contentValues.put(CREATED_AT, studentModel.getCreated_at());

        db.insert(TABLE_NAME, null, contentValues); // Insère les valeurs dans la table spécifiée

        db.close(); // Ferme la connexion à la base de données
    }

    // Méthode pour récupérer un étudiant spécifique par son ID
    public StudentModel getStudent(int id) {
        SQLiteDatabase db = this.getReadableDatabase(); // Récupère la base de données en mode lecture
        Cursor cursor = db.query(TABLE_NAME, new String[]{
                        ID, NAME, EMAIL, PHONE, DOB, CREATED_AT},
                ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            StudentModel studentModel = new StudentModel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            cursor.close();
            return studentModel; // Retourne l'objet StudentModel trouvé
        } else {
            return null; // Retourne null si aucun enregistrement n'a été trouvé
        }
    }

    // Méthode pour récupérer tous les étudiants de la base de données
    public List<StudentModel> getAllStudents() {
        List<StudentModel> studentModelList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase(); // Récupère la base de données en mode lecture
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                StudentModel studentModel = new StudentModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                studentModelList.add(studentModel); // Ajoute chaque StudentModel à la liste
            } while (cursor.moveToNext());
        }

        cursor.close();
        return studentModelList; // Retourne la liste des StudentModel
    }

    // Méthode pour mettre à jour les détails d'un étudiant existant dans la base de données
    public int updateStudent(StudentModel studentModel) {
        SQLiteDatabase db = this.getWritableDatabase(); // Récupère la base de données en mode écriture
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, studentModel.getName());
        contentValues.put(EMAIL, studentModel.getEmail());
        contentValues.put(PHONE, studentModel.getPhone());
        contentValues.put(DOB, studentModel.getDob());

        int result = db.update(TABLE_NAME, contentValues, ID + "=?", new String[]{String.valueOf(studentModel.getId())});
        db.close(); // Ferme la connexion à la base de données
        return result; // Retourne le nombre de lignes affectées
    }

    // Méthode pour supprimer un étudiant de la base de données par son ID
    public void deleteStudent(String id) {
        SQLiteDatabase db = this.getWritableDatabase(); // Récupère la base de données en mode écriture
        db.delete(TABLE_NAME, ID + "=?", new String[]{id}); // Supprime l'enregistrement spécifié
        db.close(); // Ferme la connexion à la base de données
    }

    // Méthode pour obtenir le nombre total d'étudiants dans la base de données
    public int getTotalCount() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase(); // Récupère la base de données en mode lecture
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        int count = cursor.getCount(); // Récupère le nombre total de lignes dans le curseur
        cursor.close();
        sqLiteDatabase.close(); // Ferme la connexion à la base de données
        return count; // Retourne le nombre total d'enregistrements
    }
}
