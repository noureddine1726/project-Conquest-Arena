package View;

import model.*;
import javax.swing.*;
import java.awt.*;

/**
 * MainFrame - Fenêtre principale du jeu
 * Contient l'interface de jeu et tous les contrôles
 */
public class MainFrame extends JFrame {
    private GamePanel gamePanel;
    private JLabel statusLabel;
    private JLabel ressourcesLabel;
    private JButton nouvellePartieBtn;
    private JButton finTourBtn;
    private JPanel buildingPanel;
    private JPanel unitPanel;

    public MainFrame() {
        super("Conquest Arena");
        setLayout(new BorderLayout());

        gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);

        // Top panel - Statut et ressources
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        statusLabel = new JLabel("Tour du Joueur 1", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(100, 150, 255));
        statusLabel.setForeground(Color.WHITE);

        ressourcesLabel = new JLabel(updateRessourcesText(), SwingConstants.CENTER);
        ressourcesLabel.setFont(new Font("Arial", Font.BOLD, 12));
        ressourcesLabel.setOpaque(true);
        ressourcesLabel.setBackground(Color.WHITE);

        topPanel.add(statusLabel);
        topPanel.add(ressourcesLabel);
        add(topPanel, BorderLayout.NORTH);

        // Right panel - Contrôles
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(200, 600));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de contrôles principaux
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createTitledBorder("🎮 Contrôles"));

        nouvellePartieBtn = new JButton("🆕 Nouvelle Partie");
        finTourBtn = new JButton("➡️ Fin de Tour");

        nouvellePartieBtn.setMaximumSize(new Dimension(180, 35));
        finTourBtn.setMaximumSize(new Dimension(180, 35));

        controlPanel.add(nouvellePartieBtn);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(finTourBtn);

        // Panel de construction de bâtiments
        buildingPanel = new JPanel();
        buildingPanel.setLayout(new BoxLayout(buildingPanel, BoxLayout.Y_AXIS));
        buildingPanel.setBorder(BorderFactory.createTitledBorder("🏗️ Bâtiments"));

        JButton caserneBtn = new JButton("Caserne (100💰 50🪵 30🪨)");
        JButton mineBtn = new JButton("Mine (80💰 40🪵 20🪨)");
        JButton fermeBtn = new JButton("Ferme (60💰 30🪵 10🪨)");
        JButton scierieBtn = new JButton("Scierie (70💰 20🪵 15🪨)");
        JButton carriereBtn = new JButton("Carrière (75💰 25🪵 10🪨)");

        caserneBtn.setMaximumSize(new Dimension(180, 30));
        mineBtn.setMaximumSize(new Dimension(180, 30));
        fermeBtn.setMaximumSize(new Dimension(180, 30));
        scierieBtn.setMaximumSize(new Dimension(180, 30));
        carriereBtn.setMaximumSize(new Dimension(180, 30));

        buildingPanel.add(caserneBtn);
        buildingPanel.add(Box.createVerticalStrut(5));
        buildingPanel.add(mineBtn);
        buildingPanel.add(Box.createVerticalStrut(5));
        buildingPanel.add(fermeBtn);
        buildingPanel.add(Box.createVerticalStrut(5));
        buildingPanel.add(scierieBtn);
        buildingPanel.add(Box.createVerticalStrut(5));
        buildingPanel.add(carriereBtn);

        // Panel de recrutement d'unités
        unitPanel = new JPanel();
        unitPanel.setLayout(new BoxLayout(unitPanel, BoxLayout.Y_AXIS));
        unitPanel.setBorder(BorderFactory.createTitledBorder("⚔️ Recruter Unités"));

        JButton soldierBtn = new JButton("Soldat (50💰 10🍖)");
        JButton archerBtn = new JButton("Archer (70💰 10🍖)");
        JButton cavalierBtn = new JButton("Cavalier (100💰 10🍖)");

        soldierBtn.setMaximumSize(new Dimension(180, 30));
        archerBtn.setMaximumSize(new Dimension(180, 30));
        cavalierBtn.setMaximumSize(new Dimension(180, 30));

        unitPanel.add(soldierBtn);
        unitPanel.add(Box.createVerticalStrut(5));
        unitPanel.add(archerBtn);
        unitPanel.add(Box.createVerticalStrut(5));
        unitPanel.add(cavalierBtn);

        // Panel d'informations
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("ℹ️ Info"));

        JTextArea infoText = new JTextArea(8, 15);
        infoText.setEditable(false);
        infoText.setText(
                "🎯 Objectif:\n" +
                        "• Détruire l'ennemi\n" +
                        "• Dominer la carte\n\n" +
                        "🎮 Contrôles:\n" +
                        "• Clic: Sélectionner\n" +
                        "• Clic case: Déplacer\n" +
                        "• Clic ennemi: Attaquer");
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(infoText);
        infoPanel.add(scrollPane);

        // Ajouter tous les panels au panel de droite
        rightPanel.add(controlPanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(buildingPanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(unitPanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(infoPanel);

        add(rightPanel, BorderLayout.EAST);

        // Action listeners
        nouvellePartieBtn.addActionListener(e -> nouvellePartie());
        finTourBtn.addActionListener(e -> finTour());

        caserneBtn.addActionListener(e -> construire("caserne"));
        mineBtn.addActionListener(e -> construire("mine"));
        fermeBtn.addActionListener(e -> construire("ferme"));
        scierieBtn.addActionListener(e -> construire("scierie"));
        carriereBtn.addActionListener(e -> construire("carriere"));

        soldierBtn.addActionListener(e -> recruter("soldier"));
        archerBtn.addActionListener(e -> recruter("archer"));
        cavalierBtn.addActionListener(e -> recruter("cavalier"));

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Mettre à jour l'affichage des ressources
     */
    private String updateRessourcesText() {
        return String.format(
                "💰 Or:%d | 🪵 Bois:%d | 🪨 Pierre:%d | 🍖 Nour:%d | 👥 Unités:%d/%d",
                gamePanel.getPlayer1().getGold(),
                gamePanel.getPlayer1().getBois(),
                gamePanel.getPlayer1().getPierre(),
                gamePanel.getPlayer1().getNourriture(),
                gamePanel.getPlayer1().getUnitCount(),
                gamePanel.getPlayer2().getUnitCount());
    }

    /**
     * Démarrer une nouvelle partie
     */
    private void nouvellePartie() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Commencer une nouvelle partie?",
                "Nouvelle Partie",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            gamePanel.nouvellePartie();
            updateStatus();
            updateRessources();
        }
    }

    /**
     * Terminer le tour
     */
    private void finTour() {
        gamePanel.finDeTour();
        updateStatus();
        updateRessources();
    }

    /**
     * Construire un bâtiment
     */
    private void construire(String type) {
        if (!gamePanel.isPlayer1Turn()) {
            JOptionPane.showMessageDialog(this, "Ce n'est pas votre tour!");
            return;
        }

        Batiment testBuilding = new Batiment(0, 0, type, gamePanel.getPlayer1(), true);
        if (!gamePanel.getPlayer1().peutConstruire(testBuilding)) {
            JOptionPane.showMessageDialog(
                    this,
                    String.format("Ressources insuffisantes!\nCoût: %d💰 %d🪵 %d🪨",
                            testBuilding.getCostGold(),
                            testBuilding.getCostBois(),
                            testBuilding.getCostPierre()),
                    "Construction impossible",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        gamePanel.enterConstructionMode(type);
        JOptionPane.showMessageDialog(
                this,
                "Cliquez sur la carte pour placer le bâtiment",
                "Mode Construction",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Recruter une unité
     */
    private void recruter(String unitType) {
        if (!gamePanel.isPlayer1Turn()) {
            JOptionPane.showMessageDialog(this, "Ce n'est pas votre tour!");
            return;
        }

        // Vérifier si une caserne existe
        boolean hasCaserne = false;
        for (Batiment b : gamePanel.getBatiments()) {
            if (b.getType().equals("caserne") &&
                    b.getOwner() == gamePanel.getPlayer1() &&
                    b.isConstructed()) {
                hasCaserne = true;
                break;
            }
        }

        if (!hasCaserne) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vous devez d'abord construire une Caserne!",
                    "Recrutement impossible",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Trouver une position libre
        int[] pos = findSpawnPosition();
        if (pos == null) {
            JOptionPane.showMessageDialog(this, "Pas de place pour recruter!");
            return;
        }

        Unit newUnit = switch (unitType) {
            case "soldier" -> new Soldier(pos[0], pos[1], gamePanel.getPlayer1());
            case "archer" -> new Archer(pos[0], pos[1], gamePanel.getPlayer1());
            case "cavalier" -> new Cavalier(pos[0], pos[1], gamePanel.getPlayer1());
            default -> null;
        };

        if (newUnit != null) {
            if (gamePanel.getPlayer1().recruter(newUnit)) {
                updateRessources();
                gamePanel.repaint();
                JOptionPane.showMessageDialog(
                        this,
                        newUnit.getName() + " recruté avec succès!",
                        "Recrutement",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Ressources insuffisantes!\nCoût: " + newUnit.getCost() + "💰 10🍖",
                        "Recrutement impossible",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Trouver une position de spawn pour les nouvelles unités
     */
    private int[] findSpawnPosition() {
        // Chercher près du centre du joueur 1
        int centerRow = 14, centerCol = 4;
        for (int r = centerRow - 2; r <= centerRow + 2; r++) {
            for (int c = centerCol - 2; c <= centerCol + 2; c++) {
                if (r >= 0 && r < 15 && c >= 0 && c < 22) {
                    return new int[] { r, c };
                }
            }
        }
        return new int[] { 13, 6 }; // Position par défaut
    }

    /**
     * Mettre à jour le statut du tour
     */
    private void updateStatus() {
        boolean isP1 = gamePanel.isPlayer1Turn();
        statusLabel.setText(isP1 ? "🟦 Tour du Joueur 1" : "🔴 Tour de l'IA");
        statusLabel.setBackground(isP1 ? new Color(100, 150, 255) : new Color(255, 100, 100));
    }

    /**
     * Mettre à jour l'affichage des ressources
     */
    private void updateRessources() {
        ressourcesLabel.setText(updateRessourcesText());
    }

    /**
     * Point d'entrée principal
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame();
        });
    }


}