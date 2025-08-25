package com.z.module.acct.web.mapper;

import com.z.framework.common.web.vo.SelectOptionVO;
import com.z.module.acct.domain.AccountCls;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountClsSelectMapper {

    @Mapping(source = "code", target = "value")
    @Mapping(source = "name", target = "label")
    SelectOptionVO convert(AccountCls bean);

    List<SelectOptionVO> convert(List<AccountCls> bean);

}
