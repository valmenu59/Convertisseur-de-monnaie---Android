package com.example.tp1_convertisseur_monnaie;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private EditText input;
    private Button bouton;


    protected static final String ARGENT = "argent"; // permet de contenir la valeur que vous avez rentré dans l'edit text

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // instance par défaut
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Retrouve les vues au sein du dossier XML
        input = findViewById(R.id.inputNombre);
        bouton = findViewById(R.id.boutonValider);

        // désactive le bouton
        bouton.setEnabled(false);

        actions();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        // permet de sauvegarder l'état du inputText
        super.onSaveInstanceState(outState);
        outState.putDouble(ARGENT, getArgent());
    }

    /**
     * Méthode qui permet de définir les différentes actions possibles de l'activity en cours
     */
    public void actions(){
        // Action quand on clique sur le bouton
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Permet de charger un nouvel activity
                Intent intent = new Intent(MainActivity.this, CurrencyConverterActivity.class);
                intent.putExtra(ARGENT, getArgent()); // sauvegarde l'état de getArgent()
                startActivity(intent); // démarre le nouvel activity
            }
        });

        // Action qui permet de regarder les changements d'état de l'EditText
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // permet de faire une action quand le texte change
                getArgent();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * Méthode qui permet de récupérer la valeur rentrée dans l'EditText
     * @return Retourne une valeur décimale
     */

    public double getArgent(){
        String nombre = input.getText().toString(); // récupère le texte rentré
        if (!nombre.isEmpty()) {
            bouton.setEnabled(true); // active le bouton
            double res = Double.parseDouble(nombre);
            return (double) Math.round(res * 100) / 100; // arrondi à 2 chiffres après la virgule
        } else {
            // champ de texte vide
            bouton.setEnabled(false); // désactive le bouton
            return -1;
        }
    }
}
