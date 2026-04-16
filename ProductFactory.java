public class ProductFactory {
    public static Product createProduct(String type, String name, Object attribute) {
        if ("A".equalsIgnoreCase(type)) {
            int qty = attribute instanceof Integer
                    ? (Integer) attribute
                    : Integer.parseInt(attribute.toString());
            return new ProductA(name, qty);
        } else if ("B".equalsIgnoreCase(type)) {
            String color = attribute == null ? "unknown" : attribute.toString();
            return new ProductB(name, color);
        } else {
            throw new IllegalArgumentException("Unknown product type: " + type);
        }
    }
}