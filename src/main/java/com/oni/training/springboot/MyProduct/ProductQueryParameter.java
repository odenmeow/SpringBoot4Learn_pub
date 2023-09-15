package com.oni.training.springboot.MyProduct;

public class ProductQueryParameter {
    private String keyword;
    private String orderBy;
    private String sortRule;
    // 把當她作 參數的話 只要?orderBy=xxx 左邊的字跟這邊屬性一樣就可以無視排序
    public ProductQueryParameter(String keyword, String orderBy, String sortRule) {
        this.keyword = keyword;
        this.orderBy = orderBy;
        this.sortRule = sortRule;
    }

    public String getSortRule() {
        return sortRule;
    }

    public void setSortRule(String sortRule) {
        this.sortRule = sortRule;
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
}
