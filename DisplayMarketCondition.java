import java.util.ArrayList;

public class DisplayMarketCondition {

    ArrayList<Product> products;
    String property;
    String sortBy;
    boolean sale;
    boolean searching;
    String search;

    public DisplayMarketCondition(ArrayList<Product> products, String property, String sortBy, boolean sale, boolean searching, String search) {
        this.products = products;
        this.property = property;
        this.sortBy = sortBy;
        this.sale = sale;
        this.searching = searching;
        this.search = search;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public boolean isSale() {
        return sale;
    }

    public void setSale(boolean sale) {
        this.sale = sale;
    }

    public boolean isSearching() {
        return searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
