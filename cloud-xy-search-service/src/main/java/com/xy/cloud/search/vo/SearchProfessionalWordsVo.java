package com.xy.cloud.search.vo;

import java.io.Serializable;

/**
 * <p>
 *
 * @author yangchang
 * @since 2021-07-02
 */
public class SearchProfessionalWordsVo implements Serializable {

    private static final long serialVersionUID = -5107757912886593656L;

    private  Integer id;

    private  String  professionalWordsName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProfessionalWordsName() {
        return professionalWordsName;
    }

    public void setProfessionalWordsName(String professionalWordsName) {
        this.professionalWordsName = professionalWordsName;
    }

    @Override
    public String toString() {
        return "SearchProfessionalWordsVo{" +
                "id=" + id +
                ", professionalWordsName='" + professionalWordsName + '\'' +
                '}';
    }
}
