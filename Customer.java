import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class Customer extends UserBase {

    private double balance;
    private ArrayList<Product> purchaseHistory;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public Customer(String email, String password, String nickname, double balance, ArrayList<Product> purchaseHistory,
        ObjectInputStream ois, ObjectOutputStream oos) {
        super(email, nickname, password);
        this.balance = balance;
        this.purchaseHistory = purchaseHistory;
        this.ois = ois;
        this.oos = oos;
    }

    public ArrayList<Product> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory() {
        this.purchaseHistory = purchaseHistory;
    }
    @Override
    public String getRole() {
        return "customer";
    }

    // placed method for displaying customer menu inside customer class
    public void displayMarket(ArrayList<Product> products,
                                     String property,
                                     String sortBy,
                                     boolean sale,
                                     boolean searching,
                                     String search) {
        try {
            DisplayMarketCondition condition = new DisplayMarketCondition(products, property, sortBy, sale, searching, search);
            oos.writeObject(condition);

            if (sale) {
                System.out.println("Getting products on sale...");
            }
            
            if (property.equals("normal") && sortBy.equals("normal")) {
                System.out.println("Now displaying the marketplace...\n====================");
            } else {
                System.out.println("Sorting marketplace...\n====================");
                // Sort Arraylist of Products with selection sort
                int n = products.size();
                for (int i = 0; i < n-1; i++) {
                    int min_idx = i;
                    for (int j = i+1; j < n; j++)
                        if (property.equals("quantity")) {
                            if (products.get(j).getQuantity() < products.get(min_idx).getQuantity()) {
                                min_idx = j;
                            }
                        } else if (property.equals("price")) {
                            if (products.get(j).getPrice() < products.get(min_idx).getPrice()) {min_idx = j;
                            }
                        }
                    Collections.swap(products, min_idx, i);
                }
                // reverses list to descending if needed
                if (sortBy.equals("descending")) {
                    Collections.reverse(products);
                }
            }
            ArrayList<String> productStrings = new ArrayList<>();
            for (Product p: products) {
                if (sale) {
                    if (p.isOnSale()) {
                        if (searching) {
                            if (p.getName().toLowerCase().contains(search.toLowerCase()) ||
                                    p.getStoreName().toLowerCase().contains(search.toLowerCase()) ||
                                    p.getDescription().toLowerCase().contains(search.toLowerCase())) {
                                System.out.printf("%s - %s - $%.2f\n", p.getStoreName(), p.getName(), p.getPrice());
                                productStrings.add(String.format("%s - %s - $%.2f\n", p.getStoreName(), p.getName(), p.getPrice()));
                            }
                        } else {
                            System.out.printf("%s - %s - $%.2f\n", p.getStoreName(), p.getName(), p.getPrice());
                            productStrings.add(String.format("%s - %s - $%.2f\n", p.getStoreName(), p.getName(), p.getPrice()));
                        }
                    }
                } else if (searching) {
                    if (p.getName().toLowerCase().contains(search.toLowerCase()) ||
                            p.getStoreName().toLowerCase().contains(search.toLowerCase()) ||
                            p.getDescription().toLowerCase().contains(search.toLowerCase())) {
                        if (sale) {
                            if (p.isOnSale()) {
                                System.out.printf("%s - %s - $%.2f\n", p.getStoreName(), p.getName(), p.getPrice());
                                productStrings.add(String.format("%s - %s - $%.2f\n", p.getStoreName(), p.getName(), p.getPrice()));
                            }
                        } else {
                            System.out.printf("%s - %s - $%.2f\n", p.getStoreName(), p.getName(), p.getPrice());
                            productStrings.add(String.format("%s - %s - $%.2f\n", p.getStoreName(), p.getName(), p.getPrice()));
                        }
                    }
                } else {
                    System.out.printf("%s - %s - $%.2f\n", p.getStoreName(), p.getName(), p.getPrice());
                    productStrings.add(String.format("%s - %s - $%.2f\n", p.getStoreName(), p.getName(), p.getPrice()));
                }
            }
            oos.writeObject(productStrings);
            System.out.println("====================");
        } catch (Exception e) {
            System.out.println("There was an error displaying the market page!");
        }
    }

    // method for displaying dashboard
    public static void displayDashboard(ArrayList<Store> stores, String sortBy, ArrayList<Product> purchaseHistory) {
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
                    for (int j = i + 1; j < n; j++) {
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
                System.out.printf("%s, %d products sold total, ", x.getName(), x.getsales());
                String purchasehistory = "";
                ArrayList<Product> storeProducts = x.getProducts();
                for (Product purchasedProduct: purchaseHistory) {
                    for (int i = 0; i < storeProducts.size(); i++) {
                        String purchasedname = purchasedProduct.getName();
                        if (purchasedname.contains(storeProducts.get(i).getName())) {
                            purchasehistory += purchasedname;
                            if (i != 0 && i < storeProducts.size() - 1) {
                                purchasehistory += ", ";
                            }
                        }
                    }
                }
                if (purchasehistory.equals("")) {
                    purchasehistory = "none";
                }
                System.out.printf("Products that you purchased: %s\n", purchasehistory);
            }
            System.out.println("====================");
        } catch (Exception e) {
            System.out.println("There was an error displaying the dashboard!");
        }
    }

    // checks if purchase is successful and adds product to current customer's purchase history

    public static boolean purchase(ArrayList<Product> products, ArrayList<Store> stores, String name,
                                   ArrayList<Product> purchaseHistory, String nickname) {
        try {
            for (Product p: products) {
                if (p.getName().equalsIgnoreCase(name)) {
                    p.setQuantity(p.getQuantity() - 1);
                    p.setQuantitySold(p.getQuantitySold() + 1);
                    String storename = p.getStoreName();
                    for (Store s: stores) {
                        if (s.getName().equals(storename)) {
                            s.setSales(s.getsales() + 1);
                            s.addCustomer(nickname);
                            purchaseHistory.add(p);
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}
