package juno;

import org.junit.jupiter.api.Test;

public class Scribbling2 {
    @Test
    void earlyReturn() {
        int count = 3;

        printHello(count);
    }

    private static void printHello(int count) {
        if (count < 0) {
            System.out.println("hello 0");
            return ;
        }
        if (count < 2) {
            System.out.println("hello 1");
            return;
        }
        System.out.println("hello 2");
    }

    @Test
    void depth() {
        int count = 3;

        // 코드 30줄...

        printHello(count);
    }

    @Test
    void depth2() {
        for (int i=0; i<30; i++) {
            printByI(i);
        }
    }

    private static void printByI(int i) {
        for (int j=0; j<10; j++) {
            if (i > 10 && j < 5) {
                System.out.println("출력");
            }
        }
    }

}
