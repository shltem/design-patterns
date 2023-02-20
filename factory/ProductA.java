

public class ProductA implements Product{

    Integer num;

    public ProductA(Integer num){
        this.num = num;
    }

    @Override
    public void doStuff() {
        System.out.println("do stuff productA " + num);
    }
}
