package com.yyz.Yangtze_MANAGER.service;

import com.yyz.Yangtze_MANAGER.entity.Role;

import java.util.List;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/16 9:39
 */
public interface RoleService {
    /**
     * 查询所有的角色信息
     * @return
     */
    List<Role> getRoleList();

    /**
     * 查询角色表所有信息
     */
    List<Role> getAllRoleList();

    /**
     * 校验角色
     * @param roleCode
     * @return
     */
    Role getRoleById(String roleCode);

    /**
     * 添加角色
     * @param role
     */
    boolean addRole(Role role);

    Role getRoleById2(int id);

    /**
     * 修改角色
     * @param role
     * @return
     */
    boolean updateRole(Role role);

    boolean deleteRoleById(int id);
}
