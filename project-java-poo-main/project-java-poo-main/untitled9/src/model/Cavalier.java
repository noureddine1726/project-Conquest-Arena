package model;

/**
 * Classe Cavalier - Unité d'élite mobile
 * Caractéristiques :
 * - HP: 120 (très résistant)
 * - Attaque: 35
 * - Défense: 8
 * - Portée: 1 (corps à corps)
 * - Mouvement: 4 (très mobile!)
 * - Coût: 100 Or (cher)
 *
 * Capacité spéciale : +15 dégâts contre les Archers
 * Stratégie : Contourner l'ennemi, raids rapides, chasser les archers
 */
public class Cavalier extends Unit {

    /**
     * Constructeur avec position et propriétaire
     */
    public Cavalier(int row, int col, Joueur owner) {
        super(row, col, owner,
                120,    // maxHp - Très résistant
                35,     // attack - Attaque correcte
                8,      // defense - Bonne défense
                1,      // range - Corps à corps
                4,      // movement - Très mobile (avantage principal!)
                100,    // cost - Unité d'élite coûteuse
                "Cavalier"
        );
    }

    /**
     * Constructeur par défaut
     */
    public Cavalier() {
        this(-1, -1, null);
    }

    /**
     * Attaque avec bonus spécial contre les Archers
     * Le cavalier inflige +15 dégâts aux archers
     */
    @Override
    public void attack(Unit target) {
        if (target != null && target.isAlive() && target.getOwner() != owner && !hasAttacked && isInRange(target)) {
            // Bonus de +15 dégâts contre les archers
            int bonus = (target instanceof Archer) ? 15 : 0;
            int damage = Math.max(0, this.attack + bonus - target.defense + (int)(Math.random() * 11) - 5);
            target.takeDamage(damage);
            hasAttacked = true;
        }
    }
}
