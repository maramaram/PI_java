package Controller;

import Entities.Exercice;
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
import Entities.SessionManager;
import Entities.User;
import Service.UserService;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;



public class ExerciceFront {


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
    private TableColumn mc;
    @FXML
    private TableColumn nd;
    @FXML
    private TableColumn img;
    @FXML
    private TableColumn gif;

    @FXML
    private TextField search;
    @FXML
    private BarChart<String, Integer> barChartStats;
    @FXML
    public void initialize() {


            AfficherEX(); // Appeler la méthode pour afficher les données

            // Ajouter un écouteur sur la table pour détecter les double-clics
            table.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) { // Vérifier si c'est un double-clic
                    // Convertir l'objet sélectionné en Exercice en utilisant le casting
                    Exercice exercice = (Exercice) table.getSelectionModel().getSelectedItem();
                    if (exercice != null) {
                        try {
                            // Charger le fichier FXML ExerciceUpdate.fxml
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Exercice/ExerciceUpdate.fxml"));
                            Parent root = fxmlLoader.load();

                            // Obtenir le contrôleur associé au fichier FXML chargé
                            ExerciceUpdate exerciceUpdate = fxmlLoader.getController();

                            // Remplir les champs du contrôleur avec les données de l'exercice sélectionné
                            exerciceUpdate.setIdM(String.valueOf(exercice.getId()));
                            exerciceUpdate.setNomM(exercice.getNom());
                            exerciceUpdate.setDesM(exercice.getDes());
                            exerciceUpdate.setMcM(exercice.getMc());
                            exerciceUpdate.setNdM(exercice.getNd());
                            exerciceUpdate.setImgM(exercice.getImg());
                            exerciceUpdate.setGifM(exercice.getGif());

                            // Remplacer la racine de la scène par le nouveau contenu chargé à partir du fichier FXML
                            table.getScene().setRoot(root);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }

        public void stats()
        {
            barChartStats.getData().clear();
            ExerciceService es = new ExerciceService();
            try {
                List<Exercice> listeExercices = es.afficherList();

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

                for (Exercice exercice : listeExercices) {
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
        }


    @FXML
    protected void supprimerEX() {

        TableView.TableViewSelectionModel<Exercice> selectionModel = table.getSelectionModel();

// Vérifier s'il y a une ligne sélectionnée
        if (!selectionModel.isEmpty()) {
            ExerciceService es = new ExerciceService();
            // Récupérer l'objet Exercice correspondant à la ligne sélectionnée
            Exercice selectedExercice = selectionModel.getSelectedItem();
            try {
            es.delete(selectedExercice);
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
        ExerciceService es = new ExerciceService();
        try {
            List<Exercice> l = es.afficherList();

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la liste à la TableView
            for (Exercice exercice : l) {
                table.getItems().add(exercice);
            }

            // Associer les propriétés des objets Exercice aux colonnes de la TableView
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            des.setCellValueFactory(new PropertyValueFactory<>("des"));
            mc.setCellValueFactory(new PropertyValueFactory<>("mc"));
            nd.setCellValueFactory(new PropertyValueFactory<>("nd"));
            img.setCellValueFactory(new PropertyValueFactory<>("image"));
            gif.setCellValueFactory(new PropertyValueFactory<>("giif"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        stats();
    }


    public void AJ(ActionEvent event) {
        try {
            // Charger le fichier FXML de la nouvelle scène

            Parent root = FXMLLoader.load(getClass().getResource("/Exercice/ExerciceAdd.fxml"));
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
        ExerciceService es = new ExerciceService();
        try {
            List<Exercice> l = es.afficherListSearch(search.getText());

            // Effacer les données existantes de la TableView
            table.getItems().clear();

            // Ajouter les données de la liste à la TableView
            for (Exercice exercice : l) {
                table.getItems().add(exercice);
            }

            // Associer les propriétés des objets Exercice aux colonnes de la TableView
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            des.setCellValueFactory(new PropertyValueFactory<>("des"));
            mc.setCellValueFactory(new PropertyValueFactory<>("mc"));
            nd.setCellValueFactory(new PropertyValueFactory<>("nd"));
            img.setCellValueFactory(new PropertyValueFactory<>("image"));
            gif.setCellValueFactory(new PropertyValueFactory<>("giif"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        stats();
    }


    @FXML
    public void PDF() {
        ExerciceService es = new ExerciceService();
        try {
            List<Exercice> listeExercices = es.afficherList();

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
                    int rows = listeExercices.size();
                    for (int i = 0; i < rows; i++) {
                        contentStream.setStrokingColor(Color.BLACK); // Couleur noire pour les lignes horizontales
                        contentStream.moveTo(margin, yStart - i * rowHeight);
                        contentStream.lineTo(margin + tableWidth, yStart - i * rowHeight);
                        contentStream.stroke();
                    }

                    String[] headers = {"ID", "Nom", "Description", "Muscle Cible", "Niveau de difficulté"};
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
                    for (int i = 0; i < listeExercices.size(); i++) {
                        Exercice ex = listeExercices.get(i);
                        String description = ex.getDes(); // Récupérer la description de l'exercice
                        if (description.length() > 50) {
                            description = description.substring(0, 60) + "..."; // Limiter à 50 caractères et ajouter "..." si la description est plus longue
                        }
                        String[] data = {
                                String.valueOf(ex.getId()),
                                ex.getNom(),
                                description,
                                ex.getMc(),
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
                    document.save("TableauExercices.pdf");
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

        ExerciceService es = new ExerciceService();
        try {
            List<Exercice> listeExercices = es.afficherList();

            // Création d'un nouveau classeur Excel
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Exercices");

            // Ajustement de la largeur des colonnes
            sheet.setColumnWidth(0, 20 * 256); // Largeur de la colonne 1 (ID) en unités de 1/256 de caractère
            sheet.setColumnWidth(1, 40 * 256); // Largeur de la colonne 2 (Nom et Description)
            sheet.setColumnWidth(2, 20 * 256); // Largeur de la colonne 3 (Muscle Cible)
            sheet.setColumnWidth(3, 20 * 256); // Largeur de la colonne 4 (Niveau de difficulté)

            // Création de l'en-tête
            String[] headers = {"ID", "Nom et Description", "Muscle Cible", "Niveau de difficulté"};
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Remplissage des données
            for (int i = 0; i < listeExercices.size(); i++) {
                Exercice ex = listeExercices.get(i);
                XSSFRow dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(ex.getId());
                // Concaténer le nom et la description dans une seule cellule
                String nomEtDescription = ex.getNom() + "\n" + ex.getDes(); // Utilisez "\n" pour ajouter un saut de ligne entre le nom et la description
                dataRow.createCell(1).setCellValue(nomEtDescription);
                dataRow.createCell(2).setCellValue(ex.getMc());
                dataRow.createCell(3).setCellValue(ex.getNd());
            }

            // Enregistrement du classeur Excel
            try (FileOutputStream outputStream = new FileOutputStream("Exercices.xlsx")) {
                workbook.write(outputStream);
            }

            System.out.println("Fichier Excel généré avec succès !");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }


}

