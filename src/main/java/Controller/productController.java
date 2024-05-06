package Controller;

import Entities.product;
import Service.productService;
import com.example.projectpi.HelloApplication;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.fxml.FXML;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
public class productController extends Application {



    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/products/baseback.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    private final ObjectProperty<product> selectedProductProperty = new SimpleObjectProperty<>();
    private boolean isAscending = true;

    @FXML
    private TableColumn<?, ?> col_categorie;

    @FXML
    private TableColumn<?, ?> col_description;

    @FXML
    private TableColumn<?, ?> col_id;

    @FXML
    private TableColumn<?, ?> col_image;

    @FXML
    private TableColumn<?, ?> col_nom;

    @FXML
    private TableColumn<?, ?> col_prix;

    @FXML
    private TableColumn<?, ?> col_quantite;

    @FXML
    private TableView<product> table_produits;

    @FXML
    private TextField txt_categorie;

    @FXML
    private TextField txt_description;

    @FXML
    private TextField txt_nom;

    @FXML
    private TextField txt_prix;

    @FXML
    private TextField txt_quantite;
    @FXML
    private TextField txt_image;
    @FXML
    private TextField search;
    @FXML
    private ChoiceBox<String> choiceBoxSort;
    @FXML
    private Button btnGeneratePDF;



    @FXML
    private void handleGeneratePDFButton(ActionEvent event) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            PDImageXObject logo = PDImageXObject.createFromFile("src/main/resources/logo.png", document);
            contentStream.drawImage(logo, 20, page.getMediaBox().getHeight() - 70, 70, 50);

            // Déterminez les dimensions et les marges du tableau
            float margin = 60;
            float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
            float nexty = page.getMediaBox().getHeight() - margin;
            float rowHeight = 70;
            float[] columnWidths = {50, 100, 150, 70, 100, 100}; // Largeurs de colonne pour chaque colonne
            String[] columnTitles = {"ID", "Nom", "Description", "Quantité", "Catégorie", "Prix"}; // Titres de colonne

            // Dessinez le tableau avec les données du TableView
            drawTable(contentStream, nexty, margin, tableWidth, rowHeight, columnWidths, columnTitles);

            contentStream.close();
            document.save("tableau_produits.pdf");
            document.close();

