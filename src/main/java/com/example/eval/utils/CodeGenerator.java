package com.example.eval.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

/**
 * mp代码生成器
 * by luo
 * @since 2022-10-20
 */
public class CodeGenerator {

    static String proj_dir = System.getProperty("user.dir");

    public static void main(String[] args) {
        generate();
    }

    private static void generate() {
        FastAutoGenerator.create("jdbc:mysql://101.37.246.72:3306/eval?serverTimezone=GMT%2B8", "root", "jinNIU123456!")
                .globalConfig(builder -> {
                    builder.author("luo") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(proj_dir + "/src/main/java/"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.example.eval") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, proj_dir + "/src/main/resources/mapper/")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();//启用lombok
                    builder.mapperBuilder().enableMapperAnnotation();//为mapper添加@Mapper注解
                    builder.controllerBuilder().enableHyphenStyle()//开启驼峰转连字符
                            .enableRestStyle();//为Controller添加@RestController注解
                    builder.addInclude("details") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
