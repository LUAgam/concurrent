package io.concurrent.ioconcurrent.constant;


public final class ResponseConstant {

    /**
     * 响应状态码:失败
     **/
    public static final Integer RESPONSE_CODE_ERROR = -1;

    /**
     * 响应消息:失败
     **/
    public static final String RESPONSE_MESSAGE_ERROR = "失败，请核对参数";

    /**
     * 响应状态码:成功
     **/
    public static final Integer RESPONSE_CODE_SUCCESS = 0;

    /**
     * 响应消息:成功
     **/
    public static final String RESPONSE_MESSAGE_SUCCESS = "成功";

    /**
     * 响应状态码:参数为空
     **/
    public static final Integer RESPONSE_CODE_PARAM_EMPTY = -2;

    /**
     * 响应消息:参数为空
     **/
    public static final String RESPONSE_MESSAGE_PARAM_EMPTY = "失败，请求参数不能为空: ";

    /**
     * 响应状态码:参数格式错误
     **/
    public static final Integer RESPONSE_CODE_PARAM_FORMAT_ERROR = -3;

    /**
     * 响应消息:参数格式错误
     **/
    public static final String RESPONSE_MESSAGE_PARAM_FORMAT_ERROR = "失败，请求参数格式错误: ";

    /**
     * 响应状态码:未授权
     **/
    public static final Integer RESPONSE_CODE_AUTH_ERROR = -4;

    /**
     * 响应消息:未授权
     **/
    public static final String RESPONSE_MESSAGE_AUTH_ERROR = "未授权，请重新登陆";

    /**
     * 响应状态码:未授权
     **/
    public static final Integer RESPONSE_CODE_NO_RESOURCE = -5;

    /**
     * 当前不是导购
     **/
    public static final Integer RESPONSE_CODE_NO_EMPLOYEE = -7;

    /**
     * 响应消息:未授权
     **/
    public static final String RESPONSE_MESSAGE_NO_RESOURCE = "无法查找到资源:";

    /**
     * 响应消息:未授权
     **/
    public static final String RESPONSE_MESSAGE_DATE_ERROR = "时间转换错误";

    public static final String RESPONSE_MESSAGE_SAVE_ERROR = "保存失败";

    public static final Integer RESPONSE_CODE_CUSTOM = -6;

    public static final String RESPONSE_MESSAGE_PAY_ALREADY = "您已购买过此卡";

    public static final String RESPONSE_MESSAGE_NOT_ALLOW = "当前订单已结算！";

    public static final String KEY_IS_FORCELOGOUT = "isForceLogout";

    public static final String KEY_SYNC_VIP = "syncVip";

    public static final String KEY_SYNC_EMPLOYEE = "syncEmployeeId";

    public static final String KEY_SYNC_TOKEN = "syncToken";

    public static final String KEY_UNBIND_TOKEN = "unBindToken";

    public static final String KEY_UNBIND_FLAG = "unBindFlag";

    public static final String LAND_VIP_FLAG = "landVipFlag";

    public static final String KEY_BIND_TOKEN = "bindToken";

    public static final String KEY_BIND_FLAG = "bindFlag";

    public static final String KEY_SYNC_EMPLOYEE_TYPE = "syncEmployeeType";

    public static final String RESPONSE_MESSAGE_NOT_ACCESS = "该操作不允许！";

    public static final String KEY_SYNC_FANS_TYPE = "syncFansType";

    public static final Integer RESPONSE_CODE_PARAM_ERROR = -7;

    public static final String RESPONSE_MESSAGE_PARAM_ERROR = "失败，请求参数错误: ";

    public static final String KEY_RESULT = "result";

    /**
     * 需要重新授权
     */
    public static final Integer RESPONSE_CODE_RESYNC = -10000;

    public static final String RESPONSE_MESSAGE_RESYNC_ERROR = "失败，请重新授权！";

    /**
     * 事务回滚
     */
    public static final Integer RESPONSE_CODE_TRANSACTION = -10001;

    /**
     * 未获得抽奖次数
     */
    public static final Integer DRAW_NONE_TIMES = -1;
    /**
     * 抽奖次数用完
     */
    public static final Integer DRAW_FINISH_ALL_TIMES = -2;
    /**
     * 当天抽奖次数用完
     */
    public static final Integer DRAW_FINISH_TODAY_TIMES = -3;
    /**
     * 当天可增加抽奖次数达到上限
     */
    public static final Integer DRAW_TODAY_ADD_END = -4;

    /**
     * 业务状态码: 授权失败
     */
    public static final Integer CODE_AUTH_ERROR = 1001;

