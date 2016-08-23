package Math;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Hakim on 28-Jul-16.
 *
 * @author by Hakim
 *         <p/>
 *         Please update the author field if you are editing
 *         this file and your name is not written.
 */
public class Permutation {

    private int[] array;
    private int firstNum;
    private boolean firstReady = false;

    public Permutation(int[] array, int firstNum) {
        this.array = array;
        this.firstNum = firstNum;
    }

    public boolean hasMore() {
        boolean end = firstReady;
        for (int i = 1; i < array.length; i++) {
            end = end && array[i] < array[i-1];
        }
        return !end;
    }

    public int[] getNext() {

        if (!firstReady) {
            firstReady = true;
            return array;
        }

        int temp;
        int j = array.length - 2;
        int k = array.length - 1;

        // Find largest index j with a[j] < a[j+1]

        for (;array[j] > array[j+1]; j--);

        // Find index k such that a[k] is smallest integer
        // greater than a[j] to the right of a[j]

        for (;array[j] > array[k]; k--);

        // Interchange a[j] and a[k]

        temp = array[k];
        array[k] = array[j];
        array[j] = temp;

        // Put tail end of permutation after jth position in increasing order

        int r = array.length - 1;
        int s = j + 1;

        while (r > s) {
            temp = array[s];
            array[s++] = array[r];
            array[r--] = temp;
        }

        return array;
    } // getNext()

    public <T> Set<Set<T>> powerSet(Set<T> originalSet){
        Set<Set<T>> sets = new HashSet<Set<T>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<T>());
            return sets;
        }
        List<T> list = new ArrayList<T>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
        for (Set<T> set : powerSet(rest)) {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }
}
