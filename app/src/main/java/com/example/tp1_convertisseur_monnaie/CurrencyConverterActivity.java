package com.example.tp1_convertisseur_monnaie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;


public class CurrencyConverterActivity extends AppCompatActivity{
    private TextView argentRentre;
    private RadioGroup radioDepart;
    private RadioGroup radioArrive;
    private Button boutonConvertir;
    private TextView conversion;
    private TextView infos;
    private RadioGroup radioGroupTypeConnexion;
    private TextView viewTauxDeConversion;


    private double argent;
    private static final double DOLLAR_VERS_EURO = 0.92;
    private static final double DOLLAR_VERS_POUND = 0.78;
    private static final double EURO_VERS_DOLLAR = 1.09;
    private static final double EURO_VERS_POUND = 0.85;
    private static final double POUND_VERS_DOLLAR = 1.27;
    private static final double POUND_VERS_EURO = 1.17;
    private boolean donneesLocales = true;
    private double tauxConversion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_converter);

        // Retrouve les vues au sein du dossier XML
        argentRentre = findViewById(R.id.precedent);
        radioDepart = findViewById(R.id.depart);
        radioArrive = findViewById(R.id.arrive);
        boutonConvertir = findViewById(R.id.boutonConvertir);
        conversion = findViewById(R.id.conversion);
        infos = findViewById(R.id.infoConnexion);
        radioGroupTypeConnexion = findViewById(R.id.connexion);
        viewTauxDeConversion = findViewById(R.id.tauxConversion);


        // Permet de récupérer la valeur de l'activity précédent
        Intent intent = getIntent();
        argent = intent.getDoubleExtra(MainActivity.ARGENT, 0.);
        argentRentre.setText(String.format("Vous avez rentré: %s", argent));


        infos.setText("Les données locales sont moins précises mais ne nécessite aucune connexion internet"); // change le texte
        boutonConvertir.setEnabled(false); // désactive le bouton

        actions();
    }

    /**
     * Méthode qui permet de définir les différentes actions possibles de l'activity en cours
     */

    public void actions(){
        // setOnCheckedChangeListener permet de voir si le RadioGroup a changé d'état
        radioDepart.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            // Regarde s'il a changé
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                verifierEtatRadios();
            }
        });
        radioArrive.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                verifierEtatRadios();
            }
        });

        // permet de convertir les devises
        boutonConvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultat();
            }
        });

        // Si le radioGroup du type de connexion est "Local" (enfant 0 du radioGroup actuel)
        radioGroupTypeConnexion.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            // quand on clique dessus
            public void onClick(View v) {
                donneesLocales = true;
                infos.setText("Les données locales sont moins précises mais ne nécessite aucune connexion internet");
            }
        });
        // Si le radioGroup du type de connexion est "En ligne" (enfant 1 du radioGroup actuel)
        radioGroupTypeConnexion.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            // quand on clique dessus
            public void onClick(View v) {
                donneesLocales = false;
                infos.setText("Les données en lignes sont plus précises mais nécessite une connexion internet");
            }
        });
    }

    /**
     * Méthode qui permet de vérifier l'état des RadioGroup
     */

    public void verifierEtatRadios() {
        int checkGroupeRadioDepart = radioDepart.getCheckedRadioButtonId(); // retrouve l'id
        int checkGroupeRadioArrive = radioArrive.getCheckedRadioButtonId();
        // RadioGroupget.CheckedRadioButtonId() retourne -1 si aucun radio n'a été sélectionné
        boutonConvertir.setEnabled(checkGroupeRadioDepart != -1 && checkGroupeRadioArrive != -1); // le bouton convertir s'active si les 2 radioGroup sont cochés
    }

    /**
     * Méthode qui permet de récupérer les devises sélectionnées
     */

    public void resultat() {
        // récupération de l'id
        int idRadioDepart = radioDepart.getCheckedRadioButtonId();
        int idRadioArrive = radioArrive.getCheckedRadioButtonId();

        // récupération de la devise en char
        char monnaieDepart = getDevise(getResources().getResourceEntryName(idRadioDepart));
        char monnaieArrivee = getDevise(getResources().getResourceEntryName(idRadioArrive));


        obtenirTauxConversion(monnaieDepart, monnaieArrivee);
    }

    /**
     * Méthode qui permet de récupérer la devise à partir de l'id
     * @param id : nom de l'id
     * @return : retourne un char qui est une devise
     */

    private char getDevise(String id){
        if (id.contains("euro")) return '€';
        else if (id.contains("dollar")) return '$';
        else return '£';
    }

    /**
     * Méthode qui permet de récupérer le taux de conversion de devises que ce soit en local ou en ligne
     * En cas d'échec en ligne, retourne -1
     * @param monnaieDepart : la devise à convertir
     * @param monnaieArrivee : la devise à obtenir
     */

    private void obtenirTauxConversion(char monnaieDepart, char monnaieArrivee) {
        if (donneesLocales){
            if (monnaieDepart == '€' && monnaieArrivee == '€') tauxConversion =  1.0;
            if (monnaieDepart == '€' && monnaieArrivee == '$') tauxConversion = EURO_VERS_DOLLAR;
            if (monnaieDepart == '€' && monnaieArrivee == '£') tauxConversion = EURO_VERS_POUND;
            if (monnaieDepart == '$' && monnaieArrivee == '€') tauxConversion = DOLLAR_VERS_EURO;
            if (monnaieDepart == '$' && monnaieArrivee == '$') tauxConversion = 1.0;
            if (monnaieDepart == '$' && monnaieArrivee == '£') tauxConversion = DOLLAR_VERS_POUND;
            if (monnaieDepart == '£' && monnaieArrivee == '€') tauxConversion = POUND_VERS_EURO;
            if (monnaieDepart == '£' && monnaieArrivee == '$') tauxConversion = POUND_VERS_DOLLAR;
            if (monnaieDepart == '£' && monnaieArrivee == '£') tauxConversion = 1.0;
            resultatRequete(monnaieDepart, monnaieArrivee, tauxConversion);
        } else {
            // en ligne
            // Etape 1 : conversion des char pour que la requête REST soit correcte
            String param1, param2;
            if (monnaieDepart == '€'){
                param1 = "eur";
            } else if (monnaieDepart == '$'){
                param1 = "usd";
            } else {
                param1 = "gbp";
            }
            if (monnaieArrivee == '€'){
                param2 = "eur";
            } else if (monnaieArrivee == '$'){
                param2 = "usd";
            } else {
                param2 = "gbp";
            }
            // Si les 2 devises sont les mêmes alors le taux de conversion est de 1
            if (param1.equals(param2)){
                resultatRequete(monnaieDepart, monnaieArrivee, 1);
            } else {
                // API venant de: https://github.com/fawazahmed0/currency-api
                String URL = String.format("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/%s/%s.json", param1, param2);
                // Permet de faire la requête API
                RequeteAPI requeteAPI = new RequeteAPI(URL, param2, new RequeteAPI.Requete() {
                    @Override
                    // Requête réussie
                    public void tacheComplete(String resultat) {
                        System.out.println("resultat final : " + resultat);
                        tauxConversion = Double.parseDouble(resultat); // récupère le taux de conversion
                        resultatRequete(monnaieDepart, monnaieArrivee, tauxConversion);
                    }

                    @Override
                    // requête échouée
                    public void tacheEchoue() {
                        System.out.println("Tâche échouée");
                        tauxConversion = -1;
                        resultatRequete(monnaieDepart, monnaieArrivee, tauxConversion);
                    }
                });
                requeteAPI.execute(); // permet d'exécuter le Thread faisant la requête
            }
        }
    }

    /**
     * Méthode qui permet de donner le résultat de la conversion entre 2 devises
     * @param monnaieDepart : Monnaie à convertir
     * @param monnaieArrivee : Monnaie à obtenir
     * @param tauxConversion : Taux de conversion
     */

    private void resultatRequete(char monnaieDepart, char monnaieArrivee, double tauxConversion){
        if (tauxConversion != -1){
            // si la requête est réussie
            double resultat = argent * tauxConversion;
            System.out.println("Résultat de la requête: " + resultat);

            DecimalFormat decimalFormat = new DecimalFormat("#.##"); // donne un résultat décimal à 2 chiffres après la virgule
            String resultatArrondi = decimalFormat.format(resultat);
            conversion.setText(String.format("%s%s -> %s%s", argent, monnaieDepart, resultatArrondi , monnaieArrivee)); // donne le résultat sous forme de texte de la conversion

            viewTauxDeConversion.setText(String.format("Taux de conversion actuel de %s vers %s: %s", monnaieDepart, monnaieArrivee, tauxConversion)); // affiche le taux de conversion
            Toast toast = Toast.makeText(this, "Conversion effectué avec succès !", Toast.LENGTH_SHORT); // affiche un toast succès
            toast.show();
        } else {
            // en cas d'échec de la requête
            conversion.setText("Requête échouée, veuillez réessayer");
            viewTauxDeConversion.setText("");
            Toast toast = Toast.makeText(this, "Conversion échouée...", Toast.LENGTH_SHORT); // affiche un toast échec
            toast.show();
        }
    }


}
