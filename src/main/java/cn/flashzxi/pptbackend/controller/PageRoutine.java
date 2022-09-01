package cn.flashzxi.pptbackend.controller;

import cn.flashzxi.pptbackend.util.Pair;
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

    private final Path lowestPath;

    PageRoutine(){
        lowestPath = Paths.get("/home/ubuntu/www/ppt/ppt");
    }

    @ResponseBody
    @GetMapping
    List<Pair<String, String>> getDirectoriesAndPPTNames(@RequestParam(required = false) String path){
        Path root = Paths.get("/home/ubuntu/www/ppt/ppt");
        if(!Objects.isNull(path)){
            root = root.resolve(path);
        }
        if(!root.startsWith(lowestPath)){
            return new ArrayList<>();
        }

        File file = root.toFile();
        if(!file.exists()){
            return new ArrayList<>();
        }else {
            File[] files = file.listFiles();
            assert files != null;
            return Arrays.stream(files).filter(fileOrDir ->
                    fileOrDir.isDirectory() || (fileOrDir.isFile() && fileOrDir.getName().endsWith(".html"))
            ).map(fileOrDir -> {
                if (fileOrDir.isDirectory()) {
                    return new Pair<>(fileOrDir.getName(), "dir");
                } else {
                    return new Pair<>(fileOrDir.getName(), "file");
                }
            }).collect(Collectors.toList());
        }
    }

//    @ResponseBody
    @GetMapping("/jump")
    String jumpTo(@RequestParam String url){
        return "forward:" + url;
    }
}
