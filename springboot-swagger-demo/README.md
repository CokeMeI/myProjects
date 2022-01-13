# springboot 2.6.2 + swagger 3.0

## 需要降低版本的可在根目录的pom.xml文件里面修改

swagger是一个流行的API开发框架，这个框架以“开放API声明”（OpenAPI Specification，OAS）为基础，
对整个API的开发周期都提供了相应的解决方案，是一个非常庞大的项目（包括设计、编码和测试，几乎支持所有语言）。

Swagger 是一个规范和完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务。
总体目标是使客户端和文件系统作为服务器以同样的速度来更新。
文件的方法，参数和模型紧密集成到服务器端的代码，允许API来始终保持同步。Swagger 让部署管理和使用功能强大的API从未如此简单。

**springfox大致原理:**

springfox的大致原理就是，在项目启动的过种中，spring上下文在初始化的过程，
框架自动跟据配置加载一些swagger相关的bean到当前的上下文中，并自动扫描系统中可能需要生成api文档那些类，
并生成相应的信息缓存起来。如果项目MVC控制层用的是springMvc那么会自动扫描所有Controller类，并生成对应的文档描述数据.

该数据是json格式，通过路径：项目地址/ v2/api-docs可以访问到该数据，然后swaggerUI根据这份数据生成相应的文档描述界面。
因为我们能拿到这份数据，所以我们也可以生成自己的页面.

## SpringBoot 与 Swagger2

由于java的强大的注解功能,我们使用SpringBoot来结合Swagger2,在使用起来非常简单.
由于Spring的流行，Marty Pitt编写了一个基于Spring的组件swagger-springmvc，用于将swagger集成到springmvc中来。


### 第一步: 新建SpringBoot项目,引入依赖.

```xml
<!--添加Swagger依赖 -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>
```

**上面依赖的作用:**

springfox-boot-starter这是Spring Boot的核心启动器，包含了自动配置、日志和YAML。

### 第二步:创建api

```java
package com.example.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:Dylan
 * @date: 2022/1/7
 * @time: 14:48
 * @desc: 用户控制层
 */
@Api(tags = "用户管理") //  tags：你可以当作是这个组的名字。
@RestController
public class UserController {
    // 注意，对于swagger，不要使用@RequestMapping，
    // 因为@RequestMapping支持任意请求方式，swagger会为你生成7个接口文档
    @GetMapping("/info")
    public String info(String id) {
        return "aaa";
    }
}

```

### 配置Swagger2

现在Swagger2还不能为我们生成API文档,因为我们还没有对它进行配置.

我们需要创建一个配置类,进行如下配置:

```java
package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author:Dylan
 * @date: 2022/1/7
 * @time: 14:48
 * @desc: 配置类
 */
@Configuration // 标明是配置类
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)  // DocumentationType.SWAGGER_2 固定的，代表swagger2
                .apiInfo(apiInfo()) // 用于生成API信息
                .select() // select()函数返回一个ApiSelectorBuilder实例,用来控制接口被swagger做成文档
                .apis(RequestHandlerSelectors.basePackage("com.example.controller")) // 用于指定扫描哪个包下的接口
                .paths(PathSelectors.any())// 选择所有的API,如果你想只为部分API生成文档，可以配置这里
                .build();
    }

    /**
     * 用于定义API主界面的信息，比如可以声明所有的API的总标题、描述、版本
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("XX项目API") //  可以用来自定义API的主标题
                .description("XX项目SwaggerAPI管理") // 可以用来描述整体的API
                .termsOfServiceUrl("") // 用于定义服务的域名
                .version("1.0") // 可以用来定义版本。
                .build(); //
    }
}

```

springfox为我们提供了一个Docket（摘要的意思）类，我们需要把它做成一个Bean注入到spring中，
显然，我们需要一个配置文件，并通过一种方式（显然它会是一个注解）告诉程序，这是一个Swagger配置文件。

springfox允许我们将信息组合成一个ApiInfo的类，作为构造参数传给Docket（当然也可以不构造这个类，而直接使用null，但是你的这个API就太low了）。

### 搞定

现在我们要做的配置已经能满足一个生成API文档的基本要求了,让我们启动项目,访问:http://localhost:8089/swagger-ui/


这是Swagger-ui 为我们生成的界面.

## Swagger2 注解使用

接下来我们就要好好研究下 springfox-swagger2 给我们提供的注解了.

```java
package com.example.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:Dylan
 * @date: 2022/1/7
 * @time: 14:48
 * @desc: 用户控制层
 */
@Api(tags = "用户管理") //  tags：你可以当作是这个组的名字。
@RestController
public class UserController {
    // 注意，对于swagger，不要使用@RequestMapping，
    // 因为@RequestMapping支持任意请求方式，swagger会为你生成7个接口文档
    @GetMapping("/info")
    public String info(String id) {
        return "aaa";
    }
}


```
 

## 常用注解说明:

通过上面的了解,我们大概已经会使用Swagger2 了,但我们只介绍了一些简单常用的注解,下面我们系统的总结一下:

### Swagger2 基本使用(重点加粗显示)：

* **@Api: 描述类/接口的主要用途**

* **@ApiOperation: 描述方法用途**

* **@ApiImplicitParam: 描述方法的参数**
* **@ApiImplicitParams: 描述方法的参数(Multi-Params)**
> 可以在上面 创建用户的方法上添加 `@ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")`试试看.

* **@ApiParam:请求属性**

* **@ApiIgnore: 忽略某类/方法/参数的文档**
> 注意与 `@ApiModelProperty(hidden = true)`不同, `@ApiIgnore` 不能用在模型数据上

* @ApiResponse：响应配置
> 如: `@ApiResponse(code = 400, message = "无效的用户信息")` ,注意这只是在 生成的Swagger文档上有效,不要和实际的客户端调用搞混了.
通常我们都是统一JSON返回,用不到这个注解
* @ApiResponses：响应集配置

* @ResponseHeader: 响应头设置
> 例如: @ResponseHeader(name="head1",description="response head conf")

* @ApiModelProperty:添加和操作模型属性的数据

**Spring Boot 默认“约定”从资源目录的这些子目录读取静态资源：**

        src/main/resources/META-INF/resources
        src/main/resources/static （推荐）
        src/main/resources/public

注：若不同静态目录含有相同路径图片，则按上述优先级，即META-INF/resources目录优先级最高。

## 小结:

好了，关于Swagger2在项目中的使用教程就到这里。

> 源码下载: https://github.com/CokeMeI/myProjects/tree/main/springboot-swagger-demo













