package les1;
//b. Класс Box в который можно складывать фрукты, коробки условно сортируются по типу
 //    фрукта, поэтому в одну коробку нельзя сложить и яблоки, и апельсины;
//c. Для хранения фруктов внутри коробки можете использовать ArrayList;
//d. Сделать метод getWeight() который высчитывает вес коробки, зная количество фруктов и
 //    вес одного фрукта(вес яблока - 1.0f, апельсина - 1.5f, не важно в каких это единицах);
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Box<T extends Fruit> {
    public List<T> getList() {
        return list;
    }

    private List<T> list;

    public Box(T... obj) {
        list = Arrays.asList(obj);
    }

    public Box() {
        list = new ArrayList<T>();
    }
// g. Не забываем про метод добавления фрукта в коробку.
    void add(T obj) {
        list.add(obj);
    }

    void info() {
        if (list.isEmpty()) {
            System.out.println("Коробка пуста");
        } else {
            System.out.println("В коробке находятся " + list.get(0).toString() + " в количестве: " + list.size());
        }
    }
    //d. Сделать метод getWeight() который высчитывает вес коробки, зная количество фруктов и
     //    вес одного фрукта(вес яблока - 1.0f, апельсина - 1.5f, не важно в каких это единицах);
    float getWeight() {
        if (list.isEmpty()) {
            return 0;
        } else {
            return list.size() * list.get(0).getWeight();
        }
    }
    //e. Внутри класса коробка сделать метод compare, который позволяет сравнить текущую коробку с той, которую подадут в compare в качестве параметра,
     // true - если их веса равны, false в противном случае(коробки с яблоками мы можем сравнивать с коробками с апельсинами);
    boolean compare(Box<? extends Fruit> box) {
        return this.getWeight() == box.getWeight();
    }
    // Написать метод, который позволяет пересыпать фрукты из текущей коробки в другую коробку(помним про сортировку фруктов, нельзя яблоки
    // высыпать в коробку с апельсинами), соответственно в текущей коробке фруктов не остается, а в другую перекидываются объекты, которые были в этой коробке;
    public void pour(Box<? super T> box) {
        Box<Orange> OrangeBox = new Box<Orange>();
        box.list.addAll(this.list);
        list.clear();
    }
}
