public abstract class Product {
    protected String productName;

    public Product(String productName) {
        this.productName = productName;
    }

    public abstract String getDetails();

    public String getProductName() {
        return productName;
    }
}