package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class is the controller for the product form.
 * @author Kurtis
 */
public class ProductFormController extends BaseController implements Initializable {

    /** The parts table */
    @FXML
    private TableView<Part> partsTableView;
    /** The part id column */
    @FXML
    private TableColumn<Part, Integer> partIdCol;
    /** The part stock/inventory column */
    @FXML
    private TableColumn<Part, Integer> partInventoryCol;
    /** The part name column */
    @FXML
    private TableColumn<Part, String> partNameCol;
    /** The part price column */
    @FXML
    private TableColumn<Part, Double> partPriceCol;
    /** The part searchbar */
    @FXML
    private TextField partSearch;
    /** The product id text field */
    @FXML
    private TextField productIdTxt;
    /** The product stock/inventory text field */
    @FXML
    private TextField productInventoryTxt;
    /** The product max stock/inventory text field */
    @FXML
    private TextField productMaxTxt;
    /** The product min stock/inventory text field */
    @FXML
    private TextField productMinTxt;
    /** The product name text field */
    @FXML
    private TextField productNameTxt;
    /** The associated parts table */
    @FXML
    private TableView<Part> productPartsTableView;
    /** The associated part id column */
    @FXML
    private TableColumn<Part, Integer> productPartIdCol;
    /** The associated part stock/inventory column */
    @FXML
    private TableColumn<Part, Integer> productPartInventoryCol;
    /** The associated part name column */
    @FXML
    private TableColumn<Part, String> productPartNameCol;
    /** The associated part price column */
    @FXML
    private TableColumn<Part, Double> productPartPriceCol;
    /** The product price text field */
    @FXML
    private TextField productPriceTxt;
    /** The form title label */
    @FXML
    private Label productTitleLbl;
    /** The part not found warning label */
    @FXML
    private Label partNotFoundLbl;
    /** A product object used to add/modify a product in the list of products */
    private Product newProduct;
    /** The form mode enum */
    private FormMode formMode;


    /**
     * Adds the selected part to the product's associated parts list.
     * This method will create a dialog box if no part is selected.
     * @param event action event from button press
     */
    @FXML
    void onActionAddPart(ActionEvent event) {

        try {
            Part part = Objects.requireNonNull(partsTableView.getSelectionModel().getSelectedItem());
            newProduct.addAssociatedPart(part);
        }
        catch (NullPointerException e) {
            alertUnselectedItem();
        }

    }

    /**
     * Returns to the main form after receiving confirmation from a dialog box.
     * @param event action event from button press
     * @throws IOException if the fxml file is not found
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {

        if (alertCancelConfirmation()) {
            loadViewFromButtonPress(event, "/view/MainForm.fxml");
        }

    }

    /**
     * Removes the selected part from the product's associated parts list.
     * This method will create a dialog box if no part is selected,
     * and will also create a dialog box asking for confirmation.
     * @param event action event from button press
     */
    @FXML
    void onActionRemovePart(ActionEvent event) {

        try {
            Part part = Objects.requireNonNull(productPartsTableView.getSelectionModel().getSelectedItem());

            if (alertDeleteConfirmation("Remove", part.getName())) {
                newProduct.deleteAssociatedPart(part);
            }
        }
        catch (NullPointerException e) {
            alertUnselectedItem();
        }

    }

    /**
     * Saves the product and returns to the main form.
     * This will call saveNewProductAndExit() if the formMode is set to ADD,
     * or it will call updateProductAndExit() if the formMode is set to MODIFY.
     * @param event action event from button press
     * @throws IOException if the fxml file is not found
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {

        if (formMode == FormMode.ADD) {
            saveNewProductAndExit(event);
        }
        else if (formMode == FormMode.MODIFY) {
            updateProductAndExit(event);
        }

    }

    /**
     * Updates a product and returns to the main form.
     * This will generate a dialog box if the inputs are of the incorrect format.
     * @param event action event from button press
     * @throws IOException if the fxml file is not found
     */
    private void updateProductAndExit(ActionEvent event) throws IOException {

        try {
            int min = Integer.parseInt(productMinTxt.getText());
            int max = Integer.parseInt(productMaxTxt.getText());
            int inventory = Integer.parseInt(productInventoryTxt.getText());

            validateMinMaxInv(min, max, inventory);

            int id = Integer.parseInt(productIdTxt.getText());
            newProduct.setId(id);
            newProduct.setName(productNameTxt.getText());
            newProduct.setStock(inventory);
            newProduct.setPrice(Double.parseDouble(productPriceTxt.getText()));
            newProduct.setMax(max);
            newProduct.setMin(min);

            int index = 0;
            for (Product product : Inventory.getAllProducts()) {

                if (product.getId() == id) {
                    break;
                }

                index++;
            }

            Inventory.updateProduct(index, newProduct);

            loadViewFromButtonPress(event, "/view/MainForm.fxml");
        }
        catch (NumberFormatException e) {
            alertInputError();
        }
        catch (Exception e) {
            alertInputError(e.getMessage());
        }

    }

