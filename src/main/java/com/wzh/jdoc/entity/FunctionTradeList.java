package com.wzh.jdoc.entity;

import java.util.List;

/**
 * @author wzh
 * @since 2021/7/27
 */
public class FunctionTradeList {
    private List<FunctionTrade> functionTradeList;

    public List<FunctionTrade> getFunctionTradeList() {
        return functionTradeList;
    }

    public void setFunctionTradeList(List<FunctionTrade> functionTradeList) {
        this.functionTradeList = functionTradeList;
    }

    @Override
    public String toString() {
        return "FunctionTradeList{" +
                "functionTradeList=" + functionTradeList +
                '}';
    }
}
