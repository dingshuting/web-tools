package com.ijs.core.common.model;


public class VersionUpdateModel {
    private int build;
    private boolean isNeedUpgrade;
    private String upgradeUrl;
    private boolean isMustUpgrade;
    private String description;
    public int getBuild() {
        return build;
    }

    public void setBuild(int build) {
        this.build = build;
    }

    public boolean isNeedUpgrade() {
        return isNeedUpgrade;
    }

    public void setNeedUpgrade(boolean needUpgrade) {
        isNeedUpgrade = needUpgrade;
    }

    public String getUpgradeUrl() {
        return upgradeUrl;
    }

    public void setUpgradeUrl(String upgradeUrl) {
        this.upgradeUrl = upgradeUrl;
    }

    public boolean isMustUpgrade() {
        return isMustUpgrade;
    }

    public void setMustUpgrade(boolean mustUpgrade) {
        isMustUpgrade = mustUpgrade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
