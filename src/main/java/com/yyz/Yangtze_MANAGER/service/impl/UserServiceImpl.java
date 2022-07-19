package com.yyz.Yangtze_MANAGER.service.impl;

import com.yyz.Yangtze_MANAGER.mapper.UserMapper;
import com.yyz.Yangtze_MANAGER.entity.User;
import com.yyz.Yangtze_MANAGER.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/15 8:50
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User login(String userCode, String userPassword){
      User user =  userMapper.login(userCode,userPassword);
      if(user !=null){
          // 获取的密码与输入的密码进行比对
          if(!user.getUserPassword().equals(userPassword)){
              user=null;
          }
      }
        return user;
    }

    /**
     * 查询总记录数量
     * @param queryname
     * @param queryUserRole
     * @return
     */
    @Override
    public int getTotalCount(String queryname, Integer queryUserRole) {

        return userMapper.getTotalCount(queryname,queryUserRole);
    }

    /**
     * 查询用户数列表  分页
     * @param queryname
     * @param queryUserRole
     * @param currentPageNo  当前页
     * @param pageSize  页面容量
     * @return
     */
    @Override
    public List<User> getUserList(String queryname, Integer queryUserRole, int currentPageNo, int pageSize) {
        // 计算分页的索引
        currentPageNo = (currentPageNo - 1) * pageSize;

        return userMapper.getUserList(queryname,queryUserRole,currentPageNo,pageSize);
    }

    @Override
    public boolean add(User user) {
        boolean flag =false;
        if(userMapper.add(user)>0)
            flag=true;
        return flag;
    }

    /**
     * 验证用户编码
     * @param userCode
     * @return
     */
    @Override
    public User selectUserCodeExist(String userCode) {
        return userMapper.selectUserCodeExist(userCode);
    }

    /**
     *  修改回显
     * 通过id 获取用户信息
     * @param id
     * @return
     */
    @Override
    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    @Override
    public boolean modify(User user) {
        boolean flag = false;
        if(userMapper.modify(user)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean deleteUserById(int id) {
        // 删除标识
        boolean flag = false;
        if(userMapper.deleteById(id)>0){
            flag=true;
        }
        return flag;
    }

    @Override
    public boolean updatePwd(Integer id, String newpassword) {
        boolean flag=false;
        if(userMapper.updatePwd(id,newpassword)>0){
            flag =true;
        }
        return flag;
    }
}
