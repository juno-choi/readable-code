package juno;

import java.util.*;

public class Scribbling {
    public static void main(String[] args) {
        List<PocketMon> myPocketMonList = new ArrayList<>();
        List<PocketMon> pocketMonList = createPickupPocketMonList();
        if (pocketMonList.size() < 0) {
            throw new RuntimeException("포켓몬 리스트가 피어 있습니다.");
        }
        PocketMon pickupPocketMon = randmonPickupFrom(pocketMonList);
        String resultText = pocketMonAddResultText(myPocketMonList, pickupPocketMon);
        System.out.println(resultText);
    }

    private static String pocketMonAddResultText(List<PocketMon> myPocketMonList, PocketMon pickupPocketMon) {
        return isAddMyPocketMonList(myPocketMonList, pickupPocketMon) ?
                String.format("%s(을)를 추가했습니다.", pickupPocketMon.getName()) : "이미 포켓몬 리스트가 가득 찼습니다.";
    }

    private static Boolean isAddMyPocketMonList(List<PocketMon> myPocketMon, PocketMon pickupPocketMon) {
        if (isSparePocketMonList(myPocketMon)) {
            myPocketMon.add(pickupPocketMon);
            return true;
        }
        return false;
    }

    private static boolean isSparePocketMonList(List<PocketMon> myPocketMon) {
        // 포켓몬은 5마리만 가져갈 수 있다.
        return myPocketMon.size() < 6;
    }

    private static PocketMon randmonPickupFrom(List<PocketMon> pocketMonList) {
        double random = Math.random() * pocketMonList.size();
        PocketMon pickupPocketMon = pocketMonList.get((int) random);
        return pickupPocketMon;
    }

    private static List<PocketMon> createPickupPocketMonList() {
        List<PocketMon> pocketMonList = new ArrayList<>();
        pocketMonList.add(new PocketMon("피카츄"));
        pocketMonList.add(new PocketMon("파이리"));
        pocketMonList.add(new PocketMon("꼬부기"));
        pocketMonList.add(new PocketMon("이상해씨"));
        return pocketMonList;
    }

    public static class PocketMon {
        private String name;

        public PocketMon(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
