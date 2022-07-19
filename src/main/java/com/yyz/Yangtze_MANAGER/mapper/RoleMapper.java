package com.yyz.Yangtze_MANAGER.mapper;

import com.yyz.Yangtze_MANAGER.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/16 9:39
 */
public interface RoleMapper {
    List<Role> getRoleList();

    List<Role> getAllRoleList();

    /**
     * 根据RoleCode
     * @param roleCode
     * @return
     */
    Role getRoelById(@Param("roleCode") String roleCode);

    int addRoel(Role role);

    Role getRoleById2(int id);

    /**
     * 更新角色
     * @param role
     * @return
     */
    int updateRoel(Role role);

    int deleteRoleById(int id);
}
