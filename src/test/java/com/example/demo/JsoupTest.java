package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.platform.commons.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author Administrator
 * @date 2020-07-01 15:54
 */
public class JsoupTest {

    // 模拟浏览器请求
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36";
    private static final String EXIST = "123";
    private static final String HTTP = "http";
    private static final String HREF = "href";
    private static final String CONTENT = "content";
    private static final String CSS_QUERY_A = "div[class = bottem1] > a[href]";
    private static final String CSS_QUERY_BOOK_NAME = "div.bookname > h1";
    private static final String NEXT = "下一章";
    private static final String PATH = "D:/Text/url.txt";

    public static void main(String[] args) throws IOException {
        String name = "";
//         name = "修仙从沙漠开始";
//         name = "沈氏家族崛起";
//         name = "太丘之上";
//         name = "牧龙师";
//         name = "大周仙吏";
//         name = "大丰打更人";
//         name = "种田系修仙";
//         name = "荒岛求生之逃出生天";
//         name = "修神外传";
//        name = "人魔之路";
//        name = "沧元图";
//        name = "万族之劫";
        String read = read(name, "");
        int index = read.lastIndexOf("/") + 1;
        String preUrl = read.substring(0, index);
        String suffixUrl = read.substring(index);
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;
        while (scanner.hasNext()) {
            String str = scanner.next();
            if (str.equals(EXIST)) {
                break;
            } else {
                String url;
                if (suffixUrl.startsWith(HTTP)) {
                    url = suffixUrl;
                } else {
                    url = preUrl + suffixUrl;
                }
                Document document = connect(url);
                if (null == document) {
                    break;
                }
                Element body = document.body();
                // 下一章的地址
                Elements aTag = body.select(CSS_QUERY_A);
                for (Element a : aTag) {
                    if (a.text().equals(NEXT)) {
                        suffixUrl = a.attr(HREF);
                        if (flag) {
                            int i = findChar(read) - findChar(suffixUrl) + 1;
                            preUrl = subUrl(read, i);
                            flag = false;
                        }
                        break;
                    }
                }
                Elements bookName = document.select(CSS_QUERY_BOOK_NAME);
                String title = bookName.text().trim();
                System.err.println(title);
                Element content = body.getElementById(CONTENT);
                if (null == content) {
                    break;
                }
                System.out.println((" " + content.text()).replaceAll(" ", "\n  ") + "\n" + title);
                write(url, name);
            }
        }
        System.out.println("已是最新章节啦!");
    }


    private static Document connect(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .timeout(500000)
                .get();
    }

    private static String read(String name, String newUrl) throws IOException {
        if (StringUtils.isNotBlank(newUrl)) {
            return newUrl;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(PATH), StandardCharsets.UTF_8))) {
            String url;
            while (StringUtils.isNotBlank(url = reader.readLine())) {
                if (url.startsWith(name)) {
                    return url.split("-")[1];
                }
            }
            throw new IOException("没有匹配到地址!");
        }
    }

    private static void write(String url, String name) throws IOException {
        StringBuilder text = new StringBuilder();
        boolean hasUrlFlag = true;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(PATH), StandardCharsets.UTF_8))) {
            if (StringUtils.isNotBlank(url)) {
                String line;
                while (StringUtils.isNotBlank(line = reader.readLine())) {
                    if (line.startsWith(name)) {
                        text.append(name).append("-").append(url).append("\n");
                        if (hasUrlFlag) {
                            hasUrlFlag = false;
                        }
                    } else {
                        text.append(line).append("\n");
                    }
                }
            }
        }
        if (hasUrlFlag) {
            text.append(name).append("-").append(url).append("\n");
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\Text\\url.txt"), StandardCharsets.UTF_8))) {
            writer.write(text.toString());
        }
    }

    private static int findChar(String url) {
        int total = 0;
        for (char c : url.toCharArray()) {
            if (c == '/') {
                total++;
            }
        }
        return total;
    }

    private static String subUrl(String url, int i) {
        int total = 0;
        int sub = 0;
        char[] array = url.toCharArray();
        for (int index = 0; index < array.length; index++) {
            if (array[index] == '/' && ++total == i) {
                sub = index;
                break;
            }
        }
        return url.substring(0, sub);
    }

}
