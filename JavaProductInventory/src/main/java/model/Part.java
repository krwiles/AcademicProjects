package model;
/**
 * Supplied class Part.java
 */

/**
 * An abstract class to hold generic part members.
 * @author Kurtis
 */
public abstract class Part {

    /** The part id */
    private int id;
    /** The part name */
    private String name;
    /** The part price */
    private double price;
    /** The part stock/inventory */
    private int stock;
    /** The part min stock/inventory */
    private int min;
    /** THe part max stock/inventory */
    private int max;


    /**
     * Constructs a new part (to be used as super).
     * @param id the part id
     * @param name the part name
     * @param price the part price
     * @param stock the part stock/inventory
     * @param min the part min stock/inventory
     * @param max the part max stock/inventory
     */
    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Gets the part id.
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the part id.
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the part name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the part name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the part price.
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the part price.
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the part stock/inventory.
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the part stock/inventory.
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Gets the part min stock.
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the part min stock.
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Gets the part max stock.
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the part max stock.
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

}