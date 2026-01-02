package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Joueur - Représente un joueur (humain ou IA)
 * Gère les ressources et les unités du joueur
 */
public class Joueur {
    private String name;
    private int gold = 200;         // Or pour construire et recruter
    private int bois = 100;         // Bois pour construire
    private int pierre = 80;        // Pierre pour construire
    private int nourriture = 150;   // Nourriture pour recruter
    private List<Unit> unites = new ArrayList<>();
    private boolean isPlayer1;

    /**
     * Constructeur principal
     * @param name Nom du joueur
     * @param isPlayer1 true si joueur 1, false si joueur 2/IA
     */
    public Joueur(String name, boolean isPlayer1) {
        this.name = name;
        this.isPlayer1 = isPlayer1;
    }

    /**
     * Constructeur par défaut
     */
    public Joueur() {
        this("Player", true);
    }

    /**
     * Obtenir une copie de la liste des unités
     * @return Liste des unités
     */
    public List<Unit> getUnites() {
        return new ArrayList<>(unites);
    }

    /**
     * Recruter une unité (coûte or + nourriture)
     * @param unit Unité à recruter
     * @return true si le recrutement a réussi
     */
    public boolean recruter(Unit unit) {
        if (unit == null) return false;

        int cost = unit.getCost();
        if (gold >= cost && nourriture >= 10) {
            gold -= cost;
            nourriture -= 10;
            unites.add(unit);
            unit.setOwner(this);
            return true;
        }
        return false;
    }

    /**
     * Vérifier si le joueur peut construire un bâtiment
     * @param batiment Bâtiment à vérifier
     * @return true si les ressources sont suffisantes
     */
    public boolean peutConstruire(Batiment batiment) {
        return gold >= batiment.getCostGold() &&
                bois >= batiment.getCostBois() &&
                pierre >= batiment.getCostPierre();
    }

    /**
     * Construire un bâtiment (consomme les ressources)
     * @param batiment Bâtiment à construire
     * @return true si la construction a commencé
     */
    public boolean construire(Batiment batiment) {
        if (peutConstruire(batiment)) {
            gold -= batiment.getCostGold();
            bois -= batiment.getCostBois();
            pierre -= batiment.getCostPierre();
            return true;
        }
        return false;
    }

    /**
     * Ajouter une unité directement (sans coût)
     * Utilisé pour les unités de départ
     */
    public void addUnite(Unit unit) {
        if (unit != null) {
            unites.add(unit);
            unit.setOwner(this);
        }
    }

    /**
     * Retirer les unités mortes de la liste
     */
    public void removeDeadUnits() {
        unites.removeIf(u -> !u.isAlive());
    }

    /**
     * Réinitialiser toutes les unités pour un nouveau tour
     */
    public void resetTurn() {
        for (Unit u : unites) {
            u.resetTurn();
        }
    }

    // Ajouter des ressources
    public void addGold(int amount) { gold += amount; }
    public void addBois(int amount) { bois += amount; }
    public void addPierre(int amount) { pierre += amount; }
    public void addNourriture(int amount) { nourriture += amount; }

    // Dépenser des ressources (avec vérification)
    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }

    public boolean spendBois(int amount) {
        if (bois >= amount) {
            bois -= amount;
            return true;
        }
        return false;
    }

    public boolean spendPierre(int amount) {
        if (pierre >= amount) {
            pierre -= amount;
            return true;
        }
        return false;
    }

    public boolean spendNourriture(int amount) {
        if (nourriture >= amount) {
            nourriture -= amount;
            return true;
        }
        return false;
    }

    // Getters
    public String getName() { return name; }
    public int getGold() { return gold; }
    public int getBois() { return bois; }
    public int getPierre() { return pierre; }
    public int getNourriture() { return nourriture; }
    public boolean isPlayer1() { return isPlayer1; }

    /**
     * Obtenir le nombre d'unités vivantes
     */
    public int getUnitCount() {
        return (int) unites.stream().filter(Unit::isAlive).count();
    }

    /**
     * Calculer la force totale de l'armée
     */
    public int getArmyStrength() {
        return unites.stream()
                .filter(Unit::isAlive)
                .mapToInt(u -> u.getHp() + u.getAttack())
                .sum();
    }

    @Override
    public String toString() {
        return name + " (Or:" + gold + " Bois:" + bois + " Pierre:" + pierre + " Nour:" + nourriture + ")";
    }
}