    /**
     * Saves a new product and returns to the main form.
     * This will generate a dialog box if the inputs are of the incorrect format.
     * @param event action event from button press
     * @throws IOException if the fxml file is not found
     */
    private void saveNewProductAndExit(ActionEvent event) throws IOException {

        try {
            int min = Integer.parseInt(productMinTxt.getText());
            int max = Integer.parseInt(productMaxTxt.getText());
            int inventory = Integer.parseInt(productInventoryTxt.getText());

            validateMinMaxInv(min, max, inventory);

            newProduct.setId(uniqueProductIdGenerator());

            newProduct.setName(productNameTxt.getText());
            newProduct.setStock(inventory);
            newProduct.setPrice(Double.parseDouble(productPriceTxt.getText()));
            newProduct.setMax(max);
            newProduct.setMin(min);

            Inventory.addProduct(newProduct);

            loadViewFromButtonPress(event, "/view/MainForm.fxml");
        }
        catch (NumberFormatException e) {
            alertInputError();
        }
        catch (Exception e) {
            alertInputError(e.getMessage());
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
     * @param event key event from the text field
     */
    @FXML
    void onActionPartSearch(KeyEvent event) {

        String input = partSearch.getText();

        try {
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
     * Enables add mode for the product form.
     * This method is called by the main controller before switching views to the product form.
     * It changes labels of the form and sets the enum formMode to ADD.
     */
    public void enableAddMode() {

        formMode = FormMode.ADD;

        productTitleLbl.setText("Add Product");
        productIdTxt.setText("Automatic");

    }

    /**
     * Enables modify mode for the product form.
     * This method is called by the main controller before switching views to the product form.
     * It sets the labels, text fields, and table views of the form and sets the enum formMode to MODIFY.
     * @param product the product that is being modified
     */
    public void enableModifyMode(Product product) {

        formMode = FormMode.MODIFY;

        productTitleLbl.setText("Modify Product");

        productIdTxt.setText(Integer.toString(product.getId()));
        productNameTxt.setText(product.getName());
        productInventoryTxt.setText(Integer.toString(product.getStock()));
        productPriceTxt.setText(Double.toString(product.getPrice()));
        productMaxTxt.setText(Integer.toString(product.getMax()));
        productMinTxt.setText(Integer.toString(product.getMin()));

        for (Part part : product.getAllAssociatedParts()) {
            newProduct.addAssociatedPart(part);
        }

    }

    /**
     * Generates a unique product id.
     * it will find the first available id counting up from 1.
     * @return the next unique id
     */
    public int uniqueProductIdGenerator() {

        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ArrayList<Integer> idList = new ArrayList<Integer>(allProducts.size());

        for (Product product : allProducts) {
            idList.add(product.getId());
        }

        idList.sort(Comparator.naturalOrder());

        int newId = 1;
        for (Integer id : idList) {
            if (id != newId) {
                break;
            }
            newId++;
        }
        return newId;

    }

    /**
     * This method initializes the parts table view with the parts list.
     */
    private void initializePartsTable() {

        partsTableView.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * This method initializes the product's associated parts table view with the product's associated parts list.
     */
    private void initializeProductPartsTable() {

        productPartsTableView.setItems(newProduct.getAllAssociatedParts());

        productPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productPartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * This method is an override of the initialize method,
     * used to initialize the table views and the product that will be added/modified.
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializePartsTable();

        newProduct = new Product(0, null, 0, 0, 0, 0);
        initializeProductPartsTable();

    }

}
