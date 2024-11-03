import java.time.LocalDate;

public class Medication {
    private String name;
    private int stockLevel;
    private String dosage;
    private LocalDate expirationDate;
    private int lowStockThreshold;

    public Medication(String name, int stockLevel, String dosage, LocalDate expirationDate, int lowStockThreshold) {
        this.name = name;
        this.stockLevel = stockLevel;
        this.dosage = dosage;
        this.expirationDate = expirationDate;
        this.lowStockThreshold = lowStockThreshold;
    }

    public String getName() {
        return name;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public String getDosage() {
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

    @Override
    public String toString() {
        return "Medication Name: " + name +
               ", Stock Level: " + stockLevel +
               ", Dosage: " + dosage +
               ", Expiration Date: " + expirationDate +
               ", Low Stock Threshold: " + lowStockThreshold +
               (isStockLow() ? " (Low Stock)" : "") +
               (isExpired() ? " (Expired)" : "");
    }
}

