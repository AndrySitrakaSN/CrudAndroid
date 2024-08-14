package com.example.snrs_01;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private final List<StudentModel> studentList; // Liste des étudiants à afficher
    private final OnStudentClickListener listener; // Interface pour gérer les événements sur les étudiants

    // Interface pour écouter les clics sur les boutons d'action (update et delete)
    public interface OnStudentClickListener {
        void onUpdateClick(StudentModel student); // Méthode appelée lors du clic sur le bouton de mise à jour
        void onDeleteClick(int studentId); // Méthode appelée lors du clic sur le bouton de suppression
    }

    public StudentAdapter(List<StudentModel> studentList, OnStudentClickListener listener) {
        this.studentList = studentList; // Initialise la liste des étudiants
        this.listener = listener; // Initialise l'écouteur pour les événements de clic
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Méthode appelée lors de la création de chaque ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view); // Retourne un nouvel objet StudentViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        // Méthode appelée pour lier chaque élément de la liste à une vue
        StudentModel student = studentList.get(position); // Récupère l'objet StudentModel à la position spécifiée
        // Met à jour les vues ViewHolder avec les détails de l'étudiant
        holder.id.setText("ID : " + String.valueOf(student.getId()));
        holder.name.setText("Nom : " + student.getName());
        holder.email.setText("Email : " + student.getEmail());
        holder.phone.setText("Téléphone : " + student.getPhone());
        holder.dob.setText("Date de naissance : " + student.getDob());

        // Configure les écouteurs de clic pour les boutons de mise à jour et de suppression
        holder.updateButton.setOnClickListener(v -> listener.onUpdateClick(student));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(student.getId()));
    }

    @Override
    public int getItemCount() {
        return studentList.size(); // Retourne le nombre d'éléments dans la liste des étudiants
    }

    // Classe ViewHolder pour contenir et gérer les vues des éléments de la liste
    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView id, name, email, phone, dob; // Vues pour afficher les détails de l'étudiant
        Button updateButton, deleteButton; // Boutons d'action pour mise à jour et suppression

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialise les vues à partir du layout XML student_item
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            dob = itemView.findViewById(R.id.dob);
            updateButton = itemView.findViewById(R.id.update_data);
            deleteButton = itemView.findViewById(R.id.delete_data);
        }
    }
}
