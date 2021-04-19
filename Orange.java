package les1;
//Есть классы Fruit -> Apple, Orange;(больше фруктов не надо)
public class Orange extends Fruit {

    Orange() {
        super(1.5f);
    }
    @Override
    public String toString(){
        return "апельсины";
    }

}
