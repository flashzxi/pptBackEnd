package cn.flashzxi.pptbackend.controller;

import cn.flashzxi.pptbackend.util.Pair;
import cn.flashzxi.pptbackend.util.RespondFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

@Controller
@RequestMapping()
public class PageRoutine {

    private String startPath;
    private final Path lowestPath;

    PageRoutine(){
//        startPath = "C:/maven";
        startPath = "/home/ubuntu/www/ppt/pptFont/build/ppts";
        lowestPath = Paths.get(startPath);
    }

    private String normalize(String path){
        path = startPath + "/" + path;
        String[] split = path.split("/");
        Stack<String> pathStack = new Stack<>();
        for (String s : split) {
            if (s.equals(".")) {
            } else if (s.equals("..")) {
                pathStack.pop();
            } else {
                pathStack.push(s);
            }
        }
        List<String> collect = new ArrayList<>(pathStack);
        if(collect.size()!=0){
            StringBuilder absolutePath = new StringBuilder();
            for(String s: collect){
                absolutePath.append("/").append(s);
            }
            return absolutePath.toString().substring(1);
        }else{
            return "noExistPath";
        }
    }

    @ResponseBody
    @GetMapping
    RespondFormat<List<Pair<String, String>>> getDirectoriesAndPPTNames(@RequestParam(required = false) String path){
        Path root = Paths.get(startPath);
        path = normalize(path);
        root = root.resolve(path);
        if(!root.startsWith(lowestPath)){
            return new RespondFormat<>(false,null,"already lowest layer");
        }

        File file = root.toFile();
        if(!file.exists() || file.isHidden()){
            return new RespondFormat<>(false,null,"file or directory doesn't exist");
        }else {
            File[] files = file.listFiles();
            assert files != null;
            return new RespondFormat<>(Arrays.stream(files).filter(fileOrDir ->
                    (fileOrDir.isDirectory() && !fileOrDir.isHidden())
                            || (fileOrDir.isFile() && fileOrDir.getName().endsWith(".html"))
            ).map(fileOrDir -> {
                if (fileOrDir.isDirectory()) {
                    return new Pair<>(fileOrDir.getName(), "dir");
                } else {
                    return new Pair<>(fileOrDir.getName(), "file");
                }
            }).collect(Collectors.toList()));
        }
    }

//    @ResponseBody
    @GetMapping("/jump")
    String jumpTo(@RequestParam String url){
        return "forward:" + url;
    }
}
