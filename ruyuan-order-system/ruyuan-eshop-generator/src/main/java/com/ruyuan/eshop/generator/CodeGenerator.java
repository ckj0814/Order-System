package com.ruyuan.eshop.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * 执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public class CodeGenerator {

    private final static String author = "zhonghuashishan";

    /**
     * mapper后缀名
     */
    private final static String mapperSuffix = "Mapper";
    private final static String mappers = "mappers";
    private final static String mapperPackageName = "dao";
    /**
     * 模块包名
     */
    private final static String module = "com.ruyuan.eshop";

    /**
     * 数据库链接配置
     */
    private final static String jdbcUrl = "jdbc:mysql://47.102.157.169:3306/ruyuan_eshop_address?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
    private final static String jdbcUserName = "root";
    private final static String jdbcPwd = "root";
    private final static String moduleName = "address";


    /**
     * 读取控制台内容
     *
     * @param tip
     * @return
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir") + "/ruyuan-eshop-generator";
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setIdType(IdType.ASSIGN_ID);
        gc.setAuthor(author);
        gc.setOpen(false);
        gc.setEnableCache(false);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        gc.setMapperName("%s" + mapperSuffix);
//        gc.setServiceName(null);
        gc.setServiceImplName("%sService");
        gc.setFileOverride(true);
        //开启 BaseResultMap 默认值：false
//        gc.setBaseResultMap(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(jdbcUrl);
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(jdbcUserName);
        dsc.setPassword(jdbcPwd);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        // pc.setModuleName(scanner("模块名"));
        pc.setParent(module);
        pc.setMapper(mapperPackageName + "." + moduleName);
        mpg.setPackageInfo(pc);
        mpg.setPackageInfo(pc);
        pc.setEntity("model" + "." + "entity" + "." + moduleName);
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/" + mappers + "/" + moduleName
                        + "/" + tableInfo.getEntityName() + mapperSuffix + StringPool.DOT_XML;

            }
        });

        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        //【实体】是否为lombok模型（默认 false）
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
//        strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
        // 写于父类中的公共字段
        strategy.setEntityBuilderModel(true);
        // strategy.setSuperEntityColumns("id");
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}