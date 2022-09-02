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
import java.util.stream.Collectors;

@Controller
@RequestMapping()
public class PageRoutine {

    private String startPath;
    private final Path lowestPath;

    PageRoutine(){
//        startPath = "C:/";
        startPath = "/home/ubuntu/www/ppt/pptFont/build/ppts";
        lowestPath = Paths.get(startPath);
    }

    @ResponseBody
    @GetMapping
    RespondFormat<List<Pair<String, String>>> getDirectoriesAndPPTNames(@RequestParam(required = false) String path){
        Path root = Paths.get(startPath);
        if(!Objects.isNull(path)){
            root = root.resolve(path);
        }
        if(!root.startsWith(lowestPath)){
            return new RespondFormat<>(false,null,"already lowest layer");
        }

        File file = root.toFile();
        if(!file.exists()){
            return new RespondFormat<>(false,null,"file or directory doesn't exist");
        }else {
            File[] files = file.listFiles();
            assert files != null;
            return new RespondFormat<>(Arrays.stream(files).filter(fileOrDir ->
                    fileOrDir.isDirectory() || (fileOrDir.isFile() && fileOrDir.getName().endsWith(".html"))
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
