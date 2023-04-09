
public class Customer extends UserBase {

    private final static String PURCHASE_HISTORY_FILE_PATH = "customer\\purchase\\history.txt";

    /**
     * 账户余额
     */
    private double balance;

    public Customer(String email, String password, String nickname, double balance) {
        super(email, nickname, password);
        this.balance = balance;
    }

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
        // todo 余额持久化, 商品库存持久化
        FilesModule.append(PurchaseHistory.createBy(this, product, quantity), PURCHASE_HISTORY_FILE_PATH);
        return true;
    }

    public void leaveMessageTo(Seller seller, String content) {
        Message.createBy(this, seller, content);
    }

    @Override
    public String getRole() {
        return "customer";
    }
}
