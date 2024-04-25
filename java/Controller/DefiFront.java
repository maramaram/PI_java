package Controller;

import Entities.Defi;
import Entities.Exercice;
import Service.DefiService;
import Service.ExerciceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class DefiFront {


    private Stage primaryStage;



    @FXML
    private TableView table;
    @FXML
    private TableColumn id;
    @FXML
    private TableColumn nom;
    @FXML
    private TableColumn des;
    @FXML
    private TableColumn nd;
    @FXML
    private TableColumn nbj;
    @FXML
    private TableColumn ex;

    @FXML
    private TextField search;

    @FXML
    private BarChart<String, Integer> barChartStats;



    @FXML
    public void initialize() {



        DefiService es = new DefiService();
        try {
            List<Defi> listeDefis = es.afficherList();

            XYChart.Series<String, Integer> niveau1 = new XYChart.Series<>();
            niveau1.setName("Niveau 1");

            XYChart.Series<String, Integer> niveau2 = new XYChart.Series<>();
            niveau2.setName("Niveau 2");

            XYChart.Series<String, Integer> niveau3 = new XYChart.Series<>();
            niveau3.setName("Niveau 3");

            // Parcourir la liste d'exercices et compter le nombre d'exercices pour chaque niveau de difficulté
            int countNiveau1 = 0;
            int countNiveau2 = 0;
            int countNiveau3 = 0;

            for (Defi exercice : listeDefis) {
                String niveau = exercice.getNd();
                if (niveau.equals("1")) {
                    countNiveau1++;
                } else if (niveau.equals("2")) {
                    countNiveau2++;
                } else if (niveau.equals("3")) {
                    countNiveau3++;
                }
            }

            // Ajouter les données à chaque série
            niveau1.getData().add(new XYChart.Data<>("Niveau 1", countNiveau1));
            niveau2.getData().add(new XYChart.Data<>("Niveau 2", countNiveau2));
            niveau3.getData().add(new XYChart.Data<>("Niveau 3", countNiveau3));

            // Ajouter les séries au BarChart
            barChartStats.getData().addAll(niveau1, niveau2, niveau3);



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }



        AfficherEX(); // Appeler la méthode pour afficher les données

        // Ajouter un écouteur sur la table pour détecter les double-clics
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Vérifier si c'est un double-clic
                // Vérifier le type de l'objet sélectionné
                Object selectedItem = table.getSelectionModel().getSelectedItem();
                if (selectedItem instanceof Defi) {
                    Defi defi = (Defi) selectedItem;
                    try {
                        // Charger le fichier FXML DefiUpdate.fxml
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Defi/DefiUpdate.fxml"));
                        Parent root = fxmlLoader.load();

                        // Obtenir le contrôleur associé au fichier FXML chargé
                        DefiUpdate defiUpdate = fxmlLoader.getController();

                        // Remplir les champs du contrôleur avec les données de l'defi sélectionné
                        defiUpdate.setIdM(String.valueOf(defi.getId()));
                        defiUpdate.setNomM(defi.getNom());
                        defiUpdate.setDesM(defi.getDes());
                        defiUpdate.setNdM(defi.getNd());
                        defiUpdate.setNbjM(String.valueOf(defi.getNbj()));
                        defiUpdate.setEx(defi.getLex());

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

        TableView.TableViewSelectionModel<Defi> selectionModel = table.getSelectionModel();

// Vérifier s'il y a une ligne sélectionnée
        if (!selectionModel.isEmpty()) {
            DefiService es = new DefiService();
            // Récupérer l'objet Defi correspondant à la ligne sélectionnée
            Defi selectedDefi = selectionModel.getSelectedItem();
            try {
            es.delete(selectedDefi);
            AfficherEX();
            }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        } else {
            System.out.println("Aucune ligne sélectionnée.");
        }
    }

    @FXML
    protected void AfficherEX() {
        DefiService es = new DefiService();
        try {
            List<Defi> l = es.afficherList();

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la liste à la TableView
            for (Defi defi : l) {
                table.getItems().add(defi);
            }

            // Associer les propriétés des objets Defi aux colonnes de la TableView
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            des.setCellValueFactory(new PropertyValueFactory<>("des"));
            nd.setCellValueFactory(new PropertyValueFactory<>("nd"));
            nbj.setCellValueFactory(new PropertyValueFactory<>("nbj"));
            ex.setCellValueFactory(new PropertyValueFactory<>("nomx"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void AJ(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Defi/DefiAdd.fxml"));
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
        DefiService es = new DefiService();
        try {
            List<Defi> l = es.afficherListSearch(search.getText());

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la liste à la TableView
            for (Defi defi : l) {
                table.getItems().add(defi);
            }

            // Associer les propriétés des objets Defi aux colonnes de la TableView
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            des.setCellValueFactory(new PropertyValueFactory<>("des"));
            nd.setCellValueFactory(new PropertyValueFactory<>("nd"));
            nbj.setCellValueFactory(new PropertyValueFactory<>("nbj"));
            ex.setCellValueFactory(new PropertyValueFactory<>("nomx"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }










    @FXML
    public void PDF() {
        DefiService es = new DefiService();
        try {
            List<Defi> listeDefis = es.afficherList();

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth())); // Créer une page en mode horizontal
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16); // Police en gras et taille augmentée pour le titre
                    contentStream.setNonStrokingColor(Color.RED); // Couleur rouge pour le titre

                    contentStream.beginText();
                    contentStream.newLineAtOffset(150, 750); // Décalage horizontal pour le titre
                    contentStream.showText("EXERCICES"); // Titre étendu
                    contentStream.endText();

                    float margin = 20;
                    float yStart = 580;
                    float rowHeight = 50;
                    float tableWidth = 800; // Largeur du tableau augmentée

                    // Dessiner les lignes horizontales du tableau
                    int rows = listeDefis.size();
                    for (int i = 0; i < rows; i++) {
                        contentStream.setStrokingColor(Color.BLACK); // Couleur noire pour les lignes horizontales
                        contentStream.moveTo(margin, yStart - i * rowHeight);
                        contentStream.lineTo(margin + tableWidth, yStart - i * rowHeight);
                        contentStream.stroke();
                    }

                    String[] headers = {"ID", "Nom", "Description", "Nombre de jours", "Niveau de difficulté"};
                    float colWidth = tableWidth / headers.length;

                    // Dessiner les lignes verticales du tableau
                    for (int i = 0; i < headers.length; i++) {
                        contentStream.moveTo(margin + i * colWidth, yStart);
                        contentStream.lineTo(margin + i * colWidth, yStart - rows * rowHeight);
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
                    for (int i = 0; i < listeDefis.size(); i++) {
                        Defi ex = listeDefis.get(i);
                        String description = ex.getDes(); // Récupérer la description de l'exercice
                        if (description.length() > 20) {
                            description = description.substring(0, 20) + "..."; // Limiter à 50 caractères et ajouter "..." si la description est plus longue
                        }
                        String[] data = {
                                String.valueOf(ex.getId()),
                                ex.getNom(),
                                description,
                                String.valueOf(ex.getNbj()),
                                ex.getNd()
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
                    document.save("TableauDefi.pdf");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




    @FXML
    public void EXCEL() {

        DefiService es = new DefiService();
        try {
            List<Defi> listeDefis = es.afficherList();

            // Création d'un nouveau classeur Excel
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Defis");

            // Ajustement de la largeur des colonnes
            sheet.setColumnWidth(0, 20 * 256); // Largeur de la colonne 1 (ID) en unités de 1/256 de caractère
            sheet.setColumnWidth(1, 40 * 256); // Largeur de la colonne 2 (Nom et Description)
            sheet.setColumnWidth(2, 20 * 256); // Largeur de la colonne 3 (Muscle Cible)
            sheet.setColumnWidth(3, 20 * 256); // Largeur de la colonne 4 (Niveau de difficulté)

            // Création de l'en-tête
            String[] headers = {"ID", "Nom et Description", "Nombre de jours", "Niveau de difficulté"};
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Remplissage des données
            for (int i = 0; i < listeDefis.size(); i++) {
                Defi ex = listeDefis.get(i);
                XSSFRow dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(ex.getId());
                // Concaténer le nom et la description dans une seule cellule
                String nomEtDescription = ex.getNom() + " \n" + ex.getDes(); // Utilisez "\n" pour ajouter un saut de ligne entre le nom et la description
                dataRow.createCell(1).setCellValue(nomEtDescription);
                dataRow.createCell(2).setCellValue(ex.getNbj());
                dataRow.createCell(3).setCellValue(ex.getNd());
            }

            // Enregistrement du classeur Excel
            try (FileOutputStream outputStream = new FileOutputStream("Defis.xlsx")) {
                workbook.write(outputStream);
            }

            System.out.println("Fichier Excel généré avec succès !");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }





    public void Exercices(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Exercice/Exercice-front.fxml"));
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

    public void Defis(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Defi/Defi-front.fxml"));
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


}

