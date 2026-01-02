package model;

/**
 * Classe Cell - Représente une case de la carte
 * Types de terrain :
 * - grass : Prairie (neutre)
 * - forest : Forêt (bonus défense +2)
 * - mountain : Montagne (impassable, défense +5)
 * - water : Eau (impassable)
 */
public class Cell {
    private String type;
    private boolean walkable;
    private int defenseBonus;
    private int movementCost;

    /**
     * Constructeur d'une case
     * @param type Type de terrain
     * @param walkable Si la case est franchissable
     */
    public Cell(String type, boolean walkable) {
        this.type = type;
        this.walkable = walkable;
        initBonuses();
    }

    /**
     * Initialiser les bonus selon le type de terrain
     */
    private void initBonuses() {
        switch (type) {
            case "grass" -> {
                defenseBonus = 0;
                movementCost = 1;
            }
            case "forest" -> {
                defenseBonus = 2;    // Bonus de défense
                movementCost = 2;    // Plus difficile à traverser
            }
            case "mountain" -> {
                defenseBonus = 5;    // Excellent bonus de défense
                movementCost = 3;    // Très difficile
            }
            case "water" -> {
                defenseBonus = 0;
                movementCost = 99;   // Pratiquement infranchissable
            }
            default -> {
                defenseBonus = 0;
                movementCost = 1;
            }
        }
    }

    // Getters
    public String getType() { return type; }
    public boolean isWalkable() { return walkable; }
    public int getDefenseBonus() { return defenseBonus; }
    public int getMovementCost() { return movementCost; }

    /**
     * Obtenir une description détaillée de la case
     */
    public String getDescription() {
        return switch (type) {
            case "grass" -> "Prairie (Bonus: Aucun)";
            case "forest" -> "Forêt (Défense: +2)";
            case "mountain" -> "Montagne (Défense: +5, Impassable)";
            case "water" -> "Eau (Impassable)";
            default -> type;
        };
    }

    @Override
    public String toString() {
        return type + " (walkable: " + walkable + ", def: +" + defenseBonus + ")";
    }
}