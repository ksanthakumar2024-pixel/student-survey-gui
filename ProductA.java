public class ProductA extends Product {
    private int quantity;

    public ProductA(String productName, int quantity) {
        super(productName);
        this.quantity = quantity;
    }

    @Override
    public String getDetails() {
        return "ProductA{name=" + productName + ", quantity=" + quantity + "}";
    }
}