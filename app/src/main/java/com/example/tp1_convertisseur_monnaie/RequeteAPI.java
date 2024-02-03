package com.example.tp1_convertisseur_monnaie;

import android.os.AsyncTask;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe qui permet de faire une requête API en asynchrone pour obtenir le taux de conversion d'une devise
 */
public class RequeteAPI extends AsyncTask<Void, Void, String> {
    private Requete callback; // interface appelée ci-dessous
    private final String url;
    private final String deviseRetour;

    /**
     * Constructeur par défaut
     * @param url : URL de la requête
     * @param deviseRetour : la devise à obtenir
     * @param callback : interface qui permet d'avoir le retour de la requête (si elle est échouée ou pas)
     */
    public RequeteAPI(String url, String deviseRetour, Requete callback) {
        this.url = url;
        this.deviseRetour = deviseRetour;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Void... params) {
        // méthode de AsyncTask
        try {
            URL url = new URL(this.url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // ouvre la connexion avec l'URL
            urlConnection.setRequestMethod("GET"); // méthode GET qui permet d'obtenir des valeurs
            int statusCode = urlConnection.getResponseCode(); // donne la réponse de la requête (200 si réussie)
            if (statusCode == 200) {
                // donne le résultat de la requête
                InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader read = new InputStreamReader(it);
                BufferedReader buff = new BufferedReader(read);
                StringBuilder data = new StringBuilder();
                String chunks;

                while ((chunks = buff.readLine()) != null) {
                    data.append(chunks);
                }

                return data.toString(); // retourne un String ressemblant à un JSON
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // requête échouée
    }

    @Override
    protected void onPostExecute(String resultat) {
        if (resultat != null) { // si la requête est réussie
            try {
                // transforme le résultat en JSON (utilise com.fasterxml.jackson.databind)
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(resultat);

                System.out.println(rootNode);
                System.out.println(rootNode.isArray());
                // Si le JSON n'est pas vide
                if (!rootNode.isEmpty()) {
                    // Récupère le résultat de la devise
                    JsonNode valeur = rootNode.get(deviseRetour);
                    System.out.println("deuxième valeur + " + valeur);
                    // transforme le résultat en String
                    callback.tacheComplete(String.valueOf(valeur));
                } else {
                    callback.tacheEchoue();
                }
            } catch (Exception e) {
                e.printStackTrace();
                callback.tacheEchoue();
            }
        } else {
            System.out.println("Erreur lors de la récupération des données");
            callback.tacheEchoue();
        }
    }

    /**
     * Interface qui permet de récupérer le résultat de requêtes
     */

    public interface Requete {

        /**
         * Quand la requête est réussie, récupère le résultat
         * @param resultat : Résultat de la requête
         */
        void tacheComplete(String resultat);

        /**
         * Quand la requête est échouée
         */
        void tacheEchoue();
    }
}
