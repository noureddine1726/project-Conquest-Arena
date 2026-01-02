package model;

/**
 * Classe Batiment - Représente un bâtiment dans le jeu
 * Types disponibles :
 * - centre : Centre de commandement (bâtiment principal)
 * - caserne : Permet de recruter des unités
 * - mine : Génère de l'or (+20/tour)
 * - ferme : Génère de la nourriture (+15/tour)
 * - scierie : Génère du bois (+12/tour)
 * - carriere : Génère de la pierre (+10/tour)
 */
public class Batiment {
    private int row, col;
    private String type;
    private Joueur owner;
    private int constructionTime;
    private int remainingTime;
    private int costGold;
    private int costBois;
    private int costPierre;

    /**
     * Constructeur pour bâtiment déjà construit
     */
    public Batiment(int row, int col, String type, Joueur owner) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.owner = owner;
        this.remainingTime = 0; // Déjà construit
        initCosts();
    }

    /**
     * Constructeur avec option de construction progressive
     */
    public Batiment(int row, int col, String type, Joueur owner, boolean needConstruction) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.owner = owner;
        initCosts();
        if (needConstruction) {
            this.remainingTime = this.constructionTime;
        } else {
            this.remainingTime = 0;
        }
    }

    /**
     * Initialiser les coûts et temps de construction selon le type
     */
    private void initCosts() {
        switch (type) {
            case "centre" -> {
                costGold = 0;
                costBois = 0;
                costPierre = 0;
                constructionTime = 0;
            }
            case "caserne" -> {
                costGold = 100;
                costBois = 50;
                costPierre = 30;
                constructionTime = 3;
            }
            case "mine" -> {
                costGold = 80;
                costBois = 40;
                costPierre = 20;
                constructionTime = 2;
            }
            case "ferme" -> {
                costGold = 60;
                costBois = 30;
                costPierre = 10;
                constructionTime = 2;
            }
            case "scierie" -> {
                costGold = 70;
                costBois = 20;
                costPierre = 15;
                constructionTime = 2;
            }
            case "carriere" -> {
                costGold = 75;
                costBois = 25;
                costPierre = 10;
                constructionTime = 2;
            }
            default -> {
                costGold = 50;
                costBois = 20;
                costPierre = 10;
                constructionTime = 1;
            }
        }
    }

    /**
     * Avancer la construction d'un tour
     */
    public void progressConstruction() {
        if (remainingTime > 0) {
            remainingTime--;
        }
    }

    /**
     * Vérifier si le bâtiment est complètement construit
     */
    public boolean isConstructed() {
        return remainingTime == 0;
    }

    /**
     * Générer les revenus du bâtiment
     * Appelé chaque tour pour les bâtiments construits
     */
    public void generateIncome(Joueur owner) {
        if (!isConstructed()) return;

        switch (type) {
            case "mine" -> owner.addGold(20);
            case "ferme" -> owner.addNourriture(15);
            case "scierie" -> owner.addBois(12);
            case "carriere" -> owner.addPierre(10);
        }
    }

    /**
     * Vérifier si ce bâtiment peut recruter des unités
     */
    public boolean canRecruit(String unitType) {
        return type.equals("caserne") && isConstructed();
    }

    // Getters
    public int getRow() { return row; }
    public int getCol() { return col; }
    public String getType() { return type; }
    public Joueur getOwner() { return owner; }
    public int getRemainingTime() { return remainingTime; }
    public int getCostGold() { return costGold; }
    public int getCostBois() { return costBois; }
    public int getCostPierre() { return costPierre; }
    public int getConstructionTime() { return constructionTime; }

    /**
     * Obtenir le nom complet du bâtiment
     */
    public String getDisplayName() {
        return switch (type) {
            case "centre" -> "Centre de Commandement";
            case "caserne" -> "Caserne";
            case "mine" -> "Mine d'Or";
            case "ferme" -> "Ferme";
            case "scierie" -> "Scierie";
            case "carriere" -> "Carrière";
            default -> type;
        };
    }

    @Override
    public String toString() {
        String status = isConstructed() ? "Opérationnel" : "Construction: " + remainingTime + " tours";
        return getDisplayName() + " (" + status + ")";
    }
}