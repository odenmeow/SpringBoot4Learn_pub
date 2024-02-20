package com.oni.training.springboot.MyProduct.entity.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ProductQueryParameter {
    @Schema(description = "product name",example = "Java")
    @NotEmpty(message = "關鍵字不得為空")
    private String keyword;
    @Schema(description = "price or name.",example = "price")

    private String orderBy;
    @Schema(description = "asc or desc",example = "desc")

    private String sortRule;
    @Schema(description = "0~2147483647, can't be omitted.",example = "0")

    @NotNull(message = "沒有填入被預設為null不可以唷，故意練習notnull用的")
    private Integer pricefrom;
    @Schema(description = "0~2147483647. if omitted, then default 2147483647",example = "500")

    private Integer priceto;
    // 把當她作 參數的話 只要?orderBy=xxx 左邊的字跟這邊屬性一樣就可以無視排序
    // 一下改小寫一下又大  歸剛ㄟ

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
