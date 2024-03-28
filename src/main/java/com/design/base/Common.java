package com.design.base;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Common {

    public static final long JWT_TOKEN_VALIDITY = 60 * 60 * 1000;

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";

    public static final String SYSTEM = "7864c635-7d4b-6284-927c-49698f16e8cf";

    public static final String MANAGER = "2f855f8d-9f5e-4cd4-bfbf-e6ccad4287cb";

    public static final String BUSINESS = "0e3afb66-e332-4113-bfcd-a76c563413f4";

    public static final String ENCODING = "utf-8";

    public static final String CONTENT_TYPE = "application/json;charset=utf-8";

    public static final DateTimeFormatter DATE_FORMAT_1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter DATE_FORMAT_2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final ZoneId zoneId = ZoneId.of("Asia/Taipei");

    public static final long DAYS = 24 * 60 * 60;

    public static final long HOURS = 60 * 60;

    public static final long MINUTES = 60;

    public static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

}
