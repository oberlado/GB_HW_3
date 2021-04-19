package les1;
//Есть классы Fruit -> Apple, Orange;(больше фруктов не надо)
public class Apple extends Fruit {

    Apple() {
        super(1.0f);
    }

    @Override
    public String toString(){
        return "яблоки";
    }
}