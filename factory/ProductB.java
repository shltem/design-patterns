

public class ProductB implements Product {
    Integer num;

    public ProductB(Integer num) {
        this.num = num;
    }

    @Override
    public void doStuff() {
        System.out.println("do stuff ProductB " + num);
    }
}
