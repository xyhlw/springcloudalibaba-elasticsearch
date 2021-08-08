package com.xy.cloud.search.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Document(indexName = "manual_eng_dir", type = "engdir")
public class ManualEngDirEntity {
	private String  id;
	//地址
	private String	 address;
	//标题
	private String	 title;
	//版本号
	private String   version;
	//手册目录id
	private String  parentId;
	//内容
	private String   conent;
	//手册章节id
	private String   manualInfoId;
	//手册章节当前节点id
	private String  chapterId;
	//手册章节当前父节点id
	private String  chapterPrentId;
	//链接地址
	private String   hrefAddress;
	//跳转地址
	private String   urlAddress;
	//更新时间
	private Date     updateTime;
	//使用场景
	private Integer usageScenariosType;
	//手册格式类型
	private	Integer	manualType;
	//手册权限id
	private	String	authId;
	//手册下载权限id
	private	String	downloadId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getConent() {
		return conent;
	}

	public void setConent(String conent) {
		this.conent = conent;
	}

	public String getManualInfoId() {
		return manualInfoId;
	}

	public void setManualInfoId(String manualInfoId) {
		this.manualInfoId = manualInfoId;
	}

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	public String getChapterPrentId() {
		return chapterPrentId;
	}

	public void setChapterPrentId(String chapterPrentId) {
		this.chapterPrentId = chapterPrentId;
	}

	public String getHrefAddress() {
		return hrefAddress;
	}

	public void setHrefAddress(String hrefAddress) {
		this.hrefAddress = hrefAddress;
	}

	public String getUrlAddress() {
		return urlAddress;
	}

	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getUsageScenariosType() {
		return usageScenariosType;
	}

	public void setUsageScenariosType(Integer usageScenariosType) {
		this.usageScenariosType = usageScenariosType;
	}

	public Integer getManualType() {
		return manualType;
	}

	public void setManualType(Integer manualType) {
		this.manualType = manualType;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getDownloadId() {
		return downloadId;
	}

	public void setDownloadId(String downloadId) {
		this.downloadId = downloadId;
	}
}
