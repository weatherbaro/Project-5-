
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

public class Customer extends UserBase {

    private final static String PURCHASE_HISTORY_FILE_PATH = "system\\customer\\purchase\\history.txt";
    private final static String PURCHASE_HISTORY_FILE_PATTERN = "customer\\purchase\\history\\%s.txt";
    private final static String MESSAGE_FILE_PATTERN = "system\\message\\%s.txt";

    /**
     * 账户余额
     */
    private double balance;

    public Customer(String email, String password, String nickname, double balance) {
        super(email, nickname, password);
        this.balance = balance;
    }

    /**
     * 购买
     * @param product 商品
     * @param quantity 购买数量
     * @return true-成功 false-失败
     */
    public boolean purchase(Product product, int quantity) {
        if (product.getQuantity() < quantity) {
            // 库存不足
            return false;
        }
        if (balance < product.getPrice() * quantity) {
            // 余额不足
            return false;
        }
        // 扣余额
        balance--;
        // 扣库存
        product.purchased(quantity);
        // 购买历史持久化
        FilesModule.append(PurchaseHistory.createBy(this, product, quantity), PURCHASE_HISTORY_FILE_PATH);
        // todo 余额持久化, 商品库存持久化
        return true;
    }

    /**
     * 浏览商品
     * 支持根据关键字搜索, 并根据指定字段排序
     */
    public void browseProducts(String searchBy, String keyword, String sortBy) {
        ArrayList<Product> allProducts = getProducts();

        Predicate<Product> predicate = null;
        if (searchBy != null && keyword != null) {
            switch (searchBy) {
                case "name":
                    predicate = product -> product.getName().contains(keyword);
                    break;
                case "store":
                    predicate = product -> product.getStoreName().contains(keyword);
                    break;
            }
        }
        ArrayList<Product> products = new ArrayList<>();
        for (Product product : allProducts) {
            if (predicate == null || predicate.test(product)) {
                products.add(product);
            }
        }
        Comparator<Product> comparator = null;
        if (sortBy != null) {
            switch (sortBy) {
                case "price":
                    comparator = Comparator.comparing(Product::getPrice);
                    break;
                case "quantity":
                    comparator = Comparator.comparing(Product::getQuantity);
            }
        }
        if (comparator != null) {
            products.sort(comparator);
        }
        // TODO: 2023/4/9 展示商品信息
    }

    private ArrayList<Product> getProducts() {
        return AllProducts.getProducts();
    }

    /**
     * 联系卖家
     * @param seller 卖家
     * @param content 消息内容
     */
    public void leaveMessageTo(Seller seller, String content) {
        Message message = Message.createBy(this, seller, content);
        String filePath = String.format(MESSAGE_FILE_PATTERN, seller.getEmail());
        FilesModule.append(message, filePath);
    }

    /**
     * 查看购买历史
     */
    public ArrayList<PurchaseHistory> viewPurchaseHistory() {
        ArrayList<PurchaseHistory> historyArrayList = FilesModule.load(PURCHASE_HISTORY_FILE_PATH, PurchaseHistory::new,
            purchaseHistory -> purchaseHistory.getPurchaserEmail().equals(this.getEmail()));
        System.out.println(historyArrayList);
        return historyArrayList;
    }

    /**
     * 导出购买历史记录
     */
    public void exportPurchaseHistory() {
        String to = String.format(PURCHASE_HISTORY_FILE_PATTERN, this.getEmail());
        Predicate<PurchaseHistory> predicate = purchaseHistory -> this.getEmail().equals(purchaseHistory.getPurchaserEmail());
        FilesModule.export(PURCHASE_HISTORY_FILE_PATH, to, predicate, new PurchaseHistory());
    }

    @Override
    public String getRole() {
        return "customer";
    }
    
    // placed method for displaying customer menu inside customer class
    public static void displayMarket(String property, String sortBy,
                                     boolean sale,
                                     boolean searching, String search) {
        try {
            File productfile = new File("Products.txt"); // product data file
            BufferedReader r = new BufferedReader(new FileReader(productfile));
            String line = "";
            String[] productData;

            String name;
            String store;
            String description;
            boolean onSale;
            int quantity;
            double price;

            ArrayList<Product> products = new ArrayList<>();
            while ((line = r.readLine()) != null) {
                productData = line.split("; ", 6);
                name = productData[0];
                store = productData[1];
                onSale = Boolean.parseBoolean(productData[2]);
                description = productData[3];
                quantity = Integer.parseInt(productData[4]);
                price = Double.parseDouble(productData[5]);
                products.add(new Product(name, store, onSale, description, quantity, price));
            }
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
            for (Product p: products) {
                if (sale) {
                    if (p.isOnSale()) {
                        if (searching) {
                            if (p.getName().toLowerCase().contains(search.toLowerCase()) ||
                                    p.getStoreName().toLowerCase().contains(search.toLowerCase()) ||
                                    p.getDescription().toLowerCase().contains(search.toLowerCase())) {
                                System.out.printf("%s - %s - %.2f\n", p.getName(), p.getStoreName(), p.getPrice());
                            }
                        } else {
                            System.out.printf("%s - %s - %.2f\n", p.getName(), p.getStoreName(), p.getPrice());
                        }
                    }
                } else if (searching) {
                    if (p.getName().toLowerCase().contains(search.toLowerCase()) ||
                            p.getStoreName().toLowerCase().contains(search.toLowerCase()) ||
                                    p.getDescription().toLowerCase().contains(search.toLowerCase())) {
                        if (sale) {
                            if (p.isOnSale()) {
                                System.out.printf("%s - %s - %.2f\n", p.getName(), p.getStoreName(), p.getPrice());
                            }
                        } else {
                            System.out.printf("%s - %s - %.2f\n", p.getName(), p.getStoreName(), p.getPrice());
                        }
                    }
                } else {
                    System.out.printf("%s - %s - %.2f\n", p.getName(), p.getStoreName(), p.getPrice());
                }
            }
            System.out.println("====================");
            r.close();
        } catch (Exception e) {
            System.out.println("There was an error displaying the market page!");
        }
    }
}
