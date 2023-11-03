package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This class is the controller for the part form.
 * @author Kurtis
 */
public class PartFormController extends BaseController implements Initializable {

    /** The part id text field */
    @FXML
    private TextField partIdTxt;
    /** The in-house radio button */
    @FXML
    private RadioButton partInhouseRBtn;
    /** The part stock/inventory text field */
    @FXML
    private TextField partInventoryTxt;
    /** The part max stock/inventory text field */
    @FXML
    private TextField partMaxTxt;
    /** The part min stock/inventory text field */
    @FXML
    private TextField partMinTxt;
    /** The part name text field */
    @FXML
    private TextField partNameTxt;
    /** The outsourced radio button */
    @FXML
    private RadioButton partOutsourcedRBtn;
    /** The part price text field */
    @FXML
    private TextField partPriceTxt;
    /** The part source label */
    @FXML
    private Label partSourceLbl;
    /** The part source text field */
    @FXML
    private TextField partSourceTxt;
    /** The form title label */
    @FXML
    private Label partTitleLbl;
    /** The form mode enum */
    private FormMode formMode;


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
     * Saves the part and returns to the main form.
     * This will call saveNewPartAndExit() if the formMode is set to ADD,
     * or it will call updatePartAndExit() if the formMode is set to MODIFY.
     * @param event action event from button press
     * @throws IOException if the fxml file is not found
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {

        if (formMode == FormMode.ADD) {
            saveNewPartAndExit(event);
        }
        else if (formMode == FormMode.MODIFY) {
            updatePartAndExit(event);
        }

    }

    /**
     * Updates a part and returns to the main form.
     * This will generate a dialog box if the inputs are of the incorrect format.
     * @param event action event from button press
     * @throws IOException if the fxml file is not found
     */
    private void updatePartAndExit(ActionEvent event) throws IOException {

        try {
            int id = Integer.parseInt(partIdTxt.getText());

            String name = partNameTxt.getText();
            int inventory = Integer.parseInt(partInventoryTxt.getText());
            double price = Double.parseDouble(partPriceTxt.getText());
            int max = Integer.parseInt(partMaxTxt.getText());
            int min = Integer.parseInt(partMinTxt.getText());

            validateMinMaxInv(min, max, inventory);

            int index = 0;
            for (Part part : Inventory.getAllParts()) {

                if (part.getId() == id) {
                    break;
                }

                index++;
            }

            if (partInhouseRBtn.isSelected()) {
                int machineId = Integer.parseInt(partSourceTxt.getText());
                Inventory.updatePart(index, new InHouse(id, name, price, inventory, min, max, machineId));
            }
            else if (partOutsourcedRBtn.isSelected()) {
                String companyName = partSourceTxt.getText();
                Inventory.updatePart(index, new Outsourced(id, name, price, inventory, min, max, companyName));
            }

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
     * Saves a new part and returns to the main form.
     * This will generate a dialog box if the inputs are of the incorrect format.
     * @param event action event from button press
     * @throws IOException if the fxml file is not found
     */
    private void saveNewPartAndExit(ActionEvent event) throws IOException {

        try {
            int id = uniquePartIdGenerator();

            String name = partNameTxt.getText();
            int inventory = Integer.parseInt(partInventoryTxt.getText());
            double price = Double.parseDouble(partPriceTxt.getText());
            int max = Integer.parseInt(partMaxTxt.getText());
            int min = Integer.parseInt(partMinTxt.getText());

            validateMinMaxInv(min, max, inventory);

            if (partInhouseRBtn.isSelected()) {
                int machineId = Integer.parseInt(partSourceTxt.getText());
                Inventory.addPart(new InHouse(id, name, price, inventory, min, max, machineId));
            }
            else if (partOutsourcedRBtn.isSelected()) {
                String companyName = partSourceTxt.getText();
                Inventory.addPart(new Outsourced(id, name, price, inventory, min, max, companyName));
            }

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
     * Changes the source label on radio button press.
     * @param event action event from button press
     */
    @FXML
    void onActionInhouseRBtn(ActionEvent event) {
        partSourceLbl.setText("Machine ID");
    }

    /**
     * Changes the source label on radio button press.
     * @param event action event from button press
     */
    @FXML
    void onActionOutsourcedRBtn(ActionEvent event) {
        partSourceLbl.setText("Company Name");
    }

    /**
     * Enables add mode for the part form.
     * This method is called by the main controller before switching views to the part form.
     * It changes labels of the form and sets the enum formMode to ADD.
     */
    public void enableAddMode() {

        formMode = FormMode.ADD;

        partTitleLbl.setText("Add Part");
        partIdTxt.setText("Automatic");

    }

    /**
     * Enables modify mode for the part form.
     * This method is called by the main controller before switching views to the part form.
     * It changes labels and text fields of the form and sets the enum formMode to MODIFY.
     * @param part the part that is being modified
     */
    public void enableModifyMode(Part part) {

        formMode = FormMode.MODIFY;

        partTitleLbl.setText("Modify Part");

        partIdTxt.setText(Integer.toString(part.getId()));
        partNameTxt.setText(part.getName());
        partInventoryTxt.setText(Integer.toString(part.getStock()));
        partPriceTxt.setText(Double.toString(part.getPrice()));
        partMaxTxt.setText(Integer.toString(part.getMax()));
        partMinTxt.setText(Integer.toString(part.getMin()));

        if (part instanceof InHouse) {
            partSourceTxt.setText(Integer.toString(((InHouse) part).getMachineId()));
        }
        else if (part instanceof Outsourced) {
            partOutsourcedRBtn.setSelected(true);
            partSourceTxt.setText(((Outsourced) part).getCompanyName());
            partSourceLbl.setText("Company Name");
        }

    }

    /**
     * Generates a unique part id.
     * it will find the first available id counting up from 1.
     * @return the next unique id
     */
    public int uniquePartIdGenerator() {

        ObservableList<Part> allParts = Inventory.getAllParts();
        ArrayList<Integer> idList = new ArrayList<Integer>(allParts.size());

        for (Part part : allParts) {
            idList.add(part.getId());
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
     * This method is an override of the initialize method.
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
