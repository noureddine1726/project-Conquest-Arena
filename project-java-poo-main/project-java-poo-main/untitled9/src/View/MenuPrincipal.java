package View;

import javax.swing.*;
import java.awt.*;

/**
 * MenuPrincipal - Menu de démarrage du jeu
 * Interface d'accueil avec options de jeu
 */
public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        super(" Conquest Arena- Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal avec gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(20, 30, 48),
                        0, getHeight(), new Color(36, 59, 85));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        // Titre principal
        JLabel titleLabel = new JLabel("Menu Principal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 38));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Sous-titre
        JLabel subtitleLabel = new JLabel(" Conquest Arena");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        subtitleLabel.setForeground(new Color(200, 220, 255));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Boutons du menu
        JButton nouvellePartieBtn = createMenuButton(" Nouvelle Partie");
        JButton chargerBtn = createMenuButton(" Charger Partie");
        JButton optionsBtn = createMenuButton("⚙ Options");
        JButton reglesBtn = createMenuButton(" Règles du Jeu");
        JButton creditsBtn = createMenuButton(" Crédits");
        JButton quitterBtn = createMenuButton(" Quitter");

        // Désactiver temporairement les boutons non implémentés
        chargerBtn.setEnabled(false);
        optionsBtn.setEnabled(false);

        // Actions des boutons
        nouvellePartieBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new MainFrame());
        });

        reglesBtn.addActionListener(e -> afficherRegles());
        creditsBtn.addActionListener(e -> afficherCredits());

        quitterBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Voulez-vous vraiment quitter?",
                    "Quitter",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Ajout des composants au panel
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(60));
        mainPanel.add(nouvellePartieBtn);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(chargerBtn);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(optionsBtn);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(reglesBtn);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(creditsBtn);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(quitterBtn);
        mainPanel.add(Box.createVerticalStrut(30));

        add(mainPanel);
        setVisible(true);
    }

    /**
     * Créer un bouton stylisé pour le menu
     */
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(350, 45));
        button.setMaximumSize(new Dimension(350, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 160, 220), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));

        // Effets de survol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 160, 220));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });

        return button;
    }

    /**
     * Afficher les règles du jeu
     */
    private void afficherRegles() {
        String regles = "🎯 OBJECTIF DU JEU:\n" +
                "Détruire toutes les unités ennemies ou dominer la carte\n\n" +
                "⚔️ UNITÉS:\n" +
                "• Soldat: Équilibré, corps à corps\n" +
                "• Archer: Attaque à distance (portée 3)\n" +
                "• Cavalier: Très mobile, bonus vs archers\n\n" +
                "🏗️ BÂTIMENTS:\n" +
                "• Centre: Bâtiment principal\n" +
                "• Caserne: Recruter des unités\n" +
                "• Mine: Génère de l'or\n" +
                "• Ferme: Génère de la nourriture\n" +
                "• Scierie: Génère du bois\n" +
                "• Carrière: Génère de la pierre\n\n" +
                "🎮 CONTRÔLES:\n" +
                "• Clic sur unité: Sélectionner\n" +
                "• Clic sur case: Déplacer\n" +
                "• Clic sur ennemi: Attaquer\n" +
                "• Boutons à droite: Construire et recruter\n\n" +
                "💡 CONSEILS:\n" +
                "• Construisez des mines pour l'économie\n" +
                "• Variez vos unités pour la stratégie\n" +
                "• Protégez votre centre de commandement\n" +
                "• Les forêts donnent des bonus de défense";

        JTextArea textArea = new JTextArea(regles);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 13));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "📖 Règles du Jeu",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Afficher les crédits
     */
    private void afficherCredits() {
        String credits = "⚔️ Conquest Arena ⚔️\n\n" +
                "Développé par: [khelfaoui noureddine,amara lotfi,chikh mouhamed cherif,brnchaba mouhamed djebril]\n" +
                "Projet: JEU DE STRATÉGIE\n" +
                "Langage: Java\n" +
                "Framework: Swing\n\n" +
                "📚 Technologies utilisées:\n" +
                "• POO (Programmation Orientée Objet)\n" +
                "• Design Patterns\n" +
                "• Collections Java\n" +
                "• Interface Graphique Swing\n\n" +
                "🎓 Fonctionnalités:\n" +
                "• Système de combat au tour par tour\n" +
                "• Gestion des ressources\n" +
                "• Construction de bâtiments\n" +
                "• Intelligence artificielle\n" +
                "• Interface graphique complète\n\n" +
                "Merci d'avoir joué! 🎮";

        JOptionPane.showMessageDialog(
                this,
                credits,
                "👥 Crédits",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Point d'entrée principal du jeu
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MenuPrincipal();
        });
    }
}

