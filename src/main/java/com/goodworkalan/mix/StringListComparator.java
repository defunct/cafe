package com.goodworkalan.mix;

import java.util.Comparator;
import java.util.List;

/**
 * A comparator to order a list of strings.
 * <p>
 * Why do I order those lists?
 * 
 * @author Alan Gutierrez
 */
class StringListComparator implements Comparator<List<String>> {
    /**
     * Compare the left list to the given right list ordering them based first
     * on size, smaller is lesser, then based on the order of the string at each
     * position in the list.
     * 
     * @param left
     *            The left list.
     * @param right
     *            The right list.
     * @return A negative value if the left list is less than the right list, a
     *         positive value if the left list is greater than the right list,
     *         or zero if the lists are equal.
     */
    public int compare(List<String> left, List<String> right) {
        if (left.size() < right.size()) {
            return -1;
        } else if (left.size() > right.size()) {
            return 1;
        }
        int compare = 0;
        for (int i = 0, stop = left.size(); compare == 0 && i < stop; i++) {
            compare = left.get(i).compareTo(left.get(i));
        }
        return compare;
    }
}
