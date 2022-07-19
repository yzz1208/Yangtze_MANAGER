package com.yyz.Yangtze_MANAGER.service.impl;

import com.yyz.Yangtze_MANAGER.entity.Bill;
import com.yyz.Yangtze_MANAGER.mapper.BillMapper;
import com.yyz.Yangtze_MANAGER.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/19 9:08
 */
@Service
@Transactional
public class BillServiceImpl implements BillService {
    @Autowired(required = false)
    private BillMapper billMapper;


    @Override
    public List<Bill> getBillList(String queryProductName, Integer queryProviderId, Integer queryIsPayment, int currentPageNo, int pageSize) {
        currentPageNo = (currentPageNo - 1) * pageSize;
        return billMapper.getBillList(queryProductName,queryProviderId,queryIsPayment,currentPageNo,pageSize);
    }

    @Override
    public int getTotalCount(String queryProductName, Integer queryProviderId) {
        return billMapper.getTotalCount(queryProductName,queryProviderId);
    }

    @Override
    public boolean add(Bill bill) {
        boolean flag = false;
        if(billMapper.add(bill) > 0)
            flag = true;
        return flag;
    }

    @Override
    public boolean modify(Bill bill) {
        boolean flag = false;
        if(billMapper.modify(bill) > 0)
            flag = true;
        return flag;
    }

    @Override
    public Bill getBillById(int id) {
        return billMapper.getBillById(id);
    }

    @Override
    public boolean deleteBill(int id) {
        boolean flag = false;
        if(billMapper.deleteBill(id) > 0)
            flag = true;
        return flag;
    }
}