    /**
     * 业务信息: 授权失败
     */
    public static final String MSG_AUTH_ERROR = "授权失败";

    /**
     * 红星接口出参解析出错
     */
    public static final String RESPONSE_MESSAGE_ERROR_ANALYSIS = "解析接口出参格式错误：";

    public static final String RESPONSE_MESSAGE_ERROR_INTERFACE = "红星接口调用超时或失败";

    /**
     * 业务状态码：入参omsCode为空
     */
    public static final Integer CODE_OMS_CODE_ERROR = 1002;

    /**
     * 业务信息：入参omsCode为空
     */
    public static final String MSG_OMS_CODE_ERROR = "入参omsCode为空";

    /**
     * 业务状态码：用户信息缺失
     */
    public static final Integer CODE_ACTUSER_ERROR = 1003;
    /**
     * 业务信息：用户信息缺失
     */
    public static final String MSG_ACTUSER_ERROR = "用户信息缺失";
    /**
     * 业务状态码：该用户手机号未注册
     */
    public static final Integer CODE_MOBILE_ERROR = 1004;
    /**
     * 业务信息：该用户手机号未注册
     */
    public static final String MSG_MOBILE_ERROR = "该用户手机号未注册";
    /**
     * 打卡业务状态码：打卡成功
     */
    public static final Integer CODE_IS_CARD_SUCCESS = 1005;
    /**
     * 打卡业务信息：打卡成功
     */
    public static final String MSG_IS_CARD_SUCCESS = "打卡成功";
    /**
     * 打卡业务状态码：券已发放
     */
    public static final Integer CODE_SEND_TICKET = 1006;
    /**
     * 打卡业务信息：券已发放
     */
    public static final String MSG_SEND_TICKET = "券已发放";
    /**
     * 打卡业务状态码：券已领完
     */
    public static final Integer CODE_SEND_TICKET_FALSE = 1007;
    /**
     * 打卡业务信息：券已领完
     */
    public static final String MSG_SEND_TICKET_FALSE = "券已领完";
    /**
     * 打卡业务状态码：已打卡
     */
    public static final Integer CODE_IS_CARD_ERROR = 1008;
    /**
     * 打卡业务信息：已打卡
     */
    public static final String MSG_IS_CARD_ERROR = "已打卡";
    /**
     * 打卡业务状态码：获取券信息异常
     */
    public static final Integer CODE_GET_COUPON_ERROR = 1009;
    /**
     * 打卡业务信息：获取券信息异常
     */
    public static final String MSG_GET_COUPON_ERROR = "抱歉,券已领完";
    /**
     * 打卡业务状态码：已领取
     */
    public static final Integer CODE_GET_TICKET_ERROR = 1010;
    /**
     * 打卡业务信息：已领取
     */
    public static final String MSG_GET_TICKET_ERROR = "您已领取该券！";

    /**
     * 业务状态码：入参groupId为空
     */
    public static final Integer CODE_GROUP_CODE_ERROR = 1011;

    /**
     * 业务信息：入参omsCode为空
     */
    public static final String MSG_GROUP_CODE_ERROR = "入参groupId为空";

    /**
     * 获取免费劵已到上限
     */
    public static final Integer CODE_GET_FREE_COUPON_ERROR = 1012;
    public static final String  MSG_GET_FREE_COUPON_ERROR = "抱歉，已领取所有优惠券";
    /**
     *
     */
    public static final Integer CODE_GET_FREE_COUPON_NULL_TICKET = 1013;
    public static final String MSG_GET_FREE_COUPON_NULL_TICKET="该商场未设置该券";

    public static final Integer CODE_NOT_ACT_TIME = 1014;
    public static final String MSG_NOT_ACT_TIME="不在活动时间";


    /**
     * 助力业务状态码：已为好友助力过
     */
    public static final Integer CODE_IS_BOOST_ERROR = 2000;
    /**
     * 打卡业务信息：已为好友助力过
     */
    public static final String MSG_IS_BOOST_ERROR = "您已为好友助力过！";
    /**
     * 助力业务状态码：您为好友的助力次数已达上限
     */
    public static final Integer CODE_NOT_RESIDUE_NUM_ERROR = 2001;
    /**
     * 打卡业务信息：您为好友的助力次数已达上限
     */
    public static final String MSG_NOT_RESIDUE_NUM_ERROR = "您为好友的助力次数已达上限！";
    /**
     * 助力业务状态码：助力成功
     */
    public static final Integer CODE_BOOST_SUCCESS = 2002;
    /**
     * 打卡业务信息：助力成功
     */
    public static final String MSG_BOOST_SUCCESS = "助力成功！";

}
