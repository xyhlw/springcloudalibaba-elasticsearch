package com.xy.cloud.search.vo;


import java.io.Serializable;
import java.util.Date;

public class SearchWordsSortVo implements Serializable {

    private static final long serialVersionUID = -5572093438469538077L;

    private Integer id;

    private String searchWordsName;

    private String userId;

    private String  dirId;

    private String searchCount;

    private Date createDate;

    private Date updateDate;

    private String languageType;

    //使用场景
    private String usageScenariosType;
    //手册格式类型
    private	String	manualType;



    public String getSearchWordsName() {
        return searchWordsName;
    }

    public void setSearchWordsName(String searchWordsName) {
        this.searchWordsName = searchWordsName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(String searchCount) {
        this.searchCount = searchCount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDirId() {
        return dirId;
    }

    public void setDirId(String dirId) {
        this.dirId = dirId;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public String getUsageScenariosType() {
        return usageScenariosType;
    }


    public String getManualType() {
        return manualType;
    }

    public void setManualType(String manualType) {
        this.manualType = manualType;
    }

    public void setUsageScenariosType(String usageScenariosType) {
        this.usageScenariosType = usageScenariosType;
    }
}
