package Utils;

import java.util.AbstractList;
import java.util.List;

public class Partition<T> extends AbstractList<List<T>> {
    final List<T> list;
    final int n;
    final int eachSize;
    final int remainder;

    Partition(List<T> list, int n) {
      this.list = list;
      this.n = n;
      this.eachSize = list.size() / n;
      this.remainder = list.size() % n;
    }

    @Override
    public List<T> get(int index) {
        boolean overRemainder = (index + 1) > this.remainder;
        int start = overRemainder ? this.eachSize * index + this.remainder : this.eachSize * index + (index);
        int end = start + (overRemainder ? this.eachSize : (this.eachSize + 1));
        return list.subList(start, end);
    }

    @Override
    public int size() {
        return n;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    public static <T> List<List<T>> partition(List<T> list, int n) {
        if (list.size() < n) {
            throw new RuntimeException();
        }

        return new Partition<T>(list, n);
    }
}
