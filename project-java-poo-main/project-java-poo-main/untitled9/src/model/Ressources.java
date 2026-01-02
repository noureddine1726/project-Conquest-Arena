package model;

/**
 * Classe Ressources - Constantes pour les ressources du jeu
 * Contient tous les coûts et revenus
 */
public class Ressources {
    // Types de ressources
    public static final String OR = "or";
    public static final String BOIS = "bois";
    public static final String PIERRE = "pierre";
    public static final String NOURRITURE = "nourriture";

    // ===== COÛTS DES UNITÉS =====
    public static final int SOLDIER_COST = 50;
    public static final int ARCHER_COST = 70;
    public static final int CAVALIER_COST = 100;
    public static final int UNIT_FOOD_COST = 10;  // Toutes les unités coûtent 10 nourriture

    // ===== COÛTS DES BÂTIMENTS =====

    // Centre de commandement (gratuit)
    public static final int CENTRE_COST_GOLD = 0;
    public static final int CENTRE_COST_BOIS = 0;
    public static final int CENTRE_COST_PIERRE = 0;

    // Caserne
    public static final int CASERNE_COST_GOLD = 100;
    public static final int CASERNE_COST_BOIS = 50;
    public static final int CASERNE_COST_PIERRE = 30;

    // Mine
    public static final int MINE_COST_GOLD = 80;
    public static final int MINE_COST_BOIS = 40;
    public static final int MINE_COST_PIERRE = 20;

    // Ferme
    public static final int FERME_COST_GOLD = 60;
    public static final int FERME_COST_BOIS = 30;
    public static final int FERME_COST_PIERRE = 10;

    // Scierie
    public static final int SCIERIE_COST_GOLD = 70;
    public static final int SCIERIE_COST_BOIS = 20;
    public static final int SCIERIE_COST_PIERRE = 15;

    // Carrière
    public static final int CARRIERE_COST_GOLD = 75;
    public static final int CARRIERE_COST_BOIS = 25;
    public static final int CARRIERE_COST_PIERRE = 10;

    // ===== TEMPS DE CONSTRUCTION (en tours) =====
    public static final int CASERNE_BUILD_TIME = 3;
    public static final int MINE_BUILD_TIME = 2;
    public static final int FERME_BUILD_TIME = 2;
    public static final int SCIERIE_BUILD_TIME = 2;
    public static final int CARRIERE_BUILD_TIME = 2;

    // ===== REVENUS PAR TOUR =====
    public static final int MINE_INCOME = 20;
    public static final int FERME_INCOME = 15;
    public static final int SCIERIE_INCOME = 12;
    public static final int CARRIERE_INCOME = 10;
    public static final int BASE_INCOME = 25;  // Revenu de base par tour

    // ===== STATISTIQUES DES UNITÉS =====

    // Soldat
    public static final int SOLDIER_HP = 100;
    public static final int SOLDIER_ATTACK = 30;
    public static final int SOLDIER_DEFENSE = 10;
    public static final int SOLDIER_RANGE = 1;
    public static final int SOLDIER_MOVEMENT = 2;

    // Archer
    public static final int ARCHER_HP = 80;
    public static final int ARCHER_ATTACK = 40;
    public static final int ARCHER_DEFENSE = 5;
    public static final int ARCHER_RANGE = 3;
    public static final int ARCHER_MOVEMENT = 2;

    // Cavalier
    public static final int CAVALIER_HP = 120;
    public static final int CAVALIER_ATTACK = 35;
    public static final int CAVALIER_DEFENSE = 8;
    public static final int CAVALIER_RANGE = 1;
    public static final int CAVALIER_MOVEMENT = 4;
    public static final int CAVALIER_BONUS_VS_ARCHER = 15;

    // ===== PARAMÈTRES DE JEU =====
    public static final int VICTORY_UNIT_COUNT = 15;  // Nombre d'unités pour victoire par domination
    public static final int STARTING_GOLD = 200;
    public static final int STARTING_BOIS = 100;
    public static final int STARTING_PIERRE = 80;
    public static final int STARTING_NOURRITURE = 150;
}