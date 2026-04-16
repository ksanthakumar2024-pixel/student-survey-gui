public class ProductB extends Product {
    private String color;

    public ProductB(String productName, String color) {
        super(productName);
        this.color = color;
    }

    @Override
    public String getDetails() {
        return "ProductB{name=" + productName + ", color=" + color + "}";
    }
}