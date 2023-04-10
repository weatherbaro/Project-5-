
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
}
