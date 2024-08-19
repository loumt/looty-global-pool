package cn.looty.example.生成PPTX.utils;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname FileUtils
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/15 0:42
 */
public class FileUtils {


    public static List<String> findByName(String dir, String fileNameToFind){
        // 指定要搜索的文件夹路径
        Path folderPath = Paths.get(dir);

        // 查找文件
        List<Path> matchingFiles = findFiles(folderPath, fileNameToFind);

        // 输出结果
        if (!matchingFiles.isEmpty()) {
            System.out.println("Found files:");
            for (Path path : matchingFiles) {
                System.out.println(path);
            }
        } else {
            System.out.println("No matching files found.");
        }

        return matchingFiles.stream().map(Path::toString).collect(Collectors.toList());
    }

    private static List<Path> findFiles(Path folderPath, String fileName) {
        try {
            // 列出目录下的所有文件和子目录
            List<Path> allFiles = Files.walk(folderPath).filter(Files::isRegularFile).collect(Collectors.toList());
            // 过滤出具有指定名称的文件
            return allFiles.stream().filter(file -> file.getFileName().toString().equals(fileName)).collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error reading directory: " + e.getMessage());
            return Lists.newArrayList(); // 返回空列表
        }
    }
}
