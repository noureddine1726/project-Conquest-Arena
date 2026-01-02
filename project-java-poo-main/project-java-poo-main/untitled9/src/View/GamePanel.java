package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import model.*;

/**
 * GamePanel - Interface de jeu principale
 * Gère l'affichage de la carte, des unités, des bâtiments et la logique du jeu
 */
public class GamePanel extends JPanel {
    private static final int TILE_SIZE = 45; // Reverted to original size
    private static final int ROWS = 15;
    private static final int COLS = 22;

    private Cell[][] map;
    private List<Batiment> batiments;
    private Joueur player1, player2;
    private boolean player1Turn = true;
    private Unit selectedUnit = null;
    private Batiment selectedBatiment = null;
    private Random random = new Random();

    private boolean constructionMode = false;
    private String buildingType = null;

    // Images
    private Image mapImage;
    private Image soldierImage;
    private Image archerImage;
    private Image cavalierImage;
    private Map<String, Image> buildingImages; // New map for building images

    public GamePanel() {
        map = new Cell[ROWS][COLS];
        batiments = new ArrayList<>();
        buildingImages = new HashMap<>();

        // Load images
        loadImages();

        initGreatMap();
        setupPlayers();
        addMouseListener(new MouseHandler(this));
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE + 50));
        setBackground(Color.decode("#1a1a1a"));
    }

    private void loadImages() {
        try {
            // Automatic loading with fallbacks
            // Map
            mapImage = Imageloader.loadImage("resources/image/map.png");
            if (mapImage == null)
                mapImage = Imageloader.loadImage("resources/image/map.jpg");
            if (mapImage == null)
                mapImage = Imageloader.loadImage("resources/image/map_sample.jpg");

            // Units
            soldierImage = loadUnitImage("soldier");
            archerImage = loadUnitImage("archer");
            cavalierImage = loadUnitImage("cavalier");

            // Buildings - Import logic
            String[] bTypes = { "centre", "caserne", "mine", "ferme", "scierie", "carriere" };
            for (String type : bTypes) {
                Image img = Imageloader.loadImage("resources/image/" + type + ".png");
                if (img == null)
                    img = Imageloader.loadImage("resources/image/" + type + ".jpg");
                if (img != null) {
                    buildingImages.put(type, img);
                }
            }

        } catch (Exception e) {
            System.err.println("Images loading failed (using default shapes): " + e.getMessage());
        }
    }

    private Image loadUnitImage(String name) {
        Image img = Imageloader.loadImage("resources/image/" + name + ".png");
        if (img == null)
            img = Imageloader.loadImage("resources/image/" + name + ".jpg");
        return img;
    }

    /**
     * Initialiser la carte avec différents types de terrains
     */
    private void initGreatMap() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                // Lac au centre
                if (r >= 6 && r <= 8 && c >= 8 && c <= 13) {
                    map[r][c] = new Cell("water", false);
                }
                // Montagnes dans les coins
                else if ((r <= 3 && c <= 4) || (r >= 11 && c >= 17)) {
                    map[r][c] = new Cell("mountain", false);
                }
                // Forêts dispersées
                else if ((r >= 2 && r <= 4 && c >= 6 && c <= 9) ||
                        (r >= 10 && r <= 12 && c >= 2 && c <= 5)) {
                    map[r][c] = new Cell("forest", true);
                }
                // Prairie par défaut
                else {
                    map[r][c] = new Cell("grass", true);
                }
            }
        }
    }

    /**
     * Configurer les joueurs avec leurs unités et bâtiments de départ
     */
    private void setupPlayers() {
        player1 = new Joueur("Joueur 1", true);
        player2 = new Joueur("IA Joueur 2", false);

        // Centres de commandement
        batiments.add(new Batiment(14, 4, "centre", player1));
        batiments.add(new Batiment(0, 17, "centre", player2));

        // Unités de départ
        player1.addUnite(new Soldier(13, 3, player1));
        player1.addUnite(new Archer(13, 5, player1));

        player2.addUnite(new Soldier(1, 18, player2));
        player2.addUnite(new Archer(1, 16, player2));
    }

    /**
     * Démarrer une nouvelle partie
     */
    public void nouvellePartie() {
        initGreatMap();
        batiments.clear();
        setupPlayers();
        player1Turn = true;
        selectedUnit = null;
        selectedBatiment = null;
        constructionMode = false;
        buildingType = null;
        repaint();
    }

    /**
     * Terminer le tour et passer au joueur suivant
     */
    public void finDeTour() {
        Joueur current = player1Turn ? player1 : player2;
        current.resetTurn();
        current.addGold(25);
        current.addNourriture(5);

        // Génération de revenus et progression de construction
        for (Batiment b : batiments) {
            if (b.getOwner() == current) {
                b.progressConstruction();
                b.generateIncome(b.getOwner());
            }
        }

        checkGameOver(); // Automatic Game Over Check

        player1Turn = !player1Turn;
        selectedUnit = null;
        selectedBatiment = null;
        constructionMode = false;
        buildingType = null;
        repaint();

        // AI plays automatically after player's turn
        if (!player1Turn) {
            // Use SwingWorker to run AI turn in background
            SwingWorker<Void, Void> aiWorker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    Thread.sleep(500); // Small delay for visual feedback
                    return null;
                }

                @Override
                protected void done() {
                    aiTurn();
                    checkGameOver();
                    // Automatically end AI turn and return to player
                    player1Turn = true;
                    repaint();
                }
            };
            aiWorker.execute();
        }
    }

    private void checkGameOver() {
        boolean p1Alive = hasAliveUnitsOrBuildings(player1);
        boolean p2Alive = hasAliveUnitsOrBuildings(player2);

        if (!p1Alive) {
            JOptionPane.showMessageDialog(this, "GAME OVER - L'IA a gagné !");
            nouvellePartie();
        } else if (!p2Alive) {
            JOptionPane.showMessageDialog(this, "VICTOIRE - Vous avez écrasé l'ennemi !");
            nouvellePartie();
        }
    }

    private boolean hasAliveUnitsOrBuildings(Joueur p) {
        if (p.getUnites().stream().anyMatch(Unit::isAlive))
            return true;
        return batiments.stream().anyMatch(b -> b.getOwner() == p);
    }

    /**
     * Entrer en mode construction
     */
    public void enterConstructionMode(String type) {
        constructionMode = true;
        buildingType = type;
        selectedUnit = null;
        selectedBatiment = null;
        repaint();
    }

    /**
     * Annuler le mode construction
     */
    public void cancelConstructionMode() {
        constructionMode = false;
        buildingType = null;
        repaint();
    }

    public boolean isInConstructionMode() {
        return constructionMode;
    }

    public boolean isPlayer1Turn() {
        return player1Turn;
    }

    public Joueur getPlayer1() {
        return player1;
    }

    public Joueur getPlayer2() {
        return player2;
    }

    public List<Batiment> getBatiments() {
        return new ArrayList<>(batiments);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawEpicMap(g2);
        drawBuildings(g2);
        drawRealisticUnits(g2, player1.getUnites(), true);
        drawRealisticUnits(g2, player2.getUnites(), false);

        if (selectedUnit != null) {
            drawAttackRange(g2);
        }

        if (constructionMode) {
            drawConstructionCursor(g2);
        }

        drawTurnIndicator(g2);
    }

    /**
     * Dessiner la carte avec les terrains
     */
    private void drawEpicMap(Graphics2D g2) {
        if (mapImage != null) {
            g2.drawImage(mapImage, 0, 0, COLS * TILE_SIZE, ROWS * TILE_SIZE, null);

            // Draw grid overlay
            g2.setColor(new Color(0, 0, 0, 50));
            g2.setStroke(new BasicStroke(1.0f));
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    g2.drawRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
            return;
        }

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Cell cell = map[r][c];
                String type = cell.getType();
                int x = c * TILE_SIZE;
                int y = r * TILE_SIZE;

                if ("grass".equals(type)) {
                    GradientPaint grassGrad = new GradientPaint(0, 0, new Color(20, 100, 20),
                            TILE_SIZE, TILE_SIZE, new Color(40, 120, 40));
                    g2.setPaint(grassGrad);
                    g2.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                } else if ("forest".equals(type)) {
                    g2.setColor(new Color(10, 60, 20));
                    g2.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                    g2.setColor(new Color(80, 50, 20));
                    g2.fillRect(x + TILE_SIZE / 2 - 4, y + TILE_SIZE / 2, 8, TILE_SIZE / 3);
                    g2.setColor(new Color(0, 70, 0));
                    g2.fillOval(x + TILE_SIZE / 4, y + TILE_SIZE / 4, TILE_SIZE / 2, TILE_SIZE / 2);
                } else if ("water".equals(type)) {
                    GradientPaint waterGrad = new GradientPaint(0, 0, new Color(20, 80, 150),
                            TILE_SIZE, TILE_SIZE, new Color(40, 120, 200));
                    g2.setPaint(waterGrad);
                    g2.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                    g2.setColor(new Color(255, 255, 255, 100));
                    g2.drawLine(x + 5, y + 15, x + 25, y + 15);
                } else if ("mountain".equals(type)) {
                    GradientPaint mountainGrad = new GradientPaint(0, 0, new Color(90, 90, 90),
                            0, TILE_SIZE, new Color(120, 120, 120));
                    g2.setPaint(mountainGrad);
                    g2.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                    g2.setColor(Color.WHITE);
                    g2.fillOval(x + TILE_SIZE / 4, y + TILE_SIZE / 8, TILE_SIZE / 2, TILE_SIZE / 4);
                }

                g2.setColor(new Color(60, 60, 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRect(x, y, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    /**
     * Dessiner les bâtiments
     */
    private void drawBuildings(Graphics2D g2) {
        for (Batiment b : batiments) {
            int x = b.getCol() * TILE_SIZE;
            int y = b.getRow() * TILE_SIZE;

            // "Big Building" Effect
            int drawSize = TILE_SIZE + 20;
            int drawX = x - 10;
            int drawY = y - 20;

            Color buildingColor = b.getOwner().isPlayer1() ? Color.CYAN : Color.ORANGE;

            if (b == selectedBatiment) {
                g2.setColor(new Color(255, 255, 0, 150));
                g2.setStroke(new BasicStroke(3));
                g2.drawOval(x, y + TILE_SIZE / 2, TILE_SIZE, TILE_SIZE / 2);
                g2.setStroke(new BasicStroke(1));
            }

            // Image import support
            Image bImg = buildingImages.get(b.getType());
            if (bImg != null && b.isConstructed()) {
                g2.drawImage(bImg, drawX, drawY, drawSize, drawSize, null);
                // Label owner color
                g2.setColor(buildingColor);
                g2.fillOval(drawX + drawSize - 15, drawY + 10, 10, 10);
                continue;
            }

            // Fallback render
            if (!b.isConstructed()) {
                g2.setColor(buildingColor.darker().darker());
                g2.fillRect(drawX + 15, drawY + 25, drawSize - 30, drawSize - 35);
                g2.setColor(Color.GRAY);
                int progress = (int) ((1.0 - (double) b.getRemainingTime() / b.getConstructionTime())
                        * (drawSize - 30));
                g2.fillRect(drawX + 15, drawY + drawSize - 10, progress, 5);
            } else {
                g2.setColor(buildingColor.darker());
                g2.fillRect(drawX + 10, drawY + 20, drawSize - 20, drawSize - 30);
                g2.setColor(buildingColor.brighter());
                int[] roofX = { drawX + 5, drawX + drawSize / 2, drawX + drawSize - 5 };
                int[] roofY = { drawY + 20, drawY, drawY + 20 };
                g2.fillPolygon(roofX, roofY, 3);
            }

            g2.setColor(Color.BLACK);
            g2.fillRect(drawX + drawSize / 2 - 8, drawY + drawSize - 20, 16, 20);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            String label = switch (b.getType()) {
                case "centre" -> "C";
                case "caserne" -> "K";
                case "mine" -> "M";
                case "ferme" -> "F";
                case "scierie" -> "S";
                case "carriere" -> "P";
                default -> "?";
            };
            g2.drawString(label, drawX + drawSize / 2 - 5, drawY + drawSize - 25);
        }
    }

    private void drawRealisticUnits(Graphics2D g2, List<Unit> units, boolean isPlayer1) {
        Color teamColor = isPlayer1 ? new Color(50, 150, 255) : new Color(255, 80, 80);

        for (Unit u : units) {
            if (!u.isAlive())
                continue;

            int x = u.getCol() * TILE_SIZE;
            int y = u.getRow() * TILE_SIZE;

            // "Big Player" Effect
            int drawSize = TILE_SIZE + 15;
            int drawX = x - 7;
            int drawY = y - 15;

            if (u == selectedUnit) {
                g2.setColor(new Color(255, 255, 0, 150));
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(x + 2, y + TILE_SIZE - 10, TILE_SIZE - 4, 8);
                g2.setStroke(new BasicStroke(1));
            }

            if (u instanceof Soldier) {
                drawSoldier(g2, drawX, drawY, drawSize, teamColor);
            } else if (u instanceof Archer) {
                drawArcher(g2, drawX, drawY, drawSize, teamColor);
            } else if (u instanceof Cavalier) {
                drawCavalier(g2, drawX, drawY, drawSize, teamColor);
            }

            // Health bar
            double hpPercent = u.getHpPercentage();
            int hpWidth = Math.max(4, (int) (hpPercent * (TILE_SIZE)));
            g2.setColor(hpPercent > 0.6 ? Color.GREEN : hpPercent > 0.3 ? Color.ORANGE : Color.RED);
            g2.fillRect(x, drawY - 5, hpWidth, 4);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, drawY - 5, TILE_SIZE, 4);
        }
    }

    private void drawSoldier(Graphics2D g2, int x, int y, int size, Color teamColor) {
        if (soldierImage != null) {
            g2.drawImage(soldierImage, x, y, size, size, null);
            g2.setColor(teamColor);
            g2.fillOval(x + size - 12, y + size - 12, 8, 8);
            return;
        }
        g2.setColor(teamColor.darker());
        g2.fillRect(x + size / 4, y + size / 4, size / 2, size / 2);
        g2.setColor(teamColor);
        g2.fillOval(x + size / 3, y + size / 6, size / 3, size / 3);
    }

    private void drawArcher(Graphics2D g2, int x, int y, int size, Color teamColor) {
        if (archerImage != null) {
            g2.drawImage(archerImage, x, y, size, size, null);
            g2.setColor(teamColor);
            g2.fillOval(x + size - 12, y + size - 12, 8, 8);
            return;
        }
        g2.setColor(teamColor.darker());
        g2.fillRect(x + size / 4, y + size / 4, size / 2, size / 2);
        g2.setColor(Color.WHITE);
        g2.drawArc(x + 5, y + 5, size - 10, size - 10, 90, 180);
    }

    private void drawCavalier(Graphics2D g2, int x, int y, int size, Color teamColor) {
        if (cavalierImage != null) {
            g2.drawImage(cavalierImage, x, y, size, size, null);
            g2.setColor(teamColor);
            g2.fillOval(x + size - 12, y + size - 12, 8, 8);
            return;
        }
        g2.setColor(teamColor.darker());
        g2.fillRect(x + size / 4, y + size / 4, size / 2, size / 2);
        g2.setColor(Color.BLACK);
        g2.fillOval(x + 5, y + size / 2, size - 10, size / 3);
    }

    /**
     * Afficher la portée d'attaque de l'unité sélectionnée
     */
    private void drawAttackRange(Graphics2D g2) {
        if (selectedUnit == null || !selectedUnit.canAttack())
            return;

        g2.setColor(new Color(255, 0, 0, 50));
        int range = selectedUnit.getRange();
        int ux = selectedUnit.getCol();
        int uy = selectedUnit.getRow();

        for (int r = Math.max(0, uy - range); r <= Math.min(ROWS - 1, uy + range); r++) {
            for (int c = Math.max(0, ux - range); c <= Math.min(COLS - 1, ux + range); c++) {
                int dist = Math.abs(uy - r) + Math.abs(ux - c);
                if (dist <= range && dist > 0) {
                    g2.fillRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    /**
     * Afficher le curseur en mode construction
     */
    private void drawConstructionCursor(Graphics2D g2) {
        Point mouse = getMousePosition();
        if (mouse != null) {
            int col = mouse.x / TILE_SIZE;
            int row = mouse.y / TILE_SIZE;
            if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                g2.setColor(new Color(0, 255, 0, 100));
                g2.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                g2.setColor(Color.GREEN);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    /**
     * Afficher l'indicateur de tour
     */
    private void drawTurnIndicator(Graphics2D g2) {
        String turnText = player1Turn ? "🟦 TOUR JOUEUR 1" : "🔴 TOUR IA JOUEUR 2";
        g2.setColor(player1Turn ? Color.BLUE : Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2.getFontMetrics();
        int width = fm.stringWidth(turnText);
        g2.drawString(turnText, (getWidth() - width) / 2, 35);
    }

    /**
     * Intelligence artificielle - Tour de l'IA (AMÉLIORÉ)
     */
    private void aiTurn() {
        List<Unit> aiUnits = player2.getUnites();

        // 1. GESTION ÉCONOMIQUE
        // Si on a moins de 2 bâtiments de production et assez de ressources, construire
        long prodBuildings = batiments.stream()
                .filter(b -> b.getOwner() == player2 && (b.getType().equals("mine") || b.getType().equals("ferme")))
                .count();

        if (player2.getGold() >= 80 && prodBuildings < 3) {
            String type = "mine";
            if (player2.getNourriture() < 50)
                type = "ferme";
            else if (player2.getBois() < 50)
                type = "scierie";

            tryBuildBuilding(player2, type);
        }

        // 2. RECRUTEMENT MILITAIRE
        // Si on a une caserne et assez de ressources
        Batiment caserne = findBuildingType("caserne", player2);
        if (caserne == null && player2.getGold() >= 100) {
            tryBuildBuilding(player2, "caserne");
        } else if (caserne != null && caserne.isConstructed()) {
            // Si on a moins de 8 unités ou beaucoup de ressources
            if (aiUnits.size() < 8 || player2.getGold() > 300) {
                int[] pos = getRandomWalkableTileAround(caserne.getRow(), caserne.getCol(), 2);
                if (pos != null) {
                    Unit newUnit = random.nextDouble() < 0.6 ? new Soldier(pos[0], pos[1], player2)
                            : new Archer(pos[0], pos[1], player2);
                    player2.recruter(newUnit);
                }
            }
        }

        // 3. COMBAT ET DÉPLACEMENT
        // Cibler le centre ennemi si pas d'unités proches
        Batiment enemyCenter = findBuildingType("centre", player1);

        for (Unit aiUnit : aiUnits) {
            if (!aiUnit.isAlive())
                continue;

            Unit closestEnemy = findClosestEnemy(aiUnit);

            // ATTAQUE
            if (closestEnemy != null && aiUnit.canAttack() && aiUnit.isInRange(closestEnemy)) {
                aiUnit.attack(closestEnemy);
                continue;
            }

            // DÉPLACEMENT STRATÉGIQUE
            if (aiUnit.canMove()) {
                int[] target = null;

                // Si ennemi proche, aller vers lui
                if (closestEnemy != null) {
                    target = new int[] { closestEnemy.getRow(), closestEnemy.getCol() };
                }
                // Sinon, aller vers le centre ennemi
                else if (enemyCenter != null) {
                    target = new int[] { enemyCenter.getRow(), enemyCenter.getCol() };
                }
                // Sinon explorer
                else {
                    int[] rand = getRandomWalkableTile();
                    if (rand != null)
                        target = rand;
                }

                if (target != null) {
                    int[] move = findBestMoveToTarget(aiUnit, target[0], target[1]);
                    if (move != null) {
                        aiUnit.setPosition(move[0], move[1]);
                        aiUnit.markMoved();
                    }
                }
            }
        }

        player1.removeDeadUnits();
        player2.removeDeadUnits();
    }

    // Helper pour construire proche du centre
    private void tryBuildBuilding(Joueur player, String type) {
        Batiment center = findBuildingType("centre", player);
        if (center == null)
            return;

        // Chercher une case libre autour du centre
        int[] pos = getRandomWalkableTileAround(center.getRow(), center.getCol(), 4);
        if (pos != null && findBatimentAt(pos[0], pos[1]) == null) {
            Batiment newBuilding = new Batiment(pos[0], pos[1], type, player, true);
            if (player.peutConstruire(newBuilding)) {
                player.construire(newBuilding);
                batiments.add(newBuilding);
            }
        }
    }

    private Unit findClosestEnemy(Unit aiUnit) {
        Unit closest = null;
        double minDist = Double.MAX_VALUE;

        for (Unit enemy : player1.getUnites()) {
            if (enemy.isAlive()) {
                double dist = Math.sqrt(Math.pow(aiUnit.getRow() - enemy.getRow(), 2) +
                        Math.pow(aiUnit.getCol() - enemy.getCol(), 2));
                if (dist < minDist) {
                    minDist = dist;
                    closest = enemy;
                }
            }
        }
        return closest;
    }

    private int[] findBestMoveToTarget(Unit aiUnit, int targetRow, int targetCol) {
        int bestRow = aiUnit.getRow();
        int bestCol = aiUnit.getCol();
        double bestDist = Double.MAX_VALUE;
        boolean found = false;

        for (int r = Math.max(0, aiUnit.getRow() - aiUnit.getMovement()); r <= Math.min(ROWS - 1,
                aiUnit.getRow() + aiUnit.getMovement()); r++) {
            for (int c = Math.max(0, aiUnit.getCol() - aiUnit.getMovement()); c <= Math.min(COLS - 1,
                    aiUnit.getCol() + aiUnit.getMovement()); c++) {

                if (Math.abs(r - aiUnit.getRow()) + Math.abs(c - aiUnit.getCol()) > aiUnit.getMovement())
                    continue; // Manhattan distance check roughly

                if (map[r][c].isWalkable() && findUnitAt(r, c) == null && findBatimentAt(r, c) == null) {
                    double dist = Math.sqrt(Math.pow(r - targetRow, 2) + Math.pow(c - targetCol, 2));
                    if (dist < bestDist) {
                        bestDist = dist;
                        bestRow = r;
                        bestCol = c;
                        found = true;
                    }
                }
            }
        }
        return found ? new int[] { bestRow, bestCol } : null;
    }

    private int[] getRandomWalkableTile() {
        List<int[]> walkableTiles = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (map[r][c].isWalkable() && findUnitAt(r, c) == null && findBatimentAt(r, c) == null) {
                    walkableTiles.add(new int[] { r, c });
                }
            }
        }
        return walkableTiles.isEmpty() ? null : walkableTiles.get(random.nextInt(walkableTiles.size()));
    }

    private int[] getRandomWalkableTileAround(int centerR, int centerC, int radius) {
        List<int[]> valid = new ArrayList<>();
        for (int r = Math.max(0, centerR - radius); r <= Math.min(ROWS - 1, centerR + radius); r++) {
            for (int c = Math.max(0, centerC - radius); c <= Math.min(COLS - 1, centerC + radius); c++) {
                if (map[r][c].isWalkable() && findUnitAt(r, c) == null && findBatimentAt(r, c) == null) {
                    valid.add(new int[] { r, c });
                }
            }
        }
        if (valid.isEmpty())
            return null;
        return valid.get(random.nextInt(valid.size()));
    }

    private Unit findUnitAt(int row, int col) {
        for (Unit u : player1.getUnites()) {
            if (u.isAlive() && u.getRow() == row && u.getCol() == col)
                return u;
        }
        for (Unit u : player2.getUnites()) {
            if (u.isAlive() && u.getRow() == row && u.getCol() == col)
                return u;
        }
        return null;
    }

    private Batiment findBatimentAt(int row, int col) {
        for (Batiment b : batiments) {
            if (b.getRow() == row && b.getCol() == col)
                return b;
        }
        return null;
    }

    private Batiment findBuildingType(String type, Joueur owner) {
        for (Batiment b : batiments) {
            if (b.getType().equals(type) && b.getOwner() == owner && b.isConstructed()) {
                return b;
            }
        }
        return null;
    }

    /**
     * Gestionnaire des clics souris
     */
    private class MouseHandler extends MouseAdapter {
        private final GamePanel panel;

        public MouseHandler(GamePanel panel) {
            this.panel = panel;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int col = e.getX() / TILE_SIZE;
            int row = e.getY() / TILE_SIZE;

            if (row < 0 || row >= ROWS || col < 0 || col >= COLS)
                return;

            // Mode construction
            if (constructionMode && buildingType != null) {
                if (map[row][col].isWalkable() && findUnitAt(row, col) == null && findBatimentAt(row, col) == null) {
                    Batiment newBuilding = new Batiment(row, col, buildingType, player1, true);
                    if (player1.construire(newBuilding)) {
                        batiments.add(newBuilding);
                        constructionMode = false;
                        buildingType = null;
                        panel.repaint();
                    } else {
                        JOptionPane.showMessageDialog(panel, "Ressources insuffisantes!");
                    }
                }
                return;
            }

            if (!map[row][col].isWalkable())
                return;

            Joueur current = panel.player1Turn ? panel.player1 : panel.player2;

            if (!panel.player1Turn)
                return; // Prevent clicking during AI turn

            // Sélection de bâtiment
            Batiment clickedBuilding = findBatimentAt(row, col);
            if (clickedBuilding != null && clickedBuilding.getOwner() == current) {
                selectedBatiment = clickedBuilding;
                selectedUnit = null;
                panel.repaint();
                return;
            }

            // Sélection d'unité
            Unit clicked = findUnitAt(row, col);
            if (clicked != null && clicked.getOwner() == current) {
                selectedUnit = clicked;
                selectedBatiment = null;
                panel.repaint();
                return;
            }

            // Action avec unité sélectionnée
            if (selectedUnit != null && selectedUnit.getOwner() == current) {
                Unit target = findUnitAt(row, col);

                if (target != null && target.getOwner() != current && selectedUnit.canAttack()) {
                    if (selectedUnit.isInRange(target)) {
                        selectedUnit.attack(target);
                        selectedUnit.markAttacked();
                        player1.removeDeadUnits();
                        player2.removeDeadUnits();
                        selectedUnit = null;
                        panel.repaint();
                        checkGameOver();
                    } else {
                        JOptionPane.showMessageDialog(panel, "Cible hors de portée!");
                    }
                    return;
                }

                if (selectedUnit.canMove() && findUnitAt(row, col) == null && findBatimentAt(row, col) == null) {
                    selectedUnit.setPosition(row, col);
                    selectedUnit.markMoved();
                    panel.repaint();
                    return;
                }
            }
        }
    }
}