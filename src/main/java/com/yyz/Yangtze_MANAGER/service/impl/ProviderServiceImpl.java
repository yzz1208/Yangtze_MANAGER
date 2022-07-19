package com.yyz.Yangtze_MANAGER.service.impl;

import com.yyz.Yangtze_MANAGER.entity.Provider;
import com.yyz.Yangtze_MANAGER.mapper.ProviderMapper;
import com.yyz.Yangtze_MANAGER.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/18 9:32
 */
@Service
@Transactional
public class ProviderServiceImpl implements ProviderService {
    @Autowired(required = false)
    ProviderMapper providerMapper;

    @Override
    public int getTotalCount(String queryProName, String queryProCode) {
        return providerMapper.getTotalCount(queryProCode,queryProName);
    }

    @Override
    public List<Provider> getProList(String proName, String proCode, int currentPageNo, int pageSize) {
        // 计算分页的索引
        currentPageNo = (currentPageNo - 1) * pageSize;
        return providerMapper.getProList(proName,proCode,currentPageNo,pageSize);
    }

    @Override
    public boolean add(Provider provider) {
        boolean flag = false;
        if(providerMapper.add(provider)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public Provider getProviderById(int id) {
        return providerMapper.getProviderById(id);
    }

    @Override
    public boolean modify(Provider provider) {
        boolean flag = false;
        if(providerMapper.modify(provider)>0){
            flag=true;
        }
        return flag;
    }

    @Override
    public boolean deleteProviderById(int id) {
        boolean flag = false;
        if(providerMapper.deleteProviderById(id)>0){
            flag=true;
        }

        return flag;
    }

    @Override
    public List<Provider> getProviderList() {

        return providerMapper.getProviderList();
    }
}
