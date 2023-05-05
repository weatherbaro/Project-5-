import java.util.ArrayList;
import java.util.Collections;

public class Seller extends UserBase {

    private String email;
    private ArrayList<Store> stores;

    public Seller (String email, String password, String nickname, ArrayList<Store> stores){
        super(email, nickname, password);
        this.stores = stores;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void addStore(Store store) {
        stores.add(store);
    }

    public static void displayDashboard(ArrayList<Store> stores, String sortBy) {
        try {
            // displaying dashboard with sorting conditonals
            if (sortBy.equals("normal")) {
                System.out.println("Now displaying the dashboard...\n====================");
            } else {
                System.out.println("Sorting dashboard...\n====================");
                // Sort Arraylist of Products with selection sort
                int n = stores.size();
                for (int i = 0; i < n-1; i++) {
                    int min_idx = i;
                    for (int j = i+1; j < n; j++) {
                        if (stores.get(j).getsales() < stores.get(min_idx).getsales()) {
                            min_idx = j;
                        }
                    }
                    Collections.swap(stores, min_idx, i);
                }
                // reverses list to descending if needed
                if (sortBy.equals("descending")) {
                    Collections.reverse(stores);
                }
            }
            for (Store x: stores) {
                ArrayList<String> customerlist = x.getCustomers();
                String s = "";
                for (String nickname: customerlist) {
                    s += nickname;
                }
                System.out.printf("Store: %s\nCustomers: %s\n", x.getName(), s);

                ArrayList<Product> storeProducts = x.getProducts();
                System.out.printf("Products from %s listed below...\n", x.getName());
                for (Product p: storeProducts) {
                    System.out.printf("%s - %d sold\n", p.getName(), p.getQuantitySold());
                }
                System.out.println("--------------------");

            }
            System.out.println("====================");
        } catch (Exception e) {
            System.out.println("There was an error displaying the dashboard!");
        }
    }

    @Override
    public String getRole() {
        return "seller";
    }
}
