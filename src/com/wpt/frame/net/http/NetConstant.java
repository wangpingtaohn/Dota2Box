package com.wpt.frame.net.http;

/**
 * @author liujun
 */
public class NetConstant {

	public static final long TIME_OUT = 30000L;
	public static final String JSON_RESULT = "result";
	public static final String JSON_RESULT_CODE = "code";
	public static final String JSON_RESULT_MSG = "msg";
	public static final String JSON_RESULT_DATA = "data";
	
	public static final String NETWORK_TIMEOUT = "网络不太给力哦，点我一下重新试试吧~~";
	public static final String SERVER_ERROR = "服务器好像不太高兴...";
	
	public static final int send_msm_faild = 10001;
	
	public static final int account_or_pwd_error = 20101;
	
	public static final int account_already_exists = 20102;
	
	public static final int account_not_exists = 20103;
	
	public static final int verify_code_overdue = 20104;
	
	public static final int verify_code_error = 20105;
	
	
}
