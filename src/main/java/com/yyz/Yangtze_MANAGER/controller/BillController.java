package com.yyz.Yangtze_MANAGER.controller;

import com.yyz.Yangtze_MANAGER.entity.User;
import com.yyz.Yangtze_MANAGER.service.BillService;
import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.yyz.Yangtze_MANAGER.entity.Bill;
import com.yyz.Yangtze_MANAGER.entity.Provider;
import com.yyz.Yangtze_MANAGER.service.ProviderService;
import com.yyz.Yangtze_MANAGER.utils.Constants;
import com.yyz.Yangtze_MANAGER.utils.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/19 9:05
 */
@Controller
@RequestMapping("/sys/bill")
public class BillController {
    @Autowired
    private BillService billService;
    @Autowired(required = false)
    private ProviderService providerService;

    @RequestMapping("/list.html")
    public String getBillList(
            Model model,
            @RequestParam(value = "queryProductName",required = false) String queryProductName,
            @RequestParam(value = "queryProviderId",required = false) Integer queryProviderId,
            @RequestParam(value = "queryIsPayment",required = false) Integer queryIsPayment,
            @RequestParam(value = "pageIndex",required = false) String pageIndex
    ){
        //设置初始值
        Integer _queryProviderId = null;
        //用户列表
        List<Bill> billList =null;
        //角色列表
        List<Provider> providerList = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //设置用户名称
        String providerName = null;
        //设置当前页
        int currentPageNo = 1;
        if(queryProductName ==null){
            queryProductName="";
        }
        if(queryProviderId !=null && !queryProviderId.equals("")){
            _queryProviderId = queryProviderId;
        }
        //设置当前页
        if(pageIndex !=null){
            try {
                currentPageNo = Integer.parseInt(pageIndex);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        //设置商品总量
        int totalCount = 0;
        try {
            //如果需要得到商品的总数量，根据商品名称和供应商ID
            totalCount = billService.getTotalCount(queryProductName,_queryProviderId);
        }catch (Exception e){
            e.printStackTrace();
        }
        //总页数封装分页工具类
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        //总页数
        int totalPageCount = pageSupport.getTotalPageCount();

        //控制首页和尾页
        if(currentPageNo < 1)
            currentPageNo = 1;
        else if(currentPageNo > totalPageCount)
            currentPageNo = totalPageCount;

        try {
            //查询商品页表
            billList =  billService.getBillList(queryProductName,_queryProviderId,queryIsPayment,currentPageNo,pageSize);
            providerList = providerService.getProviderList();
        }catch (Exception e){
            e.printStackTrace();
        }
        //怎么封装数据
        model.addAttribute("billList",billList);
        model.addAttribute("providerList",providerList);
        model.addAttribute("queryProductName",queryProductName);
        model.addAttribute("queryProviderId",queryProviderId);
        model.addAttribute("queryIsPayment",queryIsPayment);
        model.addAttribute("totalPageCount",totalPageCount);//总页数
        model.addAttribute("totalCount",totalCount);//总数量
        model.addAttribute("currentPageNo",currentPageNo);//当前页
        model.addAttribute("providerName",providerName);//当前页
        return "billlist";
    }
    /*
     * 添加一个跳转到添加页面
     * */
    @RequestMapping(value = "/add.html",method = RequestMethod.GET)
    public String addUser(@ModelAttribute("bill") Bill bill){
        return "billadd";
    }

    /*
     * 添加订单
     * */
    @RequestMapping(value = "/addsave.html",method = RequestMethod.POST)
    public String addBillSave(Bill bill, HttpSession session){
        if(session.getAttribute(Constants.USER_SESSION )== null)
            return "redirect:/login.jsp";
        bill.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());
        try {
            if(billService.add(bill))
                return "redirect:/sys/bill/list.html";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "billadd";
    }
    /*
     * 获取供应商名称
     * */
    @RequestMapping(value = "/providerlist.json",method = RequestMethod.GET)
    @ResponseBody
    public List<Provider> providerList(){
        //初始化供应商
        List<Provider> providerList = null;
        try {
            providerList = providerService.getProviderList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return providerList;
    }

    @RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
    public String getBillById(@PathVariable String id,Model model){
        Bill bill = new Bill();
        try {
            bill = billService.getBillById(Integer.parseInt(id));

        }catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute(bill);
        return "billmodify";
    }

    /*
     * 修改订单
     * */
    @RequestMapping(value = "/modifysave.html", method = RequestMethod.POST)
    public String modifyBill(Bill bill,HttpSession session){
        if(session.getAttribute(Constants.USER_SESSION )== null)
            return "redirect:/login.jsp";
        bill.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        try {
            if(billService.modify(bill))
                return "redirect:/sys/bill/list.html";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "billmodify";
    }

    /*
     * 查看订单
     * */
    @RequestMapping("/view/{id}")
    public String view(@PathVariable String id, Model model){
        Bill bill = new Bill();
        try {
            bill = billService.getBillById(Integer.parseInt(id));
            System.out.println(bill);
        }catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute(bill);
        return "billview";
    }

    /*
     * 删除订单
     * */
    @RequestMapping(value = "/delbill.json",method = RequestMethod.GET)
    @ResponseBody
    public Object deleteBill(@RequestParam String id) {
        HashMap<String, String> resultMap = new HashMap<>();

        if (StringUtils.isNullOrEmpty(id)) {
            resultMap.put("delResult", "noexist");
        } else {
            try {
                if(billService.deleteBill(Integer.parseInt(id))) {
                    //删除成功
                    resultMap.put("delResult", "true");
                }else {
                    //删除失败
                    resultMap.put("delResult", "false");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return JSONArray.toJSONString(resultMap);
    }
}
