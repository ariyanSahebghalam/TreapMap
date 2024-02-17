import java.util.Map;

public class MapDriver {
    public static void main(String[] args) {
        //List<Integer> treapMap = new TreapList<>();
        Map<Integer, Integer> treapMap = new TreapMap<Integer, Integer>();
        treapMap.put(4, 4);
        treapMap.put(4, 5);
        treapMap.put(2, 2);
        treapMap.put(7, 7);
        treapMap.put(3, 3);
        treapMap.put(6, 6);
        treapMap.put(5, 5);
        treapMap.put(8, 8);
        treapMap.put(1, 1);
        treapMap.put(11, 11);
        treapMap.put(10, 10);
        treapMap.put(12, 12);
        treapMap.put(13, 13);
        treapMap.put(14, 14);
        treapMap.put(15, 15);
        treapMap.put(9, 9);
        //treapMap.addAll(treapMap);
        System.out.println("Values added:");
        System.out.println(treapMap);
        
        System.out.println("Map get(1):");
        System.out.println(treapMap.get(1));

        treapMap.remove(9);
        System.out.println("Values removed:");
        System.out.println(treapMap);

        System.out.println(treapMap.keySet());

    }
}
