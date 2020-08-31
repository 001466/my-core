package org.easy.tool.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public abstract class KeyEntity<K> extends BaseEntity {
    @JsonIgnore
    public abstract K getKey();

    @ApiModelProperty(required = false, hidden = true)
    @JsonIgnore
    private String clientId;
    @ApiModelProperty(required = false, hidden = true)
    private Integer hostId;

    @ApiModelProperty(required = false, hidden = true)
    private Integer enabled;
    @ApiModelProperty(required = false, hidden = true)
    private Long createUser;
    @ApiModelProperty(required = false, hidden = true)
    private Date createTime;
    @ApiModelProperty(required = false, hidden = true)
    private Long updateUser;
    @ApiModelProperty(required = false, hidden = true)
    private Date updateTime;


    @ApiModelProperty(required = false, hidden = false)
    private Date startTime;
    @ApiModelProperty(required = false, hidden = false)
    private Date endTime;


    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getHostId() {
        return hostId;
    }

    public void setHostId(Integer hostId) {
        this.hostId = hostId;
    }


}
