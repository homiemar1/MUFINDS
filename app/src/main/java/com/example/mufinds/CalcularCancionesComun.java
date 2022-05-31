package com.example.mufinds;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class CalcularCancionesComun {
    private FirebaseFirestore database;

    public CalcularCancionesComun() {
        this.database = FirebaseFirestore.getInstance();
    }

    public int cancionesEnComun (String usuario1, String usuario2) {
        int cancionesEnComun = 0;
        ArrayList<String> cancionesUsuario1 = new ArrayList<>();
        ArrayList<String> cancionesUsuario2 = new ArrayList<>();

        Task<DocumentSnapshot> task1 = database.collection("relacionUsuarioMusica").document(usuario1).get();
        while (!task1.isComplete()) {}

        Task<DocumentSnapshot> task2 = database.collection("relacionUsuarioMusica").document(usuario2).get();
        while (!task2.isComplete()) {}

        Map<String, Object> map1 = task1.getResult().getData();
        Map<String, Object> map2 = task2.getResult().getData();

        if (map1 == null || map2 == null) {
            return 0;
        }

        for (Map.Entry<String, Object> entry : map1.entrySet()) {
            cancionesUsuario1.add(entry.getKey());
        }

        for (Map.Entry<String, Object> entry : map2.entrySet()) {
            cancionesUsuario2.add(entry.getKey());
        }

        if (cancionesUsuario1.size() >= cancionesUsuario2.size()) {
            for (String cancion :cancionesUsuario1) {
                if (cancionesUsuario2.contains(cancion)) {
                    cancionesEnComun++;
                }
            }
        }
        else {
            for (String cancion :cancionesUsuario2) {
                if (cancionesUsuario1.contains(cancion)) {
                    cancionesEnComun++;
                }
            }
        }

        return cancionesEnComun;
    }
}
