package com.yyz.Yangtze_MANAGER.mapper;

import com.yyz.Yangtze_MANAGER.entity.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/18 9:24
 */
public interface ProviderMapper {
    int getTotalCount(@Param("queryProCode") String queryProCode, @Param("queryProName") String queryProName);

    List<Provider> getProList(@Param("proName") String proName, @Param("proCode") String proCode, int currentPageNo, int pageSize);

    /**
     * 添加供应商
     */
    int add(Provider provider);

    Provider getProviderById(int id);

    int modify(Provider provider);

    int deleteProviderById(@Param("id") int id);

    List<Provider> getProviderList();
}
