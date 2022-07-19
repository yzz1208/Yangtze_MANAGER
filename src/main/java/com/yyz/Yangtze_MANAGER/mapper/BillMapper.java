package com.yyz.Yangtze_MANAGER.mapper;

import com.yyz.Yangtze_MANAGER.entity.Bill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/19 9:12
 */
public interface BillMapper {
    List<Bill> getBillList(@Param("queryProductName") String queryProductName,
                           @Param("queryProviderId") Integer queryProviderId,
                           @Param("queryIsPayment") Integer queryIsPayment,
                           @Param("currentPageNo") int currentPageNo,
                           @Param("pageSize") Integer pageSize);

    int getTotalCount(@Param("queryProductName") String queryProductName,@Param("queryProviderId") Integer queryProviderId);


    int add(Bill bill);

    int modify(Bill bill);

    Bill getBillById(@Param("id") int id);

    int deleteBill(@Param("id") int id);
}
