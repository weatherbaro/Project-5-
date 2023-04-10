import java.util.ArrayList;

public class Store {

    private String name;
    private ArrayList<Product> products;
    private int sales;
    private ArrayList<Product> productsSold;
    private ArrayList<String> customers;

    public Store(String name, int sales, ArrayList<Product> products, ArrayList<String> customers) {
        this.name = name;
        this.sales = sales;
        this.products = products;
        this.customers = customers;
    }

    public int getsales() {
        return sales;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<String> getCustomers() { return customers; }

    public void addProduct(Product product) throws NotAProductException {
        if (product instanceof Product) {
            products.add(product);
        } else {
            throw new NotAProductException("This is not a product");
        }
    }
    public void removeProduct(Product product) {
        products.remove(product);
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public ArrayList<Product> getProductsSold() {
        return productsSold;
    }

    public void addCustomer(String customerName) {
        this.customers.add(customerName);
    }


}
