package com.yyz.Yangtze_MANAGER.mapper;

import com.yyz.Yangtze_MANAGER.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/15 9:16
 */
public interface UserMapper {

    /**
     * 用户登录
     * @param userCode
     * @param userPassword
     * @return 用户对象
     */
    User login(@Param("userCode") String userCode,@Param("userPassword") String userPassword);

    /**
     * 查询记录数量
     * @param queryname
     * @param queryUserRole
     * @return
     */
    int getTotalCount(@Param("queryname") String queryname,@Param("userRole") Integer queryUserRole);

    /**
     * 查询用户列表
     * @param queryname
     * @param queryUserRole
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    List<User> getUserList(@Param("queryname") String queryname,
                           @Param("queryUserRole") Integer queryUserRole,
                           @Param("currentPageNo") int currentPageNo,
                           @Param("pageSize") int pageSize);

    /**
     *
     * @param user
     * @return
     */
    int add(User user);

    User selectUserCodeExist(String userCode);

    User getUserById(@Param("id") int id);

    int modify(User user);

    /**
     * 删除用户
     * @param id
     * @return
     */
    int deleteById(@Param("id") int id);

    int updatePwd(@Param("id") Integer id, @Param("newpassword") String newpassword);
}
