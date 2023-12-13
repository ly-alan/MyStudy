package com.roger.main.function;

import java.util.List;

/**
 * @Author Roger
 * @Date 2023/12/11 16:05
 * @Description
 */
public class DataModel<T> {

    private List<T> result;

    private Integer totalNum;

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

}
