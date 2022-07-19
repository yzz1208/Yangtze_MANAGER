package com.yyz.Yangtze_MANAGER.controller;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.yyz.Yangtze_MANAGER.entity.Role;
import com.yyz.Yangtze_MANAGER.entity.User;
import com.yyz.Yangtze_MANAGER.service.RoleService;
import com.yyz.Yangtze_MANAGER.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/16 9:39
 */
@Controller
@RequestMapping("/sys/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    /**
     * 获取角色列表
     * @param model
     * @return
     */
    @RequestMapping("/list.html")
    public String getRoelList(Model model){
        List<Role> roleList = roleService.getAllRoleList();
        model.addAttribute("roleList",roleList);
        return "rolelist";
    }

    /**
     * 跳转到添加页面
     *
     * @param role
     * @return
     */
    @GetMapping("/add.html")
    public String addUer(@ModelAttribute("role") Role role) {
        return "roleadd";
    }

    @PostMapping("/addsave.html")
    public String addSaveRole(Role role, HttpServletRequest request, HttpSession session){
        //user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        role.setCreationDate(new Date());
        // 判断用户是否过期
        if (session.getAttribute(Constants.USER_SESSION) == null){
            // 跳转到登录
            return "redirect:/login.jsp";
        }
        role.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        try{
            if(roleService.addRole(role)){
                return "redirect:/sys/role/list.html";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "roleadd";
    }

    /**
     * 校验roleCode
     * @param roleCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/rcexist.json", method = RequestMethod.GET)
    public String roleCoedIsExist(@RequestParam String roleCode){
        HashMap<String, String> hashMap = new HashMap<>();
        if(StringUtils.isNullOrEmpty(roleCode)){
            hashMap.put("roleCode","null");
        }else{
            Role role =null;
            try{
                role=roleService.getRoleById(roleCode);
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(roleCode);
            System.out.println(role);
            if(role==null) {
                hashMap.put("roleCode", "noexist");
            }else{
                hashMap.put("roleCode","exist");
            }
        }
        return JSONArray.toJSONString(hashMap);
    }

    /**
     * 修改回显
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/modify/{id}")
    public String getRoleById(@PathVariable String id, Model model){
        Role role = new Role();
        try{
             role = roleService.getRoleById2(Integer.parseInt(id));
        }catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute(role);
        return "rolemodify";
    }

    @RequestMapping(value = "/modifysave.html", method = RequestMethod.POST)
    public String modifyRole(Role role, HttpSession session){
        role.setCreationDate(new Date());
        if (session.getAttribute(Constants.USER_SESSION) == null){
            // 跳转到登录
            return "redirect:/login.jsp";
        }
        role.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        try{
            if(roleService.updateRole(role)){
                return "redirect:/sys/role/list.html";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "rolemodify";
    }
    @GetMapping("/delrole.json")
    @ResponseBody
    public Object deleteRoel(@RequestParam String id){
        HashMap<String, String> hashMap = new HashMap<>();
        if(StringUtils.isNullOrEmpty(id)){
            hashMap.put("delResult","notexist");
        }else{
            try{
                if(roleService.deleteRoleById(Integer.parseInt(id))){
                    hashMap.put("delResult","true");
                }else{
                    hashMap.put("delResult","false");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return JSONArray.toJSONString(hashMap);
    }
}
