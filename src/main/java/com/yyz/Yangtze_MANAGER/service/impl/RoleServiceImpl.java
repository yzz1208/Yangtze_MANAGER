package com.yyz.Yangtze_MANAGER.service.impl;

import com.yyz.Yangtze_MANAGER.entity.Role;
import com.yyz.Yangtze_MANAGER.mapper.RoleMapper;
import com.yyz.Yangtze_MANAGER.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/16 9:39
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired(required = false)
    private RoleMapper roleMapper;
    /**
     * 查询所有的角色信息
     * @return
     */
    @Override
    public List<Role> getRoleList() {
        return roleMapper.getRoleList();
    }

    @Override
    public List<Role> getAllRoleList() {
        return roleMapper.getAllRoleList();
    }

    @Override
    public Role getRoleById(String roleCode) {
        return roleMapper.getRoelById(roleCode);
    }

    @Override
    public boolean addRole(Role role) {
        boolean flag=false;
        if(roleMapper.addRoel(role)>0){
            flag=true;
        }
        return flag;
    }

    @Override
    public Role getRoleById2(int id) {
        return roleMapper.getRoleById2(id);
    }

    @Override
    public boolean updateRole(Role role) {
        boolean flag = false;
        if(roleMapper.updateRoel(role)>0){
            flag=true;
        }
        return flag;
    }

    @Override
    public boolean deleteRoleById(int id) {
        boolean flag = false;
        if(roleMapper.deleteRoleById(id)>0){
            flag=true;
        }
        return flag;
    }
}
