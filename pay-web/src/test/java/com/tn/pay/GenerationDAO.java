package com.tn.pay;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * dao层生成
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration(initializers = {TestContextInitializer.class})
public class GenerationDAO {

    @Value("${spring.datasource.druid.url}")
    private String url;
    @Value("${spring.datasource.druid.driverClassName}")
    private String driverName;
    @Value("${spring.datasource.druid.username}")
    private String username;
    @Value("${spring.datasource.druid.password}")
    private String password;

    @Value("${tn.dao.packageRoot}")
    private String packageRoot;
    @Value("${tn.dao.packageParent}")
    private String packageParent;
    @Value("${tn.dao.entity}")
    private String entity;
    @Value("${tn.dao.mapper}")
    private String mapper;
    @Value("${tn.dao.tablePrefix}")
    private String[] tablePrefix;
    @Value("${tn.dao.exclude}")
    private String[] exclude;

    @Test
    public void generateDAO() throws IOException {
        File directory = new File("");
        String path = directory.getCanonicalPath();
        String finalPath = new File(path).getParent() + "/" + packageRoot;
        System.out.println("path: " + finalPath);
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator().setDataSource(
                new DataSourceConfig()
                        .setDbType(DbType.MYSQL)// 数据库类型
                        .setTypeConvert(new MySqlTypeConvert())
                        .setUrl(url)
                        .setDriverName(driverName)
                        .setUsername(username)
                        .setPassword(password)
                        .setTypeConvert(new MySqlTypeConvert() {
                            // 自定义数据库表字段类型转换
                            @Override
                            public DbColumnType processTypeConvert(String fieldType) {
                                String t = fieldType.toLowerCase();
                                if (t.contains("datetime") || t.contains("date")) {
                                    return DbColumnType.STRING;
                                }
                                return super.processTypeConvert(fieldType);
                            }
                        })
        ).setGlobalConfig(
                new GlobalConfig()
                        .setOutputDir(finalPath + "/src/main/java")
                        .setEnableCache(false)// XML 二级缓存
                        .setFileOverride(true)// 是否覆盖文件
                        .setBaseColumnList(true)// XML columList
                        .setBaseResultMap(true)// XML ResultMap
                        .setOpen(true) // 生成完成打开路径
                        .setAuthor("Auto")
        ).setStrategy(
                new StrategyConfig()
                        .setTablePrefix(tablePrefix)// 表前缀
                        .setExclude(exclude) // 排除生成的表
                        .setNaming(NamingStrategy.underline_to_camel)// 表名生成策略
        ).setTemplate(
                new TemplateConfig()//关闭部分生成
                        .setController(null)
                        .setService(null)
                        .setServiceImpl(null)
                        .setXml(null)//自定义生成
                        .setEntity("/templates/entity.java.vm")
                        .setMapper("/templates/mapper.java.vm")
        ).setPackageInfo(
                new PackageConfig()
                        .setParent(packageParent)
                        .setEntity(entity)
                        .setMapper(mapper)
        ).setCfg(
                // 注入自定义配置
                new InjectionConfig() {
                    @Override
                    public void initMap() {
                    }
                }.setFileOutConfigList(Collections.singletonList(new FileOutConfig("/templates/mapper.xml.vm") {
                    // 自定义输出文件目录
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        return finalPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper.xml";
                    }
                }))
        );

        // 执行生成
        mpg.execute();
    }

}
