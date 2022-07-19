package com.yyz.Yangtze_MANAGER.service;

import com.yyz.Yangtze_MANAGER.entity.Bill;

import java.util.List;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/19 9:07
 */
public interface BillService {
    /*
     * 查询所有订单
     * */
    List<Bill> getBillList(String queryProductName, Integer queryProviderId, Integer queryIsPayment, int currentPageNo, int pageSize);

    /*
     * 查询总数
     * */
    int getTotalCount(String queryProductName, Integer queryProviderId);


    /*
     * 添加订单
     * */
    boolean add(Bill bill);

    /*
     * 修改订单
     * */
    boolean modify(Bill bill);

    Bill getBillById(int id);

    /*
     * 删除
     * */
    boolean deleteBill(int id);
}
