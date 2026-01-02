package model;

/**
 * Classe abstraite représentant une unité dans le jeu
 * Toutes les unités (Soldier, Archer, Cavalier) héritent de cette classe
 */
public abstract class Unit {
    protected int row, col;
    protected int hp;
    protected int maxHp;
    protected int attack;
    protected int defense;
    protected int range;
    protected int movement;
    protected int cost;
    protected String name;
    protected Joueur owner;
    protected boolean hasMoved = false;
    protected boolean hasAttacked = false;

    /**
     * Constructeur d'une unité
     * @param row Position ligne
     * @param col Position colonne
     * @param owner Propriétaire de l'unité
     * @param maxHp Points de vie maximum
     * @param attack Force d'attaque
     * @param defense Capacité de défense
     * @param range Portée d'attaque
     * @param movement Capacité de déplacement
     * @param cost Coût en or
     * @param name Nom de l'unité
     */
    public Unit(int row, int col, Joueur owner, int maxHp, int attack, int defense, int range, int movement, int cost, String name) {
        this.row = row;
        this.col = col;
        this.owner = owner;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.range = range;
        this.movement = movement;
        this.cost = cost;
        this.name = name;
    }

    /**
     * Définir la position de l'unité
     */
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Vérifier si une cible est à portée d'attaque
     * @param target Unité cible
     * @return true si la cible est à portée
     */
    public boolean isInRange(Unit target) {
        int distance = Math.abs(this.row - target.row) + Math.abs(this.col - target.col);
        return distance <= this.range;
    }

    /**
     * Attaquer une unité cible
     * Formule : dégâts = attaque - défense + aléatoire(-5, +5)
     * @param target Unité à attaquer
     */
    public void attack(Unit target) {
        if (target != null && target.isAlive() && target.getOwner() != owner && !hasAttacked && isInRange(target)) {
            int damage = Math.max(0, this.attack - target.defense + (int)(Math.random() * 11) - 5);
            target.takeDamage(damage);
            hasAttacked = true;
        }
    }

    /**
     * Recevoir des dégâts
     * @param damage Points de dégâts reçus
     */
    public void takeDamage(int damage) {
        this.hp = Math.max(0, this.hp - damage);
    }

    /**
     * Soigner l'unité
     * @param amount Points de vie à restaurer
     */
    public void heal(int amount) {
        this.hp = Math.min(maxHp, this.hp + amount);
    }

    // Getters
    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getRange() { return range; }
    public int getMovement() { return movement; }
    public int getCost() { return cost; }
    public String getName() { return name; }
    public Joueur getOwner() { return owner; }

    // Setters
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(maxHp, hp)); }
    public void setOwner(Joueur owner) { this.owner = owner; }

    // État de l'unité
    public boolean isAlive() { return hp > 0; }
    public boolean canMove() { return !hasMoved; }
    public boolean canAttack() { return !hasAttacked; }

    // Actions
    public void markMoved() { hasMoved = true; }
    public void markAttacked() { hasAttacked = true; }

    /**
     * Réinitialiser l'état de l'unité pour un nouveau tour
     */
    public void resetTurn() {
        hasMoved = false;
        hasAttacked = false;
    }

    /**
     * Obtenir le pourcentage de vie restant
     * @return Valeur entre 0.0 et 1.0
     */
    public double getHpPercentage() {
        return (double) hp / maxHp;
    }

    @Override
    public String toString() {
        return name + " (HP: " + hp + "/" + maxHp + ", ATK: " + attack + ", DEF: " + defense + ")";
    }
}