package com.fintech.insurance.micro.system.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 系统用户授权信息表
 */
@Entity
@Table(name = "sys_user_oauth")
public class UserOauth extends BaseEntity implements Serializable {

    @JoinColumn(name = "user_id", columnDefinition = "int(10) comment '用户id，关联sys_user表的主键'")
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @Column(name = "oauth_type", length = 16, columnDefinition = "varchar(16) comment '授权类型，微信接入有三种类型：wechat(微信内授权)、wechat_app(App微信授权)、wechat_pc(微信PC端授权)，根据以后的接入还可以增加weibo(微博授权)等'")
    private String oauthType;

    @Column(name = "oauth_app_id", length = 64, columnDefinition = "varchar(64) comment '授权的的第三方应用id'")
    private String oauthAppId;

    @Column(name = "oauth_account", length = 64, columnDefinition = "varchar(64) comment '授权后获取的用户的第三方帐号id'")
    private String oauthAccount;

    @Column(name = "nick_name", columnDefinition = "varchar(32) comment '第三方帐号中的昵称'")
    private String nickName;

    @Column(name = "gener", columnDefinition = "varchar(16) comment '第三方帐号信息中的性别'")
    private String gener;

    @Column(name = "header_image", columnDefinition = "varchar(32) comment '第三方帐号信息中的头像'")
    private String headerImage;

    @Column(name = "wx_unionid", columnDefinition = "varchar(64) comment '针对微信多公众号关联的同一用户身份识别字段'")
    private String wxUnionid;

    @Column(name = "oauth_content", columnDefinition = "varchar(1024) comment '授权信息完整内容'")
    private String oauthContent;

    @Column(name = "created_by", columnDefinition = "int(11) comment '创建人主键'")
    private Integer createBy;

    @Column(name = "updated_by", columnDefinition = "int(11) comment '更新人主键'")
    private Integer updateBy;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOauthType() {
        return oauthType;
    }

    public void setOauthType(String oauthType) {
        this.oauthType = oauthType;
    }

    public String getOauthAppId() {
        return oauthAppId;
    }

    public void setOauthAppId(String oauthAppId) {
        this.oauthAppId = oauthAppId;
    }

    public String getOauthAccount() {
        return oauthAccount;
    }

    public void setOauthAccount(String oauthAccount) {
        this.oauthAccount = oauthAccount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGener() {
        return gener;
    }

    public void setGener(String gener) {
        this.gener = gener;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public String getWxUnionid() {
        return wxUnionid;
    }

    public void setWxUnionid(String wxUnionid) {
        this.wxUnionid = wxUnionid;
    }

    public String getOauthContent() {
        return oauthContent;
    }

    public void setOauthContent(String oauthContent) {
        this.oauthContent = oauthContent;
    }

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
}
