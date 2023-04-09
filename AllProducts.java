import java.util.ArrayList;
//class that shows the customer all products to choose from, returns products that match the customer search
public class AllProducts {
    private ArrayList<Product> allProducts;

    public ArrayList<Product> getProducts() {
        return allProducts;
    }

    public void showMatches(String customerSearch) {
        String[] array = allProducts.toArray(new String[allProducts.size()]);
        for (int i = 0; i < array.length; i++) {
            if (array[i].contains(customerSearch)) {
                System.out.println(array[i]);
            }
        }
    }

}
