
public class PurchaseHistory implements Persistent<PurchaseHistory> {

    private String purchaserEmail;

    private String storeName;

    private String productName;

    private double productPrice;

    private int quantity;

    private double totalPrice;

    public PurchaseHistory() {
    }

    public PurchaseHistory(String purchaserEmail, String storeName, String productName, double productPrice, int quantity, double totalPrice) {
        this.purchaserEmail = purchaserEmail;
        this.storeName = storeName;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPurchaserEmail() {
        return purchaserEmail;
    }

    public void setPurchaserEmail(String purchaserEmail) {
        this.purchaserEmail = purchaserEmail;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String serialize() {
        return String.format("%s,%s,%s,%f,%d,%f", purchaserEmail, storeName, productName, productPrice, quantity, totalPrice);
    }

    @Override
    public PurchaseHistory deserialize(String s) {
        String[] split = s.split(",");
        setPurchaserEmail(split[0]);
        setStoreName(split[1]);
        setProductName(split[2]);
        setProductPrice(Double.parseDouble(split[3]));
        setQuantity(Integer.parseInt(split[4]));
        setTotalPrice(Double.parseDouble(split[5]));
        return this;
    }

    public static PurchaseHistory createBy(Customer customer, Product product, int quantity) {
        return new PurchaseHistory(customer.getEmail(), product.getStoreName(), product.getName(), product.getPrice(), quantity, product.getPrice() * quantity) ;
    }

    @Override
    public String toString() {
        return "PurchaseHistory{" +
                "purchaserEmail='" + purchaserEmail + '\'' +
                ", storeName='" + storeName + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
