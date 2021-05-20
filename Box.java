package leshome;
import java.util.ArrayList;
import java.util.List;
public class Box <T extends Fruit>{
    private  List<T> container;

    public Box (){
        this.container = new ArrayList<>();
    }

    public float getWeight() {
        float s = 0.0f;
        for (T fruit : container) {
            s += fruit.getWeight();
        }
        return s;
    }

    public boolean sameAvg (Box<?> another){
        return Math.abs(this.getWeight()-another.getWeight())<0.001;
    }

    boolean compare(Box<? extends Fruit> box) {
        return this.getWeight() == box.getWeight();
    }

    public void trans(Box<? super T> another){
        if (another == this){
            return;
        }
        another.container.addAll(this.container);
        this.container.clear();
    }
    public void add(T fruit){
        container.add(fruit);
    }
}