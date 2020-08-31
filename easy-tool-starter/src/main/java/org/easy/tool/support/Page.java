package org.easy.tool.support;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel
@Setter
@Getter
@NoArgsConstructor
public class Page extends BaseEntity{
    public Page(Integer page,Integer size){
        this.page=page;
        this.size=size;
    }
    @ApiModelProperty(value = "总数",required=false,hidden=true)
    Integer total;
    @ApiModelProperty(value = "页码",required=false,hidden=false)
    Integer page;
    @ApiModelProperty(value = "页数",required=false,hidden=false)
    Integer size;
}
