package Controller;

import Entities.Commande;
import Entities.Livreur;
import Entities.User;
import Service.CommandeService;
import Service.LivreurService;
import Service.UserService;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javafx.util.Callback;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Color;
import javafx.scene.chart.PieChart;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;
import com.google.zxing.client.j2se.MatrixToImageConfig;


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
    private PieChart piechart;
    @FXML
    private HBox hbox;
    @FXML
    private Pagination pagination;
    @FXML
    private Label currentPageLabel;
    private CommandeService commandeService = new CommandeService();
    private LivreurService livreurService = new LivreurService();

    private ObservableList<Commande> commandes = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        currentPageLabel = new Label();
        // Ajouter un écouteur d'événement sur la pagination pour appeler handlePageChange() lorsque la page change
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            handlePageChange();
        });
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
                        CommandeUpdate.setUserChoiceBoxValue(Commande.getLivreurId());
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
        CommandeService es = new CommandeService();
        try {
            List<Commande> listeCommandes = es.afficherList();



            // Parcourir la liste d'Commandes et compter le nombre d'Commandes pour chaque niveau de difficulté
            int countNiveau1 = 0;
            int countNiveau2 = 0;
            int countNiveau3 = 0;

            for (Commande commande : listeCommandes) {
                String niveau = commande.getStatut();
                if (niveau.equals("en attente")) {
                    countNiveau1++;
                } else if (niveau.equals("arrivée")) {
                    countNiveau2++;
                } else if (niveau.equals("en route")) {
                    countNiveau3++;
                }
            }

            // Ajouter les données au PieChart
            PieChart.Data dataNiveau1 = new PieChart.Data("en attente (" + countNiveau1 + ")", countNiveau1);
            PieChart.Data dataNiveau2 = new PieChart.Data("arrivée (" + countNiveau2 + ")", countNiveau2);
            PieChart.Data dataNiveau3 = new PieChart.Data("en route (" + countNiveau3 + ")", countNiveau3);
            piechart.getData().addAll(dataNiveau1, dataNiveau2, dataNiveau3);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

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
        try {
            commandes.clear();
            commandes.addAll(commandeService.afficherList());

            int totalPages = (int) Math.ceil(commandes.size() / 5.0);
            pagination.setPageCount(totalPages);
            pagination.setCurrentPageIndex(0);

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
            UserId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Commande, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Commande, String> cellData) {
                    Commande commande = cellData.getValue();
                    UserService userService = new UserService(); // Assuming this is your user service
                    User user = null;
                    try {
                        user = userService.getUserById(commande.getUserId()); // Retrieve user by userId
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return new SimpleStringProperty(user.getNom() + " " + user.getPrenom());
                }
            });
            Statut.setCellValueFactory(new PropertyValueFactory<>("statut"));
            PrixTotal.setCellValueFactory(new PropertyValueFactory<>("prixTotal"));

            table.setItems(getCommandesForCurrentPage());
            currentPageLabel.setText("Page " + (pagination.getCurrentPageIndex() + 1) + " / " + pagination.getPageCount());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void handlePageChange() {
        table.setItems(getCommandesForCurrentPage());
        currentPageLabel.setText("Page " + (pagination.getCurrentPageIndex() + 1) + " / " + pagination.getPageCount());
    }

    private ObservableList<Commande> getCommandesForCurrentPage() {
        int fromIndex = pagination.getCurrentPageIndex() * 5;
        int toIndex = Math.min(fromIndex + 5, commandes.size());
        return FXCollections.observableArrayList(commandes.subList(fromIndex, toIndex));
    }

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
        try {
            commandes.clear();
            commandes.addAll(commandeService.afficherListSearch(search.getText()));

            int totalPages = (int) Math.ceil(commandes.size() / 5.0);
            pagination.setPageCount(totalPages);
            pagination.setCurrentPageIndex(0);

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

            table.setItems(getCommandesForCurrentPage());
            currentPageLabel.setText("Page " + (pagination.getCurrentPageIndex() + 1) + " / " + pagination.getPageCount());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void PDF() throws IOException {
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
        // Extraire le texte du PDF
        String text = extractTextFromPDF("TableauCommande.pdf");

        // Générer le QR code à partir du texte du PDF
        int width = 300;
        int height = 300;
        BufferedImage qrImage = generateQRCode(text, width, height);

        // Enregistrer le QR code dans un fichier image
        File outputFile = new File("qrcode.png");
        ImageIO.write(qrImage, "png", outputFile);
    }

    private String extractTextFromPDF(String filePath) {
        String text = "";
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            text = stripper.getText(document);
        } catch (IOException e) {
            System.out.println("Une erreur s'est produite lors de l'extraction du texte du PDF : " + e.getMessage());
        }
        return text;
    }

    private BufferedImage generateQRCode(String text, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            return qrImage;
        } catch (WriterException e) {
            System.out.println("Une erreur s'est produite lors de la génération du code QR : " + e.getMessage());
            return null;
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

