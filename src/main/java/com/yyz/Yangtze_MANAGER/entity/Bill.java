package com.yyz.Yangtze_MANAGER.entity;

import java.util.Date;

/**
 * @author yyz
 * @version 1.0
 * @description:
 * @date 2022/7/19 9:06
 */
public class Bill {
    private Integer id;//订单编号
    private String billCode;//订单编码
    private String productName;//产品名称
    private String productDesc;//产品描述
    private String priductUnit;//产品单位
    private double productCount;//产品数量
    private double totalPrice;//总价格
    private int isPayment;//是否付款
    private Integer createdBy;//创建人
    private Date creationDate;//创建时间
    private Integer modifyBy;//更改人
    private Date modifyDate;//更改时间
    private Integer providerId;//供应商ID
    private String providerName;//供应商名称
    private String productUnit;//商品单位

    public Bill() {
    }

    public Bill(Integer id, String billCode, String productName, String productDesc, String priductUnit, Integer productCount, Integer totalPrice, int isPayment, Integer createdBy, Date creationDate, Integer modifyBy, Date modifyDate, Integer providerId,String providerName,String productUnit) {
        this.id = id;
        this.billCode = billCode;
        this.productName = productName;
        this.productDesc = productDesc;
        this.priductUnit = priductUnit;
        this.productCount = productCount;
        this.totalPrice = totalPrice;
        this.isPayment = isPayment;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.modifyBy = modifyBy;
        this.modifyDate = modifyDate;
        this.providerId = providerId;
        this.providerName = providerName;
        this.productUnit = productUnit;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getPriductUnit() {
        return priductUnit;
    }

    public void setPriductUnit(String priductUnit) {
        this.priductUnit = priductUnit;
    }

    public double getProductCount() {
        return productCount;
    }

    public void setProductCount(double productCount) {
        this.productCount = productCount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getIsPayment() {
        return isPayment;
    }

    public void setIsPayment(int isPayment) {
        this.isPayment = isPayment;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer  createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(Integer modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", billCode='" + billCode + '\'' +
                ", productName='" + productName + '\'' +
                ", productDesc='" + productDesc + '\'' +
                ", priductUnit='" + priductUnit + '\'' +
                ", productCount=" + productCount +
                ", totalPrice=" + totalPrice +
                ", isPayment=" + isPayment +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", modifyBy='" + modifyBy + '\'' +
                ", modifyDate=" + modifyDate +
                ", providerId=" + providerId +
                ", providerName=" + providerName +
                ", productUnit=" + productUnit +
                '}';
    }
}
