package com.example.service;

import com.example.entity.Recharge;
import com.example.entity.User;
import com.example.mapper.RechargeMapper;
import com.example.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 充值业务处理
 */
@Service
public class RechargeService {

    @Resource
    private RechargeMapper rechargeMapper;

    @Resource
    private UserMapper userMapper;

    @Transactional
    public void add(Recharge recharge) {
        rechargeMapper.insert(recharge);
        // 充值后更新用户余额
        if (recharge.getUserId() != null && recharge.getMoney() != null) {
            User user = userMapper.selectById(recharge.getUserId());
            if (user != null) {
                BigDecimal current = user.getAccount() != null ? user.getAccount() : BigDecimal.ZERO;
                user.setAccount(current.add(recharge.getMoney()));
                userMapper.updateById(user);
            }
        }
    }

    public void deleteById(Integer id) {
        rechargeMapper.deleteById(id);
    }

    public void updateById(Recharge recharge) {
        rechargeMapper.updateById(recharge);
    }

    public Recharge selectById(Integer id) {
        return rechargeMapper.selectById(id);
    }

    public List<Recharge> selectAll(Recharge recharge) {
        return rechargeMapper.selectAll(recharge);
    }

    public PageInfo<Recharge> selectPage(Recharge recharge, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Recharge> list = rechargeMapper.selectAll(recharge);
        return PageInfo.of(list);
    }

}
