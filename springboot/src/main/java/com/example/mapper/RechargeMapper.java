package com.example.mapper;

import com.example.entity.Recharge;
import java.util.List;

/**
 * 充值数据访问
 */
public interface RechargeMapper {

    List<Recharge> selectAll(Recharge recharge);

    Recharge selectById(Integer id);

    void insert(Recharge recharge);

    void updateById(Recharge recharge);

    void deleteById(Integer id);

}
