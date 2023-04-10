import java.util.ArrayList;

public class Store {

    private String name;
    private ArrayList<Product> products;
    private ArrayList<Product> productsSold;
    
    public Store(String name, ArrayList<Product> products) {
        this.name = name;
        this.products = products;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
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

    public ArrayList<Product> getProductsSold() {
        return productsSold;
    }

}
