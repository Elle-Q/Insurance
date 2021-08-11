package com.fintech.insurance.micro.support.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 广告实体
 *
 * @author Sean Zhang
 * @version 1.1.0
 * @since 2017-08-18 16:53:34
 */
@Entity
@Table(name = "data_advertisement")
public class Advertisement extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -6655609407433639404L;

	@Column(name = "is_display", columnDefinition = "boolean comment '是否展示'")
	private Boolean displayFlag = true;

    @Column(name = "title", columnDefinition = "varchar(256) comment '标题'")
    private String title;

    @Column(name = "content", columnDefinition = "text comment '内容'")
    private String content;

    @Column(name = "url", columnDefinition = "varchar(1024) comment '链接地址'")
    private String url;

    @Column(name = "image", columnDefinition = "varchar(256) comment '图片上传的UUID'")
    private String image;

    @Column(name = "start_at", columnDefinition = "datetime comment '开始展示时间'")
    private Date startAt;

    @Column(name = "end_at", columnDefinition = "datetime comment '结束展示时间'")
    private Date endAt;

    @Column(name = "display_sequence", columnDefinition = "int(11) default 1 comment '展示顺序'")
    private Integer displaySequence;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "position_id", columnDefinition = "int(11) comment '外键，广告位标识，关联position_id表的id'")
	private AdvertisementPosition advertisementPosition;

	@Column(name = "created_by", columnDefinition = "int(11) comment '创建人主键'")
	private Integer createBy;

    @Column(name = "updated_by", columnDefinition = "int(11) comment '更新人主键'")
    private Integer updateBy;

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Boolean getDisplayFlag() {
        return displayFlag;
    }

    public void setDisplayFlag(Boolean displayFlag) {
        this.displayFlag = displayFlag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public Integer getDisplaySequence() {
        return displaySequence;
    }

    public void setDisplaySequence(Integer displaySequence) {
        this.displaySequence = displaySequence;
    }

    public AdvertisementPosition getAdvertisementPosition() {
        return advertisementPosition;
    }

    public void setAdvertisementPosition(AdvertisementPosition advertisementPosition) {
        this.advertisementPosition = advertisementPosition;
    }
}
