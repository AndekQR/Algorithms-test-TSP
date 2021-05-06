package app.controller.algorithms.ts;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TabuList {
    private Set<Swap> tabuSwaps = new HashSet<>();

    public void addTabuSwap(Swap swap) {
        tabuSwaps.add(swap);
    }

    public boolean isTabu(Swap swap) {
//        this.printCollection(tabuSwaps);
//        System.out.println("---------");
//        printCollection(swap.getSwapSet());
//        System.out.println(tabuSwaps.contains(swap));
        return tabuSwaps.contains(swap);
    }

    private <T> void printCollection(Collection<T> collection) {
        for (T t : collection) {
            System.out.println(t.toString());
        }
    }
}
