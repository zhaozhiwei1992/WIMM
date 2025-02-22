package com.z.module.system.web.vo;

import lombok.Data;
import lombok.ToString;

/**
 * View Model object for storing a user's credentials.
 */
@Data
@ToString
public class SmsLoginVO {

    private String mobile;

    private String scene;

}
