package com.petingo.englearn;

public class WordDetailParser {
    public static String ParserChinese(String resource){
        String result;
        String[] PoS = {"vi.","vt.","n.","adj.","adv."};
        result = resource.replace(",","\n");

        return result;
    }
}
