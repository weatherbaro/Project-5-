public class Product {
    private String name;
    private String storeName;
    private String description;
    private int quantity;
    private int quantitySold;
    private double price;
    private double salePrice;
    private boolean onSale;


    public Product(String name, String storeName, boolean onSale, String description, int quantity, double price, int quantitySold) {
        this.name = name;
        this.storeName = storeName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.quantitySold = quantitySold;
        this.onSale = onSale;
    }

    public String getName() { return name; }
    public String getStoreName() { return storeName; }
    public String getDescription() { return description; }
    public boolean isOnSale() { return onSale; }
    public int getQuantity() { return quantity; }
    public int getQuantitySold() { return quantitySold; }
    public double getPrice() { return price; }
    public double getSalePrice() { return salePrice; }


    public void setName(String name) { this.name = name; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public void setSale(boolean onSale) { this.onSale = onSale; }
    public void setDescription(String description) { this.description = description; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setQuantitySold(int quantitySold) { this.quantitySold = quantitySold; }
    public void setPrice(double price) { this.price = price; }

    public void holdSale(int quantityToSell, double salePrice) {
        this.salePrice = salePrice;
        while (quantityToSell < quantitySold) {
            this.onSale = true;
        }
    }

    public String saleString() {
        if (onSale) {
            String format = "Product name: %s\nStore Name: %s\nNew price: %d\nOriginal price: %d";
            return String.format(format, this.name, this.storeName, this.salePrice, this.price);
        } else {
            String format = "Product name: %s\nStore Name: %s\nPrice: %d";
            return String.format(format, this.name, this.storeName, this.price);
        }
    }

    public String productPage() {
        String format = "====================\nName: %s\nProduct description: %s\nQuantity available: %d";
        return String.format(format, this.name, this.description, this.quantity);
    }

}

