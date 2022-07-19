package com.yyz.Yangtze_MANAGER.service;

import com.yyz.Yangtze_MANAGER.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/15 8:49
 */
public interface UserService {
    /**
     * 用户登录
     * @param userCode 用户编码
     * @param userPassword 用户密码
     * @return 用户对象
     */
    User login(String userCode, String userPassword);

    /**
     * 查询总记录数
     * @param queryname
     * @param queryUserRole
     * @return
     */
    int getTotalCount(String queryname, Integer queryUserRole);

    /**
     * 查询用户的数据列表
     * @param queryname
     * @param queryUserRole
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    List<User> getUserList(String queryname, Integer queryUserRole, int currentPageNo, int pageSize);

    boolean add(User user);

    /**
     * 验证用户编码
     * @param userCode
     * @return
     */
    User selectUserCodeExist(String userCode);

    /**
     *  修改回显
     * 通过id 获取用户信息
     * @param id
     * @return
     */
    User getUserById(int id);

    /**
     * 修改保存
     * @param user
     * @return
     */
    boolean modify(User user);

    /**
     * 删除用户
     * @param id
     * @return
     */
    boolean deleteUserById(int id);

    /**
     * 修改密码
     * @param id
     * @param newpassword
     * @return
     */
    boolean updatePwd(Integer id, String newpassword);
}

