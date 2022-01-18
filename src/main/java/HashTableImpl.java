import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HashTableImpl<K, V> implements HashTable<K, V> {

    private final List<Item<K, V>>[] data;
    private int size;
    private int itemsCount;

    static class Item<K, V> implements Entry<K, V> {
        private final K key;
        private V value;

        public Item(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Item{" + "key=" + key + ", value=" + value + '}';
        }
    }

    public HashTableImpl(int initialCapacity) {
        this.data = new List[initialCapacity];
    }

    public HashTableImpl() {
        this(16);
    }

    @Override
    public boolean put(K key, V value) {
        if (size() == data.length) {
            return false;
        }

        int index = hashFunc(key);
        int n = 0;

        if (data[index] == null) {
            data[index] = new LinkedList<>();
            size++;
        }
        data[index].add(new Item<>(key, value));
        itemsCount++;

        return true;
    }

    private int getStepDoubleHash(K key) {
        return 5 - (key.hashCode() % 5);
    }

    private int getStepQuadratic(int n) {
        return (int)Math.pow(n, 2);
    }

    private int getStepLinear() {
        return 1;
    }

    private boolean isKeysEquals(Item<K, V> item, K key) {
        return (item.getKey() == null) ? (key == null) : item.getKey().equals(key);
    }

    private int hashFunc(K key) {
        return Math.abs(key.hashCode() % data.length);
    }

    @Override
    public V get(K key) {
        int index = indexOf(key);

        if (index == -1) {
            return null;
        }

        V value = null;
        for (Item<K, V> item : data[index]) {
            if (isKeysEquals(item, key)) {
                value =  item.getValue();
            }
        }
        return value;
    }

    private int indexOf(K key) {
        return hashFunc(key);
    }

    @Override
    public V remove(K key) {
        int index = indexOf(key);

        if (index == -1) {
            return null;
        }

        V value = null;
        Item<K, V> i = null;
        for (Item<K, V> item : data[index]) {
            if (isKeysEquals(item, key)) {
                i = item;
                break;
            }
        }
        if (i != null) {
            data[index].remove(i);
            value = i.getValue();
            itemsCount--;
        }
        if (data[index].isEmpty()) {
            size--;
        }

        return value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int count() {
        return itemsCount;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void display() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("------------------------\n");
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
                for (Item<K, V> item : data[i]) {
                    sb.append(String.format("%s = [%s]%n", i, item));
                }
            }
        }
        sb.append("------------------------\n");
        return sb.toString();
    }
}