            // Affichez un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Le fichier PDF a été généré avec succès !");
            alert.showAndWait();
        } catch (IOException e) {
            // Gérez les exceptions d'entrée/sortie
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Une erreur s'est produite lors de la génération du fichier PDF !");
            alert.showAndWait();
        }
    }



    private void drawTable(PDPageContentStream contentStream, float nexty, float margin, float tableWidth, float rowHeight, float[] columnWidths, String[] columnTitles) throws IOException {
        final int numberOfColumns = columnTitles.length;
        final int numberOfRows = table_produits.getItems().size() + 1;



        // Ajout de l'en-tête avec le titre du document
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
        contentStream.newLineAtOffset(margin + 100, nexty + 20);
        contentStream.showText("Liste des Produits");
        contentStream.endText();

        // Ajout du pied de page avec la date et l'heure de génération
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(margin, 40);
        contentStream.showText("Généré le " + java.time.LocalDate.now() + " à " + java.time.LocalTime.now());
        contentStream.endText();

        // Dessin des lignes horizontales et verticales
        float nexty1 = nexty;
        contentStream.drawLine(margin, nexty1, margin + tableWidth, nexty1);
        for (int i = 0; i <= numberOfRows; i++) {
            contentStream.drawLine(margin, nexty1 - rowHeight, margin + tableWidth, nexty1 - rowHeight);
            nexty1 -= rowHeight;
        }

        float nextx = margin;
        contentStream.drawLine(nextx, nexty, nextx, nexty - numberOfRows * rowHeight);
        for (int i = 0; i < numberOfColumns; i++) {
            nextx += columnWidths[i];
            contentStream.drawLine(nextx, nexty, nextx, nexty - numberOfRows * rowHeight);
        }

        // Remplissage des cellules avec les données
        float textx = margin;
        float texty = nexty - 15;
        for (int i = 0; i < numberOfColumns; i++) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(textx, texty);
            contentStream.showText(columnTitles[i]);
            contentStream.endText();
            textx += columnWidths[i];
        }

        ObservableList<product> productList = table_produits.getItems();
        for (product item : productList) {
            texty -= rowHeight;
            textx = margin;
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(textx, texty);
            contentStream.showText(String.valueOf(item.getId()));
            contentStream.newLineAtOffset(columnWidths[0], 0);
            contentStream.showText(item.getNom());
            contentStream.newLineAtOffset(columnWidths[1], 0);
            contentStream.showText(item.getDescription());
            contentStream.newLineAtOffset(columnWidths[2], 0);
            contentStream.showText(String.valueOf(item.getQuantite_stock()));
            contentStream.newLineAtOffset(columnWidths[3], 0);
            contentStream.showText(item.getCategories());
            contentStream.newLineAtOffset(columnWidths[4], 0);
            contentStream.showText(String.valueOf(item.getPrix()));
            contentStream.endText();
        }
    }


    @FXML
    void ajouterproduit(ActionEvent event) throws SQLException {
        if (validateFields()) {
            productService pService = new productService(); // Créez une instance de productService
            product pr = new product(txt_nom.getText(), txt_description.getText(), Integer.parseInt(txt_quantite.getText()), txt_categorie.getText(), Float.parseFloat(txt_prix.getText()), txt_image.getText());
            try {
                pService.ajouterproduit(pr); // Appelez la méthode sur l'instance créée
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Le produit a été ajouté avec succès !");
                alert.showAndWait();
                initTableView();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Échec");
                alert.setContentText("Échec de l'ajout du produit !");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void deplacerverscart(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/paniers/basebackp.fxml"));
            Parent root = loader.load();

            // Obtenez le Stage actuel
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Créez une nouvelle scène avec la racine chargée
            Scene scene = new Scene(root);

            // Remplacez la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateFields() {
        // Validation pour le champ Nom
        if (txt_nom.getText().isEmpty()) {
            showAlert("Erreur", "Le champ Nom ne doit pas être vide !");
            return false;
        }

        // Validation pour le champ Description
        if (txt_description.getText().isEmpty()) {
            showAlert("Erreur", "Le champ Description ne doit pas être vide !");
            return false;
        }

        // Validation pour le champ Quantité
        if (txt_quantite.getText().isEmpty() || Integer.parseInt(txt_quantite.getText()) <= 0) {
            showAlert("Erreur", "La Quantité doit être un entier positif !");
            return false;
        }

        // Validation pour le champ Catégorie


        // Validation pour le champ Prix
        if (txt_prix.getText().isEmpty() || Float.parseFloat(txt_prix.getText()) <= 0) {
            showAlert("Erreur", "Le Prix doit être un nombre positif !");
            return false;
        }
       /* if (txt_image.getText().isEmpty() || !validateImageExtension(txt_image.getText())) {
            showAlert("Erreur", "L'extension de l'image n'est pas valide ! Les extensions autorisées sont : jpg, jpeg, png, gif");
            return false;
        }*/
        // Validation pour le champ Image (ajoutez votre propre validation ici)

        return true;
    }
    private boolean validateImageExtension(String imageName) {
        String[] allowedExtensions = {"jpg", "jpeg", "png", "gif"};
        String extension = imageName.substring(imageName.lastIndexOf(".") + 1).toLowerCase();

        for (String ext : allowedExtensions) {
            if (ext.equals(extension)) {
                return true;
            }
        }

        return false;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public  void initialize() {
        // Initialisez le TableView avec les données des produits
        initTableView();
        setupImageColumn();
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTable(newValue);
        });
        // Ajoutez un gestionnaire d'événements de sélection sur le TableView
        table_produits.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Récupérez le produit sélectionné
                product selectedProduct = table_produits.getSelectionModel().getSelectedItem();

                // Affichez les données du produit sélectionné dans les champs de texte correspondants
                txt_nom.setText(selectedProduct.getNom());
                txt_description.setText(selectedProduct.getDescription());
                txt_quantite.setText(String.valueOf(selectedProduct.getQuantite_stock()));
                txt_categorie.setText(selectedProduct.getCategories());
                txt_prix.setText(String.valueOf(selectedProduct.getPrix()));
                txt_image.setText(selectedProduct.getImage());
            }
        });
        // Contrôle de saisie pour le champ Quantité
        txt_quantite.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Vérifie que la saisie est un entier
                txt_quantite.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Contrôle de saisie pour le champ Prix
        txt_prix.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) { // Vérifie que la saisie est un nombre
                txt_prix.setText(newValue.replaceAll("[^\\d\\.]", ""));
            }
        });
        ObservableList<String> sortOptions = FXCollections.observableArrayList("Quantite", "Nom", "Prix");
        choiceBoxSort.setItems(sortOptions);

        // Ajoutez les options de tri au ChoiceBox
        choiceBoxSort.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "Quantite":
                    // Tri par ID
                    sortBy("quantite_stock");
                    break;
                case "Nom":
                    // Tri par Nom
                    sortBy("nom");
                    break;
                case "Prix":
                    // Tri par Prix
                    sortBy("prix");
                    break;
                default:
                    // Par défaut, ne pas trier
                    // Vous pouvez ajouter une logique de tri par défaut ici si nécessaire
            }
        });
    }
    private void sortBy(String columnName) {
        // Obtenez la colonne correspondant au nom de la colonne
        TableColumn<product, ?> column = getColumnByName(columnName);

        // Triez les éléments de la colonne en ordre ascendant
        table_produits.getSortOrder().clear();
        column.setSortType(TableColumn.SortType.ASCENDING);
        table_produits.getSortOrder().add(column);
    }

    // Méthode pour obtenir une colonne par son nom
    private TableColumn<product, ?> getColumnByName(String name) {
        for (TableColumn<product, ?> column : table_produits.getColumns()) {
            if (column.getText().equalsIgnoreCase(name)) {
                return column;
            }
        }
        return null;
    }


    private void initTableView() {
        try {
            productService pService = new productService(); // Créez une instance de productService
            List<product> productList = pService.afficherList(); // Récupérez la liste des produits depuis la base de données
            ObservableList<product> observableProductList = FXCollections.observableArrayList(productList); // Créez une liste observable à partir de la liste de produits

            // Associez chaque colonne à une propriété du modèle de données
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            col_quantite.setCellValueFactory(new PropertyValueFactory<>("quantite_stock"));
            col_categorie.setCellValueFactory(new PropertyValueFactory<>("categories"));
            col_prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
            col_image.setCellValueFactory(new PropertyValueFactory<>("img"));

            // Remplissez le TableView avec les données des produits
            table_produits.setItems(observableProductList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez l'exception
        }
    }



    @FXML
    void deleteproduit(ActionEvent event) throws SQLException {
        product selectedProduct = table_produits.getSelectionModel().getSelectedItem(); // Obtenez le produit sélectionné dans le TableView
        if (selectedProduct != null) {
            try {
                productService pService = new productService(); // Créez une instance de productService
                pService.deleteproduit(selectedProduct.getId()); // Appelez la méthode deleteproduit avec l'ID du produit sélectionné
                // Supprimez le produit de la liste observable
                table_produits.getItems().remove(selectedProduct);
                // Affichez un message de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Le produit a été supprimé avec succès !");
                alert.showAndWait();
            } catch (SQLException e) {
                // Gérez les erreurs de suppression
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Une erreur s'est produite lors de la suppression du produit !");
                alert.showAndWait();
            }
        } else {
            // Affichez un message d'erreur si aucun produit n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez sélectionner un produit à supprimer !");
            alert.showAndWait();
        }
    }

    @FXML
    void updateProduit(ActionEvent event) throws SQLException {
        productService pService = new productService();
        product selectedProduct = table_produits.getSelectionModel().getSelectedItem();
        if (selectedProduct != null && validateFields()) {
            try {
                selectedProduct.setNom(txt_nom.getText());
                selectedProduct.setDescription(txt_description.getText());
                selectedProduct.setQuantite_stock(Integer.parseInt(txt_quantite.getText()));
                selectedProduct.setCategories(txt_categorie.getText());
                selectedProduct.setPrix(Float.parseFloat(txt_prix.getText()));

                // Mettez à jour l'image seulement si un nouveau chemin d'image est spécifié
                if (!txt_image.getText().isEmpty()) {
                    selectedProduct.setImage(txt_image.getText());
                }

                pService.updateProduit(selectedProduct.getId(), selectedProduct);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Le produit a été mis à jour avec succès !");
                alert.showAndWait();
                initTableView();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Une erreur s'est produite lors de la mise à jour du produit !");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez vérifier les champs !");
            alert.showAndWait();
        }
    }


    @FXML
    private void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Créez un ImageView avec l'image sélectionnée
                ImageView imageView = new ImageView(new Image(selectedFile.toURI().toString()));
                imageView.setFitHeight(125);
                imageView.setFitWidth(125);

                // Créez un nouveau produit avec l'image sélectionnée
                product newProduct = new product(txt_nom.getText(), txt_description.getText(),
                        Integer.parseInt(txt_quantite.getText()), txt_categorie.getText(),
                        Float.parseFloat(txt_prix.getText()), selectedFile.getAbsolutePath());

                // Définissez l'image de votre produit sur l'ImageView créé
                newProduct.setImg(imageView);

                // Ajoutez le nouveau produit à la liste observable
                ObservableList<product> productList = table_produits.getItems();
                productList.add(newProduct);

                // Effacez le chemin de l'image dans le champ texte (si nécessaire)
                txt_image.clear();
            } catch (Exception e) {
                e.printStackTrace();
                // Gérez les erreurs si nécessaire
            }
        }
    }
    private void setupImageColumn() {
        // Créez une colonne personnalisée pour afficher les images
        TableColumn<product, ImageView> imageColumn = new TableColumn<>("Image");
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("img"));

        // Définissez la largeur de la colonne
        imageColumn.setPrefWidth(150);

        // Ajoutez la colonne au TableView
        table_produits.getColumns().add(imageColumn);
    }


    private void filterTable(String keyword) {
        ObservableList<product> filteredList = FXCollections.observableArrayList();
        if (keyword.isEmpty()) {
            initTableView();
            return;
        }
        for (product item : table_produits.getItems()) {
            // Vérifiez si le nom, la description ou le prix contient le mot-clé de recherche
            if (item.getNom().toLowerCase().contains(keyword.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                    String.valueOf(item.getPrix()).toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(item);
            }
        }

        // Mettez à jour le TableView avec la liste filtrée
        table_produits.setItems(filteredList);
    }

    public ObjectProperty<product> selectedProductProperty() {
        return selectedProductProperty;
    }

    public product getSelectedProduct() {
        return selectedProductProperty.get();
    }

    public void setSelectedProduct(product product) {
        selectedProductProperty.set(product);
    }
}