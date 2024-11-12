package model;

import java.time.LocalDate;

public class Medication {
    private String name;
    private int stockLevel;
    private int dosage;
    private LocalDate expirationDate;
    private int lowStockThreshold;

    public Medication(String name, int stockLevel,  LocalDate expirationDate, int lowStockThreshold) {
        this.name = name;
        this.stockLevel = stockLevel;
        //this.dosage = dosage;
        this.expirationDate = expirationDate;
        this.lowStockThreshold = lowStockThreshold;
    }
    
    public Medication(String name, int givenDosage)
    {
    	this.name = name;
    	this.dosage = givenDosage;
    }

    public String getName() {
        return name;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public int getDosage() {
        return dosage;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }

    public boolean isStockLow() {
        return stockLevel <= lowStockThreshold;
    }

    public void addStock(int amount) {
        this.stockLevel += amount;
    }
    
    public void minusStock(int dosage) {
    	this.stockLevel -= dosage;
    }



   /* public String toString() {
        // Basic information, always included
        StringBuilder result = new StringBuilder("Medication Name: " + name + ", Dosage: " + dosage + " pills ");

        // Include additional fields only if they are initialized (used in the first constructor)
        if (stockLevel > 0 || expirationDate != null || lowStockThreshold > 0) {
            result.append(", Stock Level: ").append(stockLevel)
                  .append(", Expiration Date: ").append(expirationDate != null ? expirationDate : "N/A")
                  .append(", Low Stock Threshold: ").append(lowStockThreshold);

            // Add status if stock is low or expired
            if (isStockLow()) result.append(" (Low Stock)");
            if (isExpired()) result.append(" (Expired)");
        }

        return result.toString();
    }*/
    
    @Override
    public String toString() {
        // Basic information, always included
        StringBuilder result = new StringBuilder("Medication Name: " + name);

        // Include dosage only if it has been assigned (i.e., greater than 0)
        if (dosage > 0) {
            result.append(", Dosage: ").append(dosage).append(" pills");
        }

        // Include additional fields only if they are initialized (used in the first constructor)
        if (stockLevel > 0 || expirationDate != null || lowStockThreshold > 0) {
            result.append(", Stock Level: ").append(stockLevel)
                  .append(", Expiration Date: ").append(expirationDate != null ? expirationDate : "N/A")
                  .append(", Low Stock Threshold: ").append(lowStockThreshold);

            // Add status if stock is low or expired
            if (isStockLow()) result.append(" (Low Stock)");
            if (isExpired()) result.append(" (Expired)");
        }

        return result.toString();
    }


}
