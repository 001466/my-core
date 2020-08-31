package org.easy.tool.util;

import java.util.Random;

public class CodeGen {
    public static int create() {
        Random randdata = new Random();
        int data = randdata.nextInt(8888) + 1000;
        return data;
    }
}
