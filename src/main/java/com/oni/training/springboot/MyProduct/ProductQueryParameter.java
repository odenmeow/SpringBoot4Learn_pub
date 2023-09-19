package com.oni.training.springboot.MyProduct;

public class ProductQueryParameter {
    private String keyword;
    private String orderBy;
    private String sortRule;
    private Integer pricefrom;
    private Integer priceto;
    // 把當她作 參數的話 只要?orderBy=xxx 左邊的字跟這邊屬性一樣就可以無視排序


    public ProductQueryParameter(String keyword, String orderBy, String sortRule, Integer pricefrom, Integer priceto) {
        this.keyword = keyword;
        this.orderBy = orderBy;
        this.sortRule = sortRule;
        this.pricefrom = pricefrom;
        this.priceto = priceto;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSortRule() {
        return sortRule;
    }

    public void setSortRule(String sortRule) {
        this.sortRule = sortRule;
    }

    public Integer getPricefrom() {
        return pricefrom;
    }

    public void setPricefrom(Integer pricefrom) {
        this.pricefrom = pricefrom;
    }

    public Integer getPriceto() {
        return priceto;
    }

    public void setPriceto(Integer priceto) {
        this.priceto = priceto;
    }
}
