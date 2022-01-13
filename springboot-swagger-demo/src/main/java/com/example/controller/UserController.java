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
