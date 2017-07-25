package com.jing.cloud.service.bean;

import javax.validation.constraints.NotNull;

public class Position {
    @NotNull
    private String org;

    
    public String getOrg() {
        return org;
    }

    
    public void setOrg(String org) {
        this.org = org;
    }


    @Override
    public String toString() {
        return "Position [org=" + org + "]";
    }
}
