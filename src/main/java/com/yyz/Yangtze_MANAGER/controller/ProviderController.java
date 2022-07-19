package com.yyz.Yangtze_MANAGER.controller;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.yyz.Yangtze_MANAGER.entity.Provider;
import com.yyz.Yangtze_MANAGER.entity.User;
import com.yyz.Yangtze_MANAGER.service.ProviderService;
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
 * @date 2022/7/18 9:22
 */
@Controller
@RequestMapping("/sys/provider")
public class ProviderController {

    @Autowired(required = false)
    ProviderService providerService;

    /**
     * 获取用户列表
     * @param model
     * @param queryProCode
     * @param queryProName
     * @param pageIndex
     * @return
     */
    @RequestMapping("/list.html")
    public String getProviderList(Model model,
                              @RequestParam(value = "queryProCode", required = false) String queryProCode,
                              @RequestParam(value = "queryProName", required = false) String queryProName,
                              @RequestParam(value = "pageIndex", required = false) String pageIndex) {



        List<Provider> providerList =null;
        int pageSize = Constants.pageSize;

        // 当前页
        int currentPageNo = 1;

        // 设置当前页
        if (pageIndex != null) {
            try {
                currentPageNo = Integer.parseInt(pageIndex);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        int totalCount = 0;
        try {
            // 根据 用户的名称，角色id查询中记录数量
            totalCount =providerService.getTotalCount(queryProName, queryProCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);// 当前页
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = pageSupport.getTotalPageCount();
        // 控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        // 查询用户的数据列表
        try {
            providerList = providerService.getProList(queryProName, queryProCode, currentPageNo, pageSize);
            System.out.println(queryProName+queryProCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("providerList", providerList);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "providerlist";
    }

    /**
     * 跳转到添加页面
     *
     * @param provider
     * @return
     */
    @GetMapping("/add.html")
    public String addUProvider(@ModelAttribute("provider") Provider provider) {
        return "provideradd";
    }

    /**
     * 添加供应商
     * @param provider
     * @param session
     * @param request
     * @param attachs
     * @return
     */
    @PostMapping("/addsave.html")
    public String addUserSave(Provider provider, HttpSession session,
                              HttpServletRequest request,
                              @RequestParam(value = "attachs", required = false) MultipartFile[] attachs) {
        // 设置初始值
        String companyLicPicPath = null;
        String orgCodePicPath = null;
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
                        companyLicPicPath = path + File.separator + fileName;
                    }
                    if (i == 1) {
                        orgCodePicPath = path + File.separator + fileName;
                    }
                    System.out.println("idPicPath" + companyLicPicPath);
                    System.out.println("workPicPath" + orgCodePicPath);
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
            provider.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
            // 创建时间
            provider.setCreationDate(new Date());
            provider.setCompanyLicPicPath(companyLicPicPath);
            provider.setOrgCodePicPath(orgCodePicPath);
            try {
                if (providerService.add(provider))
                    // 添加成功
                    return "redirect:/sys/provider/list.html";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "provideradd";
    }

    /**
     * 修改回显
     * 路径传参
     */
    @RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
    public String getUserById(@PathVariable String id, HttpServletRequest request, Model model) {
        Provider provider = new Provider();
        // 使用userService 完成修改回显功能
        try {
            provider = providerService.getProviderById(Integer.parseInt(id));

            if (provider.getCompanyLicPicPath() != null && !provider.getCompanyLicPicPath().equals("")) {
                String[] paths = provider.getCompanyLicPicPath().split("\\" + File.separator);
                // 获取照片的名称
                provider.setCompanyLicPicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
                // 图片的存储路径
                System.out.println("证件照" + provider.getCompanyLicPicPath());
            }

            if (provider.getOrgCodePicPath() != null && !provider.getOrgCodePicPath().equals("")) {
                String[] paths = provider.getOrgCodePicPath().split("\\" + File.separator);
                // 获取照片的名称
                provider.setOrgCodePicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
                // 图片的存储路径
                System.out.println("证件照" + provider.getOrgCodePicPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(provider);
        return "providermodify";
    }

    /**
     * 修该供应商
     * @param provider
     * @param session
     * @param request
     * @param attachs
     * @return
     */
    @RequestMapping(value = "/modifysave.html", method = RequestMethod.POST)
    public String modifyUser(Provider provider, HttpSession session, HttpServletRequest request,
                             @RequestParam(value = "attachs", required = false) MultipartFile[] attachs) {
        // 设置初始值
        String companyLicPicPath = null;
        String orgCodePicPath = null;
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
                            companyLicPicPath = path + File.separator + fileName;
                        }
                        if (i == 1) {
                            orgCodePicPath = path + File.separator + fileName;
                        }
                        System.out.println("idPicPath" + companyLicPicPath);
                        System.out.println("workPicPath" + orgCodePicPath);
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
            provider.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
            // 创建时间
            provider.setCreationDate(new Date());
            provider.setCompanyLicPicPath(companyLicPicPath);
            provider.setOrgCodePicPath(orgCodePicPath);
        }
        try {
            if (providerService.modify(provider))
                // 添加成功
                return "redirect:/sys/provider/list.html";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "providermodify";
    }

    /**
     * 查看供应商
     * @param id
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/view/{id}")
    public String view(@PathVariable String id, Model model, HttpServletRequest request) {

        Provider provider = new Provider();
        try {
            // 1.调用userService方法
            provider = providerService.getProviderById(Integer.parseInt(id));
            if (provider.getCompanyLicPicPath() != null && !provider.getCompanyLicPicPath().equals("")) {
                String[] paths = provider.getCompanyLicPicPath().split("\\" + File.separator);
                // 获取照片的名称
                provider.setCompanyLicPicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
                // 图片的存储路径
                System.out.println("证件照" + provider.getCompanyLicPicPath());
            }

            if (provider.getOrgCodePicPath() != null && !provider.getOrgCodePicPath().equals("")) {
                String[] paths = provider.getOrgCodePicPath().split("\\" + File.separator);
                // 获取照片的名称
                provider.setOrgCodePicPath(request.getContextPath() + "/statics/uploadfiles/" + paths[paths.length - 1]);
                // 图片的存储路径
                System.out.println("工作" + provider.getOrgCodePicPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(provider);
        return "providerview";
    }
    @PostMapping("/del.json")
    @ResponseBody
    public Object deleteUser(@RequestParam("proid") String id) {
        HashMap<String, String> resultMap = new HashMap<>();
        System.out.println(id);

        // 判断传递的id是否存在
        if (StringUtils.isNullOrEmpty(id)) {
            // id不存在
            resultMap.put("delResult", "noexist");
        } else {
            try {
                // 删除用户
                if (providerService.deleteProviderById(Integer.parseInt(id))) {
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
}
