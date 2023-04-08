package Week12.Project4;

//Sellers can create, edit, or delete products associated with their stores.
//Sellers can view a list of their sales by store, including customer information and revenues from the sale.

import java.util.ArrayList;

public class Seller {
    private String productName;
    private String storeName;
    private String description;
    private int quantityAvail;
    private int price;
    private ArrayList<String> productList;
    public Seller (String productName, String storeName, String description, int quantityAvail,
                        int price){
        this.productName = productName;
        productList = new ArrayList<>();
        this.productList.add(productName);
        this.storeName = storeName;
        this.description = description;
        this.quantityAvail = quantityAvail;
        this.price = price;
    }
    public String getProductName(){
        return productName;
    }
    public String getStoreName(){
        return storeName;
    }
    public String getDescription(){
        return description;
    }
    public int getQuantityAvail(){
        return quantityAvail;
    }
    public int getPrice(){
        return price;
    }
    public void setProductName(String productName){
        this.productName = productName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantityAvail(int quantityAvail) {
        this.quantityAvail = quantityAvail;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public void deleteProduct(ArrayList<String> productList, String productName){
        productList.remove(productName);
        this.storeName = null;
        this.description = null;
        this.quantityAvail = 0;
        this.price = 0;
    }

}
