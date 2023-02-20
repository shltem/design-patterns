

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactoryTest {
    private static Product staticCreate (int num){
        return new ProductA(num);
    }

    private Product instanceCreate (int num){
        return new ProductB(num);
    }

    @Test
    void testAdd(){
        Factory<String, Product, Integer> factory = new Factory<>();
        factory.add("productA",(num) -> new ProductA(num));
        factory.add("productB",(num) -> new ProductB(num));
        factory.add("static", (num)-> FactoryTest.staticCreate(num));
        factory.add("instance", (num)-> instanceCreate(num));

        Product a = factory.create("productA", 6);
        Product b = factory.create("productB", 8);
        Product c = factory.create("static", 3);
        Product d = factory.create("instance", 11);
        a.doStuff();
        b.doStuff();
        c.doStuff();
        d.doStuff();
    }

}