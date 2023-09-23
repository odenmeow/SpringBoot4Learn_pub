package com.oni.training.springboot.WebCrawler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSorter {
    public static void main(String[] args) {
        File folder = new File("C:\\Users\\qw284\\Downloads\\SpringBootClassroom");
        File[] files = folder.listFiles();
        // 创建一个列表来存储文件名
        List<String> fileNames = new ArrayList<>();

        // 正则表达式用于提取文件名中的数字部分
        Pattern pattern = Pattern.compile("\\d+");

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                fileNames.add(fileName);
            }
        }

        // 使用自定义比较器按数字部分排序文件名
        Collections.sort(fileNames, new NumericFileNameComparator(pattern));

        // 输出排序后的文件名
        for (String fileName : fileNames) {
            System.out.println(fileName);
        }
    }

    // 自定义比较器，用于按数字部分排序文件名
    static class NumericFileNameComparator implements Comparator<String> {
        private Pattern pattern;

        public NumericFileNameComparator(Pattern pattern) {
            this.pattern = pattern;
        }

        @Override
        public int compare(String s1, String s2) {
            Matcher matcher1 = pattern.matcher(s1);
            Matcher matcher2 = pattern.matcher(s2);

            // 提取并比较数字部分
            while (matcher1.find() && matcher2.find()) {
                int num1 = Integer.parseInt(matcher1.group());
                int num2 = Integer.parseInt(matcher2.group());

                if (num1 != num2) {
                    return num1 - num2;
                }
            }

            // 如果数字部分相同，按照字符串比较
            return s1.compareTo(s2);
        }
    }
}