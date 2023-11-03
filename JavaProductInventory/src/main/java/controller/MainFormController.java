package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller for the main form.
 * @author Kurtis
 */
public class MainFormController extends BaseController implements Initializable {

    /** The part id column */
    @FXML
    private TableColumn<Part, Integer> partIdCol;
    /** The part stock/inventory column */
    @FXML
    private TableColumn<Part, Integer>  partInventoryCol;
    /** The part name column */
    @FXML
    private TableColumn<Part, String> partNameCol;
    /** The part price column */
    @FXML
    private TableColumn<Part, Double> partPriceCol;
    /** The part searchbar */
    @FXML
    private TextField partSearch;
    /** The parts table */
    @FXML
    private TableView<Part> partsTableView;
    /** The product id column */
    @FXML
    private TableColumn<Product, Integer> productIdCol;
    /** The product stock/inventory column */
    @FXML
    private TableColumn<Product, Integer> productInventoryCol;
    /** The product name column */
    @FXML
    private TableColumn<Product, String> productNameCol;
    /** The product price column */
    @FXML
    private TableColumn<Product, Double> productPriceCol;
    /** The product searchbar */
    @FXML
    private TextField productSearch;
    /** The product table */
    @FXML
    private TableView<Product> productsTableView;
    /** The part not found warning label */
    @FXML
    private Label partNotFoundLbl;
    /** The product not found warning label */
    @FXML
    private Label productNotFoundLbl;


    /**
     * Loads the part form in add mode.
     * @param event action event from the button press
     * @throws IOException if the fxml file is not found
     */
    @FXML
    void onActionAddPart(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/PartForm.fxml"));
        loader.load();

        PartFormController partCtrl = loader.getController();
        partCtrl.enableAddMode();

        loadViewFromButtonPress(event, loader);

    }

    /**
     * Loads the product form in add mode.
     * @param event action event from the button press
     * @throws IOException if the fxml file is not found
     */
    @FXML
    void onActionAddProduct(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ProductForm.fxml"));
        loader.load();

        ProductFormController productCtrl = loader.getController();
        productCtrl.enableAddMode();

        loadViewFromButtonPress(event, loader);

    }

    /**
     * Deletes the selected part in the parts table view.
     * This method will create a dialog box if the button is pressed while nothing is selected.
     * <p>
     *     RUNTIME ERROR a NullPointerException is thrown if nothing is selected and delete is pressed.
     *     This was fixed by catching the exception and displaying a dialog box.
     * </p>
     * @param event action event from the button press
     */
    @FXML
    void onActionDeletePart(ActionEvent event) {

        try {
            Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();

            if (alertDeleteConfirmation("Delete", selectedPart.getName())) {

                // This line of code causes an exception if selectedPart is null
                if (!Inventory.deletePart(selectedPart)) {
                    alertDeleteFailed(selectedPart.getName());
                }
            }
        }
        // This catch block catches the exception and displays a dialog box to the user
        catch (NullPointerException e) {
            alertUnselectedItem();
        }

    }

    /**
     * Deletes the selected product in the parts table view.
     * This method will create a dialog box if the button is pressed while nothing is selected,
     * or if the product has remaining associated parts.
     * @param event action event from the button press
     */
    @FXML
    void onActionDeleteProduct(ActionEvent event) {

        try {
            Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();

            if (!selectedProduct.getAllAssociatedParts().isEmpty()) {
                throw new Exception("Cannot delete product with remaining associated parts.");
            }

            if (alertDeleteConfirmation("Delete", selectedProduct.getName())) {

                if (!Inventory.deleteProduct(selectedProduct)) {
                    alertDeleteFailed(selectedProduct.getName());
                }
            }
        }
        catch (NullPointerException e) {
            alertUnselectedItem();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }

    }

