package model;

/**
 * Classe Soldier - Unité de base équilibrée
 * Caractéristiques :
 * - HP: 100
 * - Attaque: 30
 * - Défense: 10
 * - Portée: 1 (corps à corps)
 * - Mouvement: 2 cases
 * - Coût: 50 Or
 */
public class Soldier extends Unit {

    /**
     * Constructeur avec position et propriétaire
     */
    public Soldier(int row, int col, Joueur owner) {
        super(row, col, owner,
                100,    // maxHp - Points de vie élevés
                30,     // attack - Attaque moyenne
                10,     // defense - Bonne défense
                1,      // range - Corps à corps uniquement
                2,      // movement - Mobilité standard
                50,     // cost - Bon rapport qualité/prix
                "Soldat"
        );
    }

    /**
     * Constructeur par défaut (pour tests ou placement ultérieur)
     */
    public Soldier() {
        this(-1, -1, null);
    }
}