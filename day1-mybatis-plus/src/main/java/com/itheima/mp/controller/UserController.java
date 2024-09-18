package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户管理接口")
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @Operation(description = "新增用户接口")
    @PostMapping
    public void saveUser(@RequestBody UserFormDTO userDTO) {
        var user = BeanUtil.copyProperties(userDTO, User.class);
        userService.save(user);
    }

    @Operation(description = "删除用户接口")
    @DeleteMapping("{id}")
    public void deleteUserById(@Parameter(description = "用户id") @PathVariable("id") Long id) {
        userService.removeById(id);
    }

    @Operation(description = "根据id查询用户接口")
    @GetMapping("{id}")
    public UserVO queryUserById(@Parameter(description = "用户id") @PathVariable("id") Long id) {
        var user = userService.getById(id);
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @Operation(description = "根据id批量查询用户接口")
    @GetMapping
    public List<UserVO> queryUsersByIds(@Parameter(description = "用户id集合") @RequestParam("ids") List<Long> ids) {
        var users = userService.listByIds(ids);
        return BeanUtil.copyToList(users, UserVO.class);
    }

    @Operation(description = "扣减用户余额接口")
    @DeleteMapping("/{id}/deduction/{money}")
    public void deductMoneyById(
            @Parameter(description = "用户id") @PathVariable("id") Long id,
            @Parameter(description = "扣减的金额") @PathVariable("money") Integer money
    ) {
        userService.deductBalance(id,money);
    }

    @Operation(description = "根据复杂条件查询用户接口")
    @GetMapping("/list")
    public List<UserVO> queryUsers(@Parameter(description = "用户id集合") UserQuery query) {
        var users = userService.queryUsers(
                query.getName(),
                query.getStatus(),
                query.getMinBalance(),
                query.getMaxBalance()
        );
        return BeanUtil.copyToList(users, UserVO.class);
    }

    @Operation(description = "根据条件分页查询用户接口")
    @GetMapping("/page")
    public PageDTO<UserVO> queryUsersPage(@Parameter(description = "用户id集合") UserQuery query) {
        return userService.queryUsersPage(query);
    }
}
