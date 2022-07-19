package com.yyz.Yangtze_MANAGER.controller;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.yyz.Yangtze_MANAGER.entity.Role;
import com.yyz.Yangtze_MANAGER.entity.User;
import com.yyz.Yangtze_MANAGER.service.RoleService;
import com.yyz.Yangtze_MANAGER.service.UserService;
import com.yyz.Yangtze_MANAGER.utils.Constants;
import com.yyz.Yangtze_MANAGER.utils.PageSupport;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/16 8:47
 */
@Controller
@RequestMapping("/sys/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired(required = false)
    private RoleService roleService;

    /**
     * 获取用户列表
     *
     * @return
     */
    @RequestMapping("/list.html")
    public String getUserList(Model model,
                              @RequestParam(value = "queryname", required = false) String queryname,
                              @RequestParam(value = "queryUserRole", required = false) String queryUserRole,
                              @RequestParam(value = "pageIndex", required = false) String pageIndex) {
        // 设置初始值
        Integer _queryUserRole = null;
        // 用户列表
        List<User> userList = null;
        // 角色列表
        List<Role> roleList = null;
        // 设置页面容量
        int pageSize = Constants.pageSize;
        // 设置当前页
        int currentPageNo = 1;
        // 判断接收用户的名称
        if (queryname == null) {
            queryname = "";
        }
        // 判断角色标识
        if (queryUserRole != null && !queryUserRole.equals("")) {
            _queryUserRole = Integer.parseInt(queryUserRole);
        }
        // 设置当前页
        if (pageIndex != null) {
            try {
                currentPageNo = Integer.parseInt(pageIndex);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // 设置用户的总数量
        int totalCount = 0;
        try {
            // 根据 用户的名称，角色id查询中记录数量
            totalCount = userService.getTotalCount(queryname, _queryUserRole);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 总页数封装到分页工具类
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);// 当前页
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        // 总页数
        int totalPageCount = pageSupport.getTotalPageCount();
        // 控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        // 查询用户的数据列表
        try {
            userList = userService.getUserList(queryname, _queryUserRole, currentPageNo, pageSize);
            for (User user : userList) {
                user.setAge((int)((System.currentTimeMillis()-user.getBirthday().getTime())/1000/60/60/24/365+1));
            }
            // 查询角色的列表
            roleList = roleService.getRoleList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("userList", userList);
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryname);
        model.addAttribute("queryUserRole", queryUserRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "userlist";
    }

    /**
     * 跳转到添加页面
     *
     * @param user
     * @return
     */
    @GetMapping("/add.html")
    public String addUer(@ModelAttribute("user") User user) {
        return "useradd";
    }

    /**
     * 用户新增功能
     *
     * @return
     */
    @PostMapping("/addsave")
    public String addUserSave(User user, HttpSession session,
                              HttpServletRequest request,
                              @RequestParam(value = "attachs", required = false) MultipartFile[] attachs) {
        // 设置初始值
        String idPicPath = null;
        String workPicPath = null;
        String errorInfo = null;
        // 文件上传成功标识
        boolean flag = true;

        // 获得文件上传的真实路径
        String path = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles");

        System.out.println("==================");

        // 文件上传
        for (int i = 0; i < attachs.length; i++) {
            MultipartFile attach = attachs[i];
            if (!attach.isEmpty()) {
                if (i == 0) {
                    // 初始化错误信息
                    errorInfo = "uploadFileError";
                } else if (i == 1) {
                    errorInfo = "uploadWpError";
                }
                // 获取源文件名称
                String oldFileName = attach.getOriginalFilename();
                System.out.println("源文件名称：" + oldFileName);
                // 获取源文件后缀
                String subfix = FilenameUtils.getExtension(oldFileName);
                System.out.println("文件后缀：" + subfix);

                //设置文件的大小
                int size = 500 << 10;// 500KB
                if (attach.getSize() > size) {
                    request.setAttribute(errorInfo, "文件上传不能超过500k");
                    flag = false;
                    // 判断文件的类型
                } else if (subfix.equalsIgnoreCase("jpg") ||
                        subfix.equalsIgnoreCase("png") ||
                        subfix.equalsIgnoreCase("jpeg") ||
                        subfix.equalsIgnoreCase("pneg")) {

                    // 给文件命名唯一的名称
                    String fileName = System.currentTimeMillis() + UUID.randomUUID().toString() + "_personal.jpg";
                    System.out.println("新文件的名称：" + fileName);
                    // 创建文件
                    File targetFile = new File(path, fileName);

                    if (!targetFile.exists()) {
                        targetFile.mkdirs();
                    }
                    // 保存文件
                    try {
                        attach.transferTo(targetFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        request.setAttribute(errorInfo, "上传失败");
                        flag = false;
                    }
                    // 给工作照和证件照赋值
                    if (i == 0) {
                        idPicPath = path + File.separator + fileName;
                    }
                    if (i == 1) {
                        workPicPath = path + File.separator + fileName;
                    }
                    System.out.println("idPicPath" + idPicPath);
                    System.out.println("workPicPath" + workPicPath);
                } else {
                    // 上传图片的格式不正确
                    request.setAttribute(errorInfo, "上传图片的格式不正确");
                    flag = false;
                }
            }
        }
        // 如果文件上传成功，保存用户
        if (flag) {
            if (session.getAttribute(Constants.USER_SESSION) == null){
                // 跳转到登录
                return "redirect:/login.jsp";
            }
            user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
            // 创建时间
            user.setCreationDate(new Date());
            user.setIdPicPath(idPicPath);
            user.setWorkPicPath(workPicPath);
            try {
                if (userService.add(user))
                    // 添加成功
                    return "redirect:/sys/user/list.html";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "useradd";
    }

    /**
     * 添加用户时验证用户编码
     *
     * @param userCode
     * @return
     */
    @RequestMapping(value = "/ucexist.json", method = RequestMethod.GET)
    //@GetMapping("/ucexist.json")
    @ResponseBody //响应
    public String userCodeIsExist(@RequestParam String userCode) {
        HashMap<String, String> resultMap = new HashMap<>();
        if (StringUtils.isNullOrEmpty(userCode)) {// 判断是否为空
            resultMap.put("userCode", "null");
        } else {
            // 客户传递的userCode不为空 校验
            User user = null;
            try {
                user = userService.selectUserCodeExist(userCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (user != null) {
                resultMap.put("userCode", "exist");
            } else {
                resultMap.put("userCode", "noexist");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    /**
     * 动态获取角色数据
     *
     * @return
     */
    @ResponseBody
    @GetMapping(value = "rolelist.json", produces = {"application/json;charset=UTF-8"})
    public List<Role> getRoleList() {
        // 初始化角色数据集合
        List<Role> roleList = null;
        try {
            roleList = roleService.getRoleList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleList;
    }

    /**
     * 修改回显
     * 路径传参
     */
    @RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
    public String getUserById(@PathVariable String id, HttpServletRequest request, Model model) {
        User user = new User();
        // 使用userService 完成修改回显功能
        try {
            user = userService.getUserById(Integer.parseInt(id));
            // 判断是否有证件照
            if (user.getIdPicPath() != null && !user.getIdPicPath().equals("")) {
                String[] paths = user.getIdPicPath().split("\\" + File.separator);
                // 获取照片的名称
                user.setIdPicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
                // 图片的存储路径
                System.out.println("证件照" + user.getIdPicPath());
            }
            // 判断是否有证件照
            if (user.getWorkPicPath() != null && !user.getWorkPicPath().equals("")) {
                String[] paths = user.getWorkPicPath().split("\\" + File.separator);
                // 获取照片的名称
                user.setWorkPicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
                // 图片的存储路径
                System.out.println("证件照" + user.getWorkPicPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(user);
        return "usermodify";
    }

    /**
     * 修改用户信息
     */
    @RequestMapping(value = "/modifysave.html", method = RequestMethod.POST)
    public String modifyUser(User user, HttpSession session, HttpServletRequest request,
                             @RequestParam(value = "attachs", required = false) MultipartFile[] attachs) {
        // 设置初始值
        String idPicPath = null;
        String workPicPath = null;
        String errorInfo = null;
        // 文件上传成功标识
        boolean flag = true;

        // 获得文件上传的真实路径
        String path = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles");

        System.out.println("==================");

        if(attachs!=null) {
            // 文件上传
            for (int i = 0; i < attachs.length; i++) {
                MultipartFile attach = attachs[i];
                if (!attach.isEmpty()) {
                    if (i == 0) {
                        // 初始化错误信息
                        errorInfo = "uploadFileError";
                    } else if (i == 1) {
                        errorInfo = "uploadWpError";
                    }
                    // 获取源文件名称
                    String oldFileName = attach.getOriginalFilename();
                    System.out.println("源文件名称：" + oldFileName);
                    // 获取源文件后缀
                    String subfix = FilenameUtils.getExtension(oldFileName);
                    System.out.println("文件后缀：" + subfix);

                    //设置文件的大小
                    int size = 500 << 10;// 500KB
                    if (attach.getSize() > size) {
                        request.setAttribute(errorInfo, "文件上传不能超过500k");
                        flag = false;
                        // 判断文件的类型
                    } else if (subfix.equalsIgnoreCase("jpg") ||
                            subfix.equalsIgnoreCase("png") ||
                            subfix.equalsIgnoreCase("jpeg") ||
                            subfix.equalsIgnoreCase("pneg")) {

                        // 给文件命名唯一的名称
                        String fileName = System.currentTimeMillis() + UUID.randomUUID().toString() + "_personal.jpg";
                        System.out.println("新文件的名称：" + fileName);
                        // 创建文件
                        File targetFile = new File(path, fileName);

                        if (!targetFile.exists()) {
                            targetFile.mkdirs();
                        }
                        // 保存文件
                        try {
                            attach.transferTo(targetFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                            request.setAttribute(errorInfo, "上传失败");
                            flag = false;
                        }
                        // 给工作照和证件照赋值
                        if (i == 0) {
                            idPicPath = path + File.separator + fileName;
                        }
                        if (i == 1) {
                            workPicPath = path + File.separator + fileName;
                        }
                        System.out.println("idPicPath" + idPicPath);
                        System.out.println("workPicPath" + workPicPath);
                    } else {
                        // 上传图片的格式不正确
                        request.setAttribute(errorInfo, "上传图片的格式不正确");
                        flag = false;
                    }
                }
            }
        }
        if (flag) {
            if (session.getAttribute(Constants.USER_SESSION) == null){
                // 跳转到登录
                return "redirect:/login.jsp";
            }
            user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
            // 创建时间
            user.setCreationDate(new Date());
            user.setIdPicPath(idPicPath);
            user.setWorkPicPath(workPicPath);
        }
        try {
            if (userService.modify(user))
                // 添加成功
                return "redirect:/sys/user/list.html";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "usermodify";
    }

    /**
     * 查看用户
     *
     * @param id
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/view/{id}")
    public String view(@PathVariable String id, Model model, HttpServletRequest request) {

        User user = new User();
        try {
            // 1.调用userService方法
            user = userService.getUserById(Integer.parseInt(id));
            if (user.getIdPicPath() != null && !user.getIdPicPath().equals("")) {
                String[] paths = user.getIdPicPath().split("\\" + File.separator);
                // 获取照片的名称
                user.setIdPicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
                // 图片的存储路径
                System.out.println("证件照" + user.getIdPicPath());
            }
            // 判断是否有工作照
            if (user.getWorkPicPath() != null && !user.getWorkPicPath().equals("")) {
                String[] paths = user.getWorkPicPath().split("\\" + File.separator);
                // 获取照片的名称
                user.setWorkPicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
                // 图片的存储路径
                System.out.println("工作" + user.getWorkPicPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(user);
        return "userview";
    }

    /**
     * 删除用户
     *
     * @param id
     */
    @GetMapping("/deluser.json")
    @ResponseBody
    public Object deleteUser(@RequestParam String id) {
        HashMap<String, String> resultMap = new HashMap<>();

        // 判断传递的id是否存在
        if (StringUtils.isNullOrEmpty(id)) {
            // id不存在
            resultMap.put("delResult", "noexist");
        } else {
            try {
                // 删除用户
                if (userService.deleteUserById(Integer.parseInt(id))) {
                    resultMap.put("delResult", "true");
                } else {
                    resultMap.put("delResult", "false");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    /**
     * 验证用户是否登录失效
     * @return
     */
    @RequestMapping("/pwdmodify.html")
    public String pwdModify(HttpSession session) {
        // 判断用户是否过期
        if (session.getAttribute(Constants.USER_SESSION) == null){
            // 跳转到登录
            return "redirect:/login.jsp";
        }
        return "pwdmodify";
    }

    /**
     * 验证旧密码
     * @return
     */
    @RequestMapping("/pwdmodify.json")
    @ResponseBody
    public Object getPwdById(@RequestParam String oldpassword,HttpSession session){
        HashMap<String,String> resultMap = new HashMap<>();

        // 判断当前用户是否过期
        if (session.getAttribute(Constants.USER_SESSION) == null){
            // 跳转到登录
            resultMap.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){
            resultMap.put("result","error");
        } else {
            // 获取此账号的面膜
            String userPassword = ((User) (session.getAttribute(Constants.USER_SESSION))).getUserPassword();
            if(oldpassword.equals(userPassword)){
                resultMap.put("result","true");
            }else{
                resultMap.put("result","false");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    /**
     * 密码修改功能
     * @param newpassword
     * @param session
     * @param request
     * @return
     */
    @RequestMapping("/pwdsave.html")
    public String pwdSave(@RequestParam(value = "newpassword") String newpassword,HttpSession session,HttpServletRequest request){
        // 修改是否成功 标识
        boolean flag = false;
        // 从当前的session中拿到用户信息
        Object obj = session.getAttribute(Constants.USER_SESSION);
        if(obj!=null&&!StringUtils.isNullOrEmpty(newpassword)){
            try{
                // 调用userService 更新密码
                flag=userService.updatePwd(((User)obj).getId(),newpassword);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(flag){
            // 密码更新成功
            request.setAttribute(Constants.SYS_MESSAGE,"修改密码成功，请重新登陆！");
            session.removeAttribute(Constants.USER_SESSION);
            return "redirect:/login.jsp";
        }else{
            request.setAttribute(Constants.SYS_MESSAGE,"修改密码失败！");
        }
        return "pwdmodify";
    }

}
