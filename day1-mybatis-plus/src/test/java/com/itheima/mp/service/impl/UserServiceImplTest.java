package com.itheima.mp.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import com.itheima.mp.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private IUserService userService;

    @Test
    void testSaveUser(){
        User user = new User();
        user.setUsername("Lilei");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo(UserInfo.of(24,"英文老师","female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userService.save(user);
    }

    @Test
    void testQuery(){
        var users = userService.listByIds(List.of(1L,2L,4L));
        users.forEach(System.out::println);
    }

    @Test
    void testPageQuery(){
        var pageNo = 1;
        var pageSize = 2;
        Page<User> page = Page.of(pageNo, pageSize);
        page.addOrder(new OrderItem().setColumn("balance").setAsc(true));
        page.addOrder(new OrderItem().setColumn("id").setAsc(true));
        var p = userService.page(page);

        long total = p.getTotal();
        System.out.println("total = "+total);
        long pages = p.getPages();
        System.out.println("pages = "+pages);

        var user=p.getRecords();
        user.forEach(System.out::println);
    }
}