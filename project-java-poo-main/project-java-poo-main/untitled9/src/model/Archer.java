package model;

/**
 * Classe Archer - Unité à distance
 * Caractéristiques :
 * - HP: 80 (fragile)
 * - Attaque: 40 (très forte)
 * - Défense: 5 (faible)
 * - Portée: 3 (attaque à distance!)
 * - Mouvement: 2 cases
 * - Coût: 70 Or
 *
 * Stratégie : Garder à l'arrière, derrière les soldats
 */
public class Archer extends Unit {

    /**
     * Constructeur avec position et propriétaire
     */
    public Archer(int row, int col, Joueur owner) {
        super(row, col, owner,
                80,     // maxHp - Fragile
                40,     // attack - Dégâts élevés
                5,      // defense - Très vulnérable
                3,      // range - Attaque à distance (avantage principal!)
                2,      // movement - Mobilité standard
                70,     // cost - Plus cher que le soldat
                "Archer"
        );
    }

    /**
     * Constructeur par défaut
     */
    public Archer() {
        this(-1, -1, null);
    }
}