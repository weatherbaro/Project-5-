
import java.util.ArrayList;

public class Seller extends UserBase {

    private String email
    
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

    @Override
    public String getRole() {
        return "seller";
    }
}
