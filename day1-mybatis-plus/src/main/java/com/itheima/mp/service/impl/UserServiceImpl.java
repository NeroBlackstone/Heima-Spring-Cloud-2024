package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {
    @Override
    @Transactional
    public void deductBalance(Long id, Integer money){
        var user = this.getById(id);
        if (user == null || user.getStatus() == UserStatus.FROZEN){
            throw new RuntimeException("用户状态异常");
        }
        if (user.getBalance()<money){
            throw new RuntimeException("用户余额不足");
        }
        int remainBalance = user.getBalance() - money;
        lambdaUpdate()
                .set(User::getId,remainBalance)
                .set(remainBalance==0,User::getStatus,UserStatus.FROZEN)
                .eq(User::getId,id)
                .eq(User::getBalance,user.getBalance())
                .update();
    }

    @Override
    public List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance) {
        return lambdaQuery()
                .like(name!=null,User::getUsername,name)
                .eq(status!=null,User::getStatus,status)
                .ge(minBalance!=null,User::getBalance,minBalance)
                .le(maxBalance!=null,User::getBalance,maxBalance)
                .list();
    }

    @Override
    public PageDTO<UserVO> queryUsersPage(UserQuery query) {
        var name = query.getName();
        var status = query.getStatus();

        Page<User> page = query.toMpPageDefaultSortByUpdateTimeDesc();

        var p = lambdaQuery()
                .like(name!=null,User::getUsername,name)
                .eq(status!=null,User::getStatus,status)
                .page(page);

        return PageDTO.of(p, user -> {
            var vo = BeanUtil.copyProperties(user, UserVO.class);
            var voUserName=vo.getUsername();
            vo.setUsername(voUserName.substring(0,voUserName.length()-2)+"**");
            return vo;
        });
    }
}
