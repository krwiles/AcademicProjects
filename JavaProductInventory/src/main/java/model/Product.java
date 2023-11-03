package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * An object to represent a product.
 * @author Kurtis
 */
public class Product {

    /** The list that holds parts associated to the product */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    /** The product id */
    private int id;
    /** The product name */
    private String name;
    /** The product price */
    private double price;
    /** The product stock/inventory */
    private int stock;
    /** The product min stock/inventory */
    private int min;
    /** The product max stock/inventory */
    private int max;


    /**
     * Constructs a new product and sets all product members except the associated parts list.
     * The associated parts list must have parts added to it using the addAssociatedPart method.
     * @param id the product id
     * @param name the product name
     * @param price the product price
     * @param stock the product stock/inventory
     * @param min the product max stock/inventory
     * @param max the product min stock/inventory
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Gets the product id.
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price.
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price.
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the stock/inventory.
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the stock/inventory.
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Gets the min stock.
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the min stock.
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Gets the max stock.
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the max stock.
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Adds a part to the product's associated parts list.
     * @param part the part to add
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Removes a part from the product's associated parts list.
     * @param part the part to delete
     * @return true if the part was removed successfully
     */
    public boolean deleteAssociatedPart(Part part) {
        return associatedParts.remove(part);
    }

    /**
     * Gets the product's list of associated parts.
     * @return the ObservableList of associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

}
