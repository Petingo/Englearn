package com.petingo.englearn;

public class WordDetailParser {
    public static String ParserChinese(String resource){
        String result;

        result = resource.replace("n.","\n名詞 (n.):\n");
        result = result.replace("adj.","\n形容詞 (adj.):\n");
        result = result.replace("a.","\n形容詞 (adj.):\n");
        result = result.replace("adv.","\n副詞 (adv.):\n");
        result = result.replace("ad.","\n副詞 (ad.):\n");
        result = result.replace("vt.","\n及物動詞 (vt.):\n");
        result = result.replace("vi.","\n不及物動詞 (vi.):\n");
        result = result.replace("v.","\n動詞 (v.):\n");
        result = result.replace("vbl.","\n動詞變化 (vbl.):\n");
        result = result.replace("int.","\n感嘆詞 (int.):\n");
        result = result.replace("prep.","\n介繫詞 (prep.):\n");
        result = result.replace("abbr.","\n縮寫 (abbr.):\n");
        result = result.replace("conj.","\n連接詞 (prep.):\n");
        result = result.replace("art.","\n冠詞 (art.):\n");
        result = result.replace("pron.","\n代名詞 (pron.):\n");
        result = result.replace("pl.","\n複數 (pl.):\n");
        result = result.replace("num.","\n數量詞 (num.):\n");

        result = result.replace(",","，");

        return result;
    }
}