    /**
     * Exits the program after receiving confirmation from a dialog box.
     * @param event action event from button press
     */
    @FXML
    void onActionExit(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to Exit?");
        alert.setContentText("Press OK to Exit.");

        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            System.exit(0);
        }

    }

    /**
     * Loads the part form in modify mode.
     * This method will create a dialog box if the button is pressed while nothing is selected.
     * @param event action event from button press
     * @throws IOException if the fxml file is not found
     */
    @FXML
    void onActionModifyPart(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/PartForm.fxml"));
        loader.load();

        try {
            PartFormController partCtrl = loader.getController();
            partCtrl.enableModifyMode(partsTableView.getSelectionModel().getSelectedItem());

            loadViewFromButtonPress(event, loader);
        }
        catch (NullPointerException e) {
            alertUnselectedItem();
        }

    }

    /**
     * Loads the product form in modify mode.
     * This method will create a dialog box if the button is pressed while nothing is selected.
     * @param event action event from button press
     * @throws IOException if the fxml file is not found
     */
    @FXML
    void onActionModifyProduct(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ProductForm.fxml"));
        loader.load();

        try {
            ProductFormController productCtrl = loader.getController();
            productCtrl.enableModifyMode(productsTableView.getSelectionModel().getSelectedItem());

            loadViewFromButtonPress(event, loader);
        }
        catch (NullPointerException e) {
            alertUnselectedItem();
        }

    }

    /**
     * This method searches the parts list and sets the table view with the result.
     * It first tries to convert the input into an integer.
     * If successful, it calls the lookupPart() method with an integer.
     * If unsuccessful, it calls the lookupPart() method with a string.
     * <p>
     *     This method also sets a label with appropriate text if nothing is found from the search.
     * </p>
     * <p>
     *     RUNTIME ERROR a NumberFormatException was thrown when converting string to integer.
     *     This was fixed by catching the exception and using the catch block to search without converting to an integer.
     * </p>
     * @param event key event from the text field
     */
    @FXML
    void onActionPartSearch(KeyEvent event) {

        String input = partSearch.getText();

        try {
            // this line of code throws an exception if the input string has any characters that aren't numbers
            int id = Integer.parseInt(input);
            Part part = Inventory.lookupPart(id);

            if (part == null) {
                partNotFoundLbl.setText("ID not found  ");
            }
            else {
                partNotFoundLbl.setText("");
                partsTableView.getSelectionModel().select(part);
            }

            partsTableView.setItems(Inventory.getAllParts());
        }

        // this catch block catches the exception and begins to search using the string instead of an integer
        catch (NumberFormatException e) {
            ObservableList<Part> filteredParts = Inventory.lookupPart(input);

            if (filteredParts.isEmpty()) {
                partNotFoundLbl.setText("Name not found  ");
            }
            else {
                partNotFoundLbl.setText("");
            }

            partsTableView.setItems(filteredParts);
        }

    }

    /**
     * This method searches the products list and sets the table view with the result.
     * It first tries to convert the input into an integer.
     * If successful, it calls the lookupPart() method with an integer.
     * If unsuccessful, it calls the lookupPart() method with a string.
     * <p>
     *     This method also sets a label with appropriate text if nothing is found from the search.
     * </p>
     * @param event key event from the text field
     */
    @FXML
    void onActionProductSearch(KeyEvent event) {

        String input = productSearch.getText();

        try {
            int id = Integer.parseInt(input);
            Product product = Inventory.lookupProduct(id);

            if (product == null) {
                productNotFoundLbl.setText("ID not found  ");
            }
            else {
                productNotFoundLbl.setText("");
                productsTableView.getSelectionModel().select(product);
            }

            productsTableView.setItems(Inventory.getAllProducts());
        }

        catch (NumberFormatException e) {
            ObservableList<Product> filteredProducts = Inventory.lookupProduct(input);

            if (filteredProducts.isEmpty()) {
                productNotFoundLbl.setText("Name not found  ");
            }
            else {
                productNotFoundLbl.setText("");
            }

            productsTableView.setItems(filteredProducts);
        }

    }

    /**
     * This method initializes the parts table view with the parts list.
     * <p>
     *     RUNTIME ERROR javafx.base cannot access class model.Part because module kwiles.c482project does not open model to javafx.base.
     *     This was fixed by adding javafx.base in module-info.java statements
     * </p>
     */
    private void initializePartsTable() {

        partsTableView.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * This method initializes the products table view with the products list.
     */
    private void initializeProductsTable() {

        productsTableView.setItems(Inventory.getAllProducts());

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * This method is an override of the initialize method, used to initialize the table views.
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializePartsTable();
        initializeProductsTable();

    }
}
