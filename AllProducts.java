
import java.util.ArrayList;

public class AllProducts {

    private static ArrayList<Product> allProducts;

    public static ArrayList<Product> getProducts() {
        return allProducts;
    }

    public static void showMatches(String customerSearch) {
        String[] array = allProducts.toArray(new String[allProducts.size()]);
        for (int i = 0; i < array.length; i++) {
            if (array[i].contains(customerSearch)) {
                System.out.println(array[i]);
            }
        }
    }

    // TODO: 2023/4/10 read all products from file

}
