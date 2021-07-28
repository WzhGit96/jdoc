package com.wzh.jdoc.entity;

/**
 * @author wzh
 * @since 2021/7/27
 */
public class InterfaceInfo {
    private String seqNo;

    private String functionName;

    private String functionDesc;

    private String requestMethod;

    private String url;

    private Input input;

    private Output output;

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionDesc() {
        return functionDesc;
    }

    public void setFunctionDesc(String functionDesc) {
        this.functionDesc = functionDesc;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "InterfaceInfo{" +
                "seqNo='" + seqNo + '\'' +
                ", functionName='" + functionName + '\'' +
                ", functionDesc='" + functionDesc + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", url='" + url + '\'' +
                ", input=" + input +
                ", output=" + output +
                '}';
    }
}
