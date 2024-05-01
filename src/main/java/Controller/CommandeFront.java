package Controller;

import Entities.Commande;
import Entities.Livreur;
import Service.CommandeService;
import Service.LivreurService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import Controller.NavigationController;
import javafx.util.Callback;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.IOException;
import java.util.List;
import java.sql.SQLException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.awt.Color;


public class CommandeFront {


    private Stage primaryStage;



    @FXML
    private TableView table;
    @FXML
    private TableColumn id;
    @FXML
    private TableColumn LivreurId;
    @FXML
    private TableColumn UserId;
    @FXML
    private TableColumn Statut;
    @FXML
    private TableColumn PrixTotal;

    @FXML
    private TextField search;



    @FXML
    public void initialize() {
        AfficherEX(); // Appeler la méthode pour afficher les données

        // Ajouter un écouteur sur la table pour détecter les double-clics
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Vérifier si c'est un double-clic
                // Vérifier le type de l'objet sélectionné
                Object selectedItem = table.getSelectionModel().getSelectedItem();
                if (selectedItem instanceof Commande) {
                    Commande Commande = (Commande) selectedItem;
                    try {
                        // Charger le fichier FXML CommandeUpdate.fxml
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Commande/CommandeUpdate.fxml"));
                        Parent root = fxmlLoader.load();

                        // Obtenir le contrôleur associé au fichier FXML chargé
                        CommandeUpdate CommandeUpdate = fxmlLoader.getController();

                        // Remplir les champs du contrôleur avec les données de l'Commande sélectionné
                        CommandeUpdate.setIdA(String.valueOf(Commande.getId()));
                        CommandeUpdate.setLivreurChoiceBoxValue(Commande.getLivreurId());
                        CommandeUpdate.setUserIdA(String.valueOf(Commande.getUserId()));
                        CommandeUpdate.setStatutA(Commande.getStatut());
                        CommandeUpdate.setPrixTotalA(String.valueOf(Commande.getPrixTotal()));

                        // Remplacer la racine de la scène par le nouveau contenu chargé à partir du fichier FXML
                        table.getScene().setRoot(root);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @FXML
    protected void supprimerEX() {

        TableView.TableViewSelectionModel<Commande> selectionModel = table.getSelectionModel();

// Vérifier s'il y a une ligne sélectionnée
        if (!selectionModel.isEmpty()) {
            CommandeService es = new CommandeService();
            // Récupérer l'objet Commande correspoStatutant à la ligne sélectionnée
            Commande selectedCommande = selectionModel.getSelectedItem();
            try {
                es.delete(selectedCommande);
                AfficherEX();
            }catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Aucune ligne sélectionnée.");
        }
    }

    @FXML // affiche nom prenom et marche
    protected void AfficherEX() {
        CommandeService es = new CommandeService();
        try {
            List<Commande> commandes = es.afficherList();

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la liste à la TableView
            for (Commande commande : commandes) {
                table.getItems().add(commande);
            }

            // Associer les colonnes de la TableView aux propriétés correspondantes
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            LivreurId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Commande, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Commande, String> cellData) {
                    Commande commande = cellData.getValue();
                    LivreurService liv = new LivreurService();
                    Livreur livreur = null;
                    try {
                        livreur = liv.getLivreurById(commande.getLivreurId());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return new SimpleStringProperty(livreur.getNom() + " " + livreur.getPrenom());
                }
            });

            UserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
            Statut.setCellValueFactory(new PropertyValueFactory<>("statut"));
            PrixTotal.setCellValueFactory(new PropertyValueFactory<>("prixTotal"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /*@FXML // afficher avec nom et prenom mais sont modifier
    protected void AfficherEX() {
        CommandeService es = new CommandeService();
        try {
            List<Commande> commandes = es.afficherList();

            // Effacer les données existantes de la TableView
            table.getItems().clear();
            LivreurService liv = new LivreurService();
            Livreur liv2 = new Livreur();
            // Ajouter les données de la liste à la TableView
            for (Commande commande : commandes) {
                liv2 = liv.getLivreurById(commande.getLivreurId());

                String id = String.valueOf(commande.getId());
                String livreurId = liv2.getNom() + " " + liv2.getPrenom();
                String userId = String.valueOf(commande.getUserId());
                String statut = commande.getStatut();
                String prixTotal = String.valueOf(commande.getPrixTotal());

                // Créer une nouvelle ligne dans la TableView avec les données de la commande
                ObservableList<String> row = FXCollections.observableArrayList(id, livreurId, userId, statut, prixTotal);
                table.getItems().add(row);
            }

            // Associer les colonnes de la TableView aux propriétés correspondantes
            id.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> param) {
                    return new SimpleStringProperty(param.getValue().get(0));
                }
            });
            LivreurId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> param) {
                    return new SimpleStringProperty(param.getValue().get(1));
                }
            });
            UserId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> param) {
                    return new SimpleStringProperty(param.getValue().get(2));
                }
            });
            Statut.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> param) {
                    return new SimpleStringProperty(param.getValue().get(3));
                }
            });
            PrixTotal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> param) {
                    return new SimpleStringProperty(param.getValue().get(4));
                }
            });

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/

    /*@FXML  // affiche id
    protected void AfficherEX() {
        CommandeService es = new CommandeService();
        try {
            List<Commande> l = es.afficherList();

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la liste à la TableView
            for (Commande Commande : l) {
                table.getItems().add(Commande);
            }

            // Associer les propriétés UserId objets Commande aux colonnes de la TableView
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            LivreurId.setCellValueFactory(new PropertyValueFactory<>("livreurId"));
            UserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
            Statut.setCellValueFactory(new PropertyValueFactory<>("statut"));
            PrixTotal.setCellValueFactory(new PropertyValueFactory<>("prixTotal"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/


    public void AJ(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Commande/CommandeAdd.fxml"));
            primaryStage=(Stage)((Node)event.getSource()).getScene().getWindow();
            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre principale (Stage)
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la nouvelle scène : " + e.getMessage());
        }
    }




    @FXML
    protected void AfficherEXSearch() {
        CommandeService es = new CommandeService();
        LivreurService ls = new LivreurService();
        try {
            List<Commande> l = es.afficherListSearch(search.getText());

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la list   e à la TableView
            for (Commande Commande : l) {
                table.getItems().add(Commande);
            }

            // Associer les propriétés UserId objets Commande aux colonnes de la TableView
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            LivreurId.setCellValueFactory(new PropertyValueFactory<>("LivreurId"));
            UserId.setCellValueFactory(new PropertyValueFactory<>("UserId"));
            Statut.setCellValueFactory(new PropertyValueFactory<>("Statut"));
            PrixTotal.setCellValueFactory(new PropertyValueFactory<>("PrixTotal"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void PDF() {
        CommandeService es = new CommandeService();
        try {
            List<Commande> listeCommandes = es.afficherList();

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth())); // Créer une page en mode horizontal
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16); // Police en gras et taille augmentée pour le titre
                    contentStream.setNonStrokingColor(Color.RED); // Couleur rouge pour le titre

                    contentStream.beginText();
                    contentStream.newLineAtOffset(150, 750); // Décalage horizontal pour le titre
                    contentStream.showText("COMMANDES"); // Titre étendu
                    contentStream.endText();

                    float margin = 20;
                    float yStart = 580;
                    float rowHeight = 50;
                    float tableWidth = 800; // Largeur du tableau augmentée

                    // Dessiner les lignes horizontales du tableau
                    int rows = listeCommandes.size();
                    for (int i = 0; i <= rows+1; i++) { // Change rows to rows + 1 to include the bottom line
                        contentStream.setStrokingColor(Color.BLACK);
                        contentStream.moveTo(margin, yStart - i * rowHeight);
                        contentStream.lineTo(margin + tableWidth, yStart - i * rowHeight);
                        contentStream.stroke();
                    }

                    String[] headers = {"ID", "UserId", "LivreurId", "Statut", "PrixTotal"};
                    float colWidth = tableWidth / headers.length;

                    // Dessiner les lignes verticales du tableau
                    // Dessiner les lignes verticales du tableau
                    for (int i = 0; i <= headers.length; i++) {
                        contentStream.moveTo(margin + i * colWidth, yStart);
                        contentStream.lineTo(margin + i * colWidth, yStart - (rows + 1) * rowHeight); // Adjust rows + 1
                        contentStream.stroke();
                    }


                    // Ajouter les en-têtes
                    for (int i = 0; i < headers.length; i++) {
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12); // Police en gras pour les en-têtes
                        contentStream.setNonStrokingColor(Color.RED); // Couleur bleue pour les en-têtes
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin + i * colWidth + 5, yStart - 15);
                        contentStream.showText(headers[i]);
                        contentStream.endText();
                    }

                    // Ajouter les données
                    for (int i = 0; i < listeCommandes.size(); i++) {
                        Commande ex = listeCommandes.get(i);

                        String[] data = {
                                String.valueOf(ex.getId()),
                                String.valueOf(ex.getUserId()),
                                String.valueOf(ex.getLivreurId()),
                                String.valueOf(ex.getStatut()),
                                String.valueOf(ex.getPrixTotal())

                        };

                        for (int j = 0; j < data.length; j++) {
                            float x = margin + j * colWidth + 5;
                            float y = yStart - (i + 1) * rowHeight - 15;
                            contentStream.setFont(PDType1Font.HELVETICA, 10); // Taille de police réduite pour les données
                            contentStream.setNonStrokingColor(Color.BLACK); // Couleur noire pour les données
                            contentStream.beginText();
                            String texteSansSautDeLigne = data[j].replace("\n", ""); // Supprimer les sauts de ligne
                            if (texteSansSautDeLigne.length() > 30) {
                                // Si la donnée dépasse 30 caractères, diviser le texte en deux lignes
                                contentStream.newLineAtOffset(x, y);
                                contentStream.showText(texteSansSautDeLigne.substring(0, 30));
                                contentStream.newLineAtOffset(0, -12); // Décalage vertical pour la deuxième ligne
                                contentStream.showText(texteSansSautDeLigne.substring(30));
                            } else {
                                contentStream.newLineAtOffset(x, y);
                                contentStream.showText(texteSansSautDeLigne);
                            }
                            contentStream.endText();
                        }
                    }

                    contentStream.close();
                    document.save("TableauCommande.pdf");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void Excel() {

        CommandeService es = new CommandeService();
        try {
            List<Commande> listeCommandes = es.afficherList();

            // Création d'un nouveau classeur Excel
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Commandes");

            // Ajustement de la largeur des colonnes
            sheet.setColumnWidth(0, 20 * 256);
            sheet.setColumnWidth(1, 40 * 256);
            sheet.setColumnWidth(2, 20 * 256);
            sheet.setColumnWidth(3, 20 * 256);
            sheet.setColumnWidth(4, 20 * 256);
            // Création de l'en-tête
            String[] headers = {"ID", "ID USER", "ID Livreur", "Statut", "PrixTotal"};
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Remplissage des données
            for (int i = 0; i < listeCommandes.size(); i++) {
                Commande ex = listeCommandes.get(i);
                XSSFRow dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(ex.getId());
                // Concaténer le nom et la description dans une seule cellule
                dataRow.createCell(1).setCellValue(ex.getUserId());
                dataRow.createCell(2).setCellValue(ex.getLivreurId());
                dataRow.createCell(3).setCellValue(ex.getStatut());
                dataRow.createCell(4).setCellValue(ex.getPrixTotal());
            }

            // Enregistrement du classeur Excel
            try (FileOutputStream outputStream = new FileOutputStream("Commandes.xlsx")) {
                workbook.write(outputStream);
            }

            System.out.println("Fichier Excel généré avec succès !");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }


}

