package com.yyz.Yangtze_MANAGER.service;

import com.yyz.Yangtze_MANAGER.entity.Provider;

import java.util.List;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/18 9:32
 */

public interface ProviderService {
    /**
     * 查询总记录数
     * @param queryProName
     * @param queryProCode
     * @return
     */
    int getTotalCount(String queryProName, String queryProCode);

    /**
     * 查询供应商列表
     * @param proName
     * @param proCode
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    List<Provider> getProList(String proName, String proCode, int currentPageNo, int pageSize);

    boolean add(Provider provider);

    Provider getProviderById(int id);

    boolean modify(Provider provider);

    boolean deleteProviderById(int id);

    /**
     * 获取供应商名称
     * @return
     */
    List<Provider> getProviderList();
}
