package com.z.module.acct.web.mapper;

import com.z.module.acct.domain.VoucherDetail;
import com.z.module.acct.web.vo.VoucherDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VoucherDetailMapper {

    @Mapping(source = "createdDate", target = "createdDate", qualifiedByName = "instantToString")
    VoucherDetailVO convert(VoucherDetail voucherDetail);

    @Named("instantToString")
    default String instantToString(Instant instant) {
        if (instant == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return formatter.format(dateTime);
    }

    List<VoucherDetailVO> convert(List<VoucherDetail> voucherDetailList);
}
