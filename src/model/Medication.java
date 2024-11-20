package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a medication in the system.
 * Stores details such as name, stock level, dosage, expiration date, and low stock threshold.
 * Provides methods to manage stock levels and check for low stock or expiration status.
 * @author Lee Kuan Rong, Lim Wee Keat, Tan Wen Rong
 * @version 1.0
 * @since 2024-11-18
 */
public class Medication implements Serializable {

    private static final long serialVersionUID = 3164761930276452742L;
    private String name;
    private int stockLevel;
    private int dosage;
    private int lowStockThreshold;
    private LocalDate expirationDate;

    /**
     * Constructs a {@code Medication} object with the specified details.
     *
     * @param name              The name of the medication.
     * @param stockLevel        The initial stock level of the medication.
     * @param expirationDate    The expiration date of the medication.
     * @param lowStockThreshold The threshold below which the stock is considered low.
     */
    public Medication(String name, int stockLevel, LocalDate expirationDate, int lowStockThreshold) {
        this.name = name;
        this.stockLevel = stockLevel;
        this.setExpirationDate(expirationDate);
        this.lowStockThreshold = lowStockThreshold;
    }

    /**
     * Constructs a {@code Medication} object with the specified name and dosage.
     * Useful for scenarios where only the medication name and dosage are needed.
     *
     * @param name      The name of the medication.
     * @param givenDosage The dosage of the medication.
     */
    public Medication(String name, int givenDosage) {
        this.name = name;
        this.dosage = givenDosage;
    }

    /**
     * Retrieves the name of the medication.
     *
     * @return The name of the medication.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the current stock level of the medication.
     *
     * @return The stock level.
     */
    public int getStockLevel() {
        return stockLevel;
    }

    /**
     * Retrieves the dosage of the medication.
     *
     * @return The dosage.
     */
    public int getDosage() {
        return dosage;
    }

    /**
     * Retrieves the low stock threshold of the medication.
     *
     * @return The low stock threshold.
     */
    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    /**
     * Sets the stock level of the medication.
     *
     * @param stockLevel The new stock level.
     */
    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    /**
     * Checks if the stock level is below or equal to the low stock threshold.
     *
     * @return {@code true} if the stock is low, {@code false} otherwise.
     */
    public boolean isStockLow() {
        return stockLevel <= lowStockThreshold;
    }

    /**
     * Increases the stock level of the medication by a specified amount.
     *
     * @param amount The amount to add to the stock.
     */
    public void addStock(int amount) {
        this.stockLevel += amount;
    }

    /**
     * Decreases the stock level of the medication by a specified amount.
     *
     * @param dosage The amount to subtract from the stock.
     */
    public void minusStock(int dosage) {
        this.stockLevel -= dosage;
    }

    /**
     * Retrieves the expiration date of the medication.
     *
     * @return The expiration date.
     */
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the expiration date of the medication.
     *
     * @param expirationDate The new expiration date.
     */
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Returns a string representation of the medication.
     * Includes name, dosage (if specified), stock level, and low stock threshold.
     * Marks the medication as low stock if the stock level is below the threshold.
     *
     * @return A string representation of the medication.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Medication Name: " + name);

        if (dosage > 0) {
            result.append(", Dosage: ").append(dosage).append(" pills");
        }

        if (stockLevel > 0 || lowStockThreshold > 0) {
            result.append(", Stock Level: ").append(stockLevel)
                  .append(", Low Stock Threshold: ").append(lowStockThreshold);

            if (isStockLow()) result.append(" (Low Stock)");
        }

        return result.toString();
    }
}
