
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsModule {

    public void browseStores(String sortBy, Customer customer) {
        ArrayList<Store> allStoreList = getAllStoreList();
        Comparator<Store> comparator = null;
        if (sortBy != null) {
            switch (sortBy) {
                case "sales":
                    // todo 按销量排序, 存疑
                    comparator = Comparator.comparing(store -> store.getProductsSold().size());
                    break;
                case "purchased":
                    // 按顾客购买量排序
                    ArrayList<PurchaseHistory> purchaseHistories = customer.viewPurchaseHistory();
                    Map<String, List<PurchaseHistory>> storeToHistories = purchaseHistories.stream().collect(Collectors.groupingBy(PurchaseHistory::getStoreName));
                    comparator = Comparator.comparing(store -> {
                        List<PurchaseHistory> historyList = storeToHistories.get(store.getName());
                        int purchased = 0;
                        if (historyList != null) {
                            for (PurchaseHistory purchaseHistory : historyList) {
                                purchased += purchaseHistory.getQuantity();
                            }
                        }
                        return purchased;
                    });
                    break;
            }
        }
    }

    private ArrayList<Store> getAllStoreList() {
        // TODO: 2023/4/9  获得store列表, 待实现
        return null;
    }
}
