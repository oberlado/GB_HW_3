package leshome;
import java.util.ArrayList;
import java.util.Arrays;
public class Main {
    public static void main(String[] args) {
        String[] arr = {"trt","ty","qw"};

        ArrayList<String> al = arrayToList(arr);
        System.out.println(al);

        Box<Apple> box1 = new Box<>();
        box1.add(new Apple());
    }
    public  static <T> ArrayList<T> arrayToList (T[] arr){
        return new ArrayList<>(Arrays.asList(arr));
    }
}

abstract class Fruit {
    private float weight;

    Fruit(float weight) {
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }
}

