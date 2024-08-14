package com.example.snrs_01;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StudentAdapter.OnStudentClickListener {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private Button insertButton;
    private TextView dataCountTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation du DatabaseHelper pour gérer la base de données des étudiants
        databaseHelper = new DatabaseHelper(this);

        // Initialisation du RecyclerView pour afficher la liste des étudiants
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation des vues d'affichage des données et du bouton d'insertion
        dataCountTextView = findViewById(R.id.data_list_Count);
        insertButton = findViewById(R.id.insert_data);
        insertButton.setOnClickListener(view -> showInputDialog());

        // Chargement initial des étudiants depuis la base de données et affichage dans RecyclerView
        loadStudents();
    }

    // Méthode pour charger la liste des étudiants depuis la base de données et les afficher dans RecyclerView
    private void loadStudents() {
        List<StudentModel> students = databaseHelper.getAllStudents();
        adapter = new StudentAdapter(students, this);
        recyclerView.setAdapter(adapter);

        // Mise à jour du texte indiquant le nombre total d'étudiants dans la base
        int totalCount = databaseHelper.getTotalCount();
        dataCountTextView.setText("Liste des étudiants (" + totalCount + ")");
    }

    // Interface de callback pour gérer le clic sur le bouton de mise à jour d'un étudiant dans l'adapter
    @Override
    public void onUpdateClick(StudentModel student) {
        showUpdateDialog(student);
    }

    // Interface de callback pour gérer le clic sur le bouton de suppression d'un étudiant dans l'adapter
    @Override
    public void onDeleteClick(int studentId) {
        // Suppression de l'étudiant dans la base de données et mise à jour de l'affichage
        databaseHelper.deleteStudent(String.valueOf(studentId));
        Toast.makeText(this, "Étudiant supprimé", Toast.LENGTH_SHORT).show();
        loadStudents(); // Recharger les étudiants après la suppression
    }

    // Méthode pour afficher une boîte de dialogue d'ajout d'un nouvel étudiant
    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter étudiant");

        // Inflation de la vue personnalisée pour la boîte de dialogue d'insertion
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.insert_dialog, null, false);
        builder.setView(viewInflated);

        // Récupération des références des champs de saisie dans la vue
        final EditText inputName = viewInflated.findViewById(R.id.name);
        final EditText inputEmail = viewInflated.findViewById(R.id.email);
        final EditText inputPhone = viewInflated.findViewById(R.id.phone);
        final EditText inputDob = viewInflated.findViewById(R.id.dob);

        // Gestion du clic sur le champ de date de naissance pour afficher le DatePickerDialog
        inputDob.setOnClickListener(v -> showDatePickerDialog(inputDob));

        // Configuration des boutons de la boîte de dialogue (Ajouter et Annuler)
        builder.setPositiveButton("Ajouter", null);
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Création de la boîte de dialogue
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Personnalisation du comportement du bouton Ajouter pour valider les données saisies
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupération des valeurs saisies par l'utilisateur
                String newName = inputName.getText().toString();
                String newEmail = inputEmail.getText().toString();
                String newPhone = inputPhone.getText().toString();
                String newDob = inputDob.getText().toString();

                // Validation des champs obligatoires et de l'adresse e-mail
                if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty() || newDob.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(newEmail)) {
                    Toast.makeText(MainActivity.this, "Adresse e-mail invalide", Toast.LENGTH_SHORT).show();
                } else {
                    // Création d'un nouvel objet StudentModel avec les données saisies
                    StudentModel newStudent = new StudentModel(0, newName, newEmail, newPhone, newDob, "");

                    // Insertion du nouvel étudiant dans la base de données et notification à l'utilisateur
                    databaseHelper.insertStudent(newStudent);
                    Toast.makeText(MainActivity.this, "Étudiant ajouté", Toast.LENGTH_SHORT).show();

                    // Rechargement de la liste des étudiants après l'insertion et fermeture de la boîte de dialogue
                    loadStudents();
                    dialog.dismiss();
                }
            }
        });
    }

    // Méthode pour afficher une boîte de dialogue de mise à jour des informations d'un étudiant existant
    private void showUpdateDialog(StudentModel student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modifier étudiant");

        // Inflation de la vue personnalisée pour la boîte de dialogue de mise à jour
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_update_student, null, false);
        builder.setView(viewInflated);

        // Récupération des références des champs de saisie dans la vue
        final EditText inputName = viewInflated.findViewById(R.id.input_name);
        final EditText inputEmail = viewInflated.findViewById(R.id.input_email);
        final EditText inputPhone = viewInflated.findViewById(R.id.input_phone);
        final EditText inputDob = viewInflated.findViewById(R.id.input_dob);

        // Pré-remplissage des champs avec les données actuelles de l'étudiant à mettre à jour
        inputName.setText(student.getName());
        inputEmail.setText(student.getEmail());
        inputPhone.setText(student.getPhone());
        inputDob.setText(student.getDob());

        // Gestion du clic sur le champ de date de naissance pour afficher le DatePickerDialog
        inputDob.setOnClickListener(v -> showDatePickerDialog(inputDob));

        // Configuration des boutons de la boîte de dialogue (Modifier et Annuler)
        builder.setPositiveButton("Modifier", null);
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Création de la boîte de dialogue
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Personnalisation du comportement du bouton Modifier pour valider les données mises à jour
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupération des valeurs mises à jour saisies par l'utilisateur
                String updatedName = inputName.getText().toString();
                String updatedEmail = inputEmail.getText().toString();
                String updatedPhone = inputPhone.getText().toString();
                String updatedDob = inputDob.getText().toString();

                // Validation des champs obligatoires et de l'adresse e-mail
                if (updatedName.isEmpty() || updatedEmail.isEmpty() || updatedPhone.isEmpty() || updatedDob.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(updatedEmail)) {
                    Toast.makeText(MainActivity.this, "Adresse e-mail invalide", Toast.LENGTH_SHORT).show();
                } else {
                    // Mise à jour des données de l'étudiant avec les nouvelles valeurs
                    student.setName(updatedName);
                    student.setEmail(updatedEmail);
                    student.setPhone(updatedPhone);
                    student.setDob(updatedDob);

                    // Mise à jour des informations de l'étudiant dans la base de données et notification à l'utilisateur
                    databaseHelper.updateStudent(student);
                    Toast.makeText(MainActivity.this, "Étudiant modifié", Toast.LENGTH_SHORT).show();

                    // Rechargement de la liste des étudiants après la modification et fermeture de la boîte de dialogue
                    loadStudents();
                    dialog.dismiss();
                }
            }
        });
    }

    // Méthode utilitaire pour valider le format d'une adresse e-mail
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Méthode pour afficher un DatePickerDialog pour sélectionner une date
    private void showDatePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                editText.setText(selectedDate);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}
