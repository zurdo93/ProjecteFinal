package com.cursfundacionesplai.restasearch.models;

public class PlusCode {
    private String compoundCode;
    private String globalCode;

    public PlusCode(String compoundCode, String globalCode) {
        this.compoundCode = compoundCode;
        this.globalCode = globalCode;
    }

    public String getCompoundCode() {
        return compoundCode;
    }

    public void setCompoundCode(String compoundCode) {
        this.compoundCode = compoundCode;
    }

    public String getGlobalCode() {
        return globalCode;
    }

    public void setGlobalCode(String globalCode) {
        this.globalCode = globalCode;
    }
}
