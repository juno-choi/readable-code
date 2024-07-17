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
}
