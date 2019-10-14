package io.concurrent.ioconcurrent.entity;

import io.concurrent.ioconcurrent.constant.ResponseConstant;
import lombok.Data;


@Data
public class Response<T> {

    /**
     * 响应码
     **/
    private Integer code;
    /**
     * 响应消息（错误消息）
     **/
    private String msg;

    /**
     * 响应数据
     **/
    private T data;


    public static Response buildErrorResponse() {
        return new Builder()
                .code(ResponseConstant.RESPONSE_CODE_ERROR)
                .msg(ResponseConstant.RESPONSE_MESSAGE_ERROR)
                .build();
    }

    public static Response buildErrorResponse(String msg) {
        return new Builder()
                .code(ResponseConstant.RESPONSE_CODE_ERROR)
                .msg(msg)
                .build();
    }

    public static Response buildSuccessResponse() {
        return new Builder()
                .code(ResponseConstant.RESPONSE_CODE_SUCCESS)
                .msg(ResponseConstant.RESPONSE_MESSAGE_SUCCESS)
                .build();
    }

    public static Response buildSuccessResponse(Object data) {
        return new Builder()
                .code(ResponseConstant.RESPONSE_CODE_SUCCESS)
                .msg(ResponseConstant.RESPONSE_MESSAGE_SUCCESS)
                .data(data)
                .build();
    }

    public static Response buildParamEmptyError(String param) {
        return new Builder()
                .code(ResponseConstant.RESPONSE_CODE_PARAM_EMPTY)
                .msg(ResponseConstant.RESPONSE_MESSAGE_PARAM_EMPTY + param)
                .build();
    }

    public static Response buildCustomerParamEmptyError(String param) {
        return new Builder()
                .code(ResponseConstant.RESPONSE_CODE_CUSTOM)
                .msg(param)
                .build();
    }

    public static Response buildParamFormatError(String param) {
        return new Builder()
                .code(ResponseConstant.RESPONSE_CODE_PARAM_FORMAT_ERROR)
                .msg(ResponseConstant.RESPONSE_MESSAGE_PARAM_FORMAT_ERROR + param)
                .build();
    }

    public static Response buildParamError(String defaultMessage) {
        return new Response.Builder()
                .code(ResponseConstant.RESPONSE_CODE_PARAM_ERROR)
                .msg(ResponseConstant.RESPONSE_MESSAGE_PARAM_ERROR + defaultMessage)
                .build();
    }

    public static Response buildReSyncError() {
        return new Builder()
                .code(ResponseConstant.RESPONSE_CODE_RESYNC)
                .msg(ResponseConstant.RESPONSE_MESSAGE_RESYNC_ERROR)
                .build();
    }

    /**
     * token为空或者从token中拿数据失败，一定要返回这个Response
     *
     * @return
     */
    public static Response buildAuthError() {
        return new Builder()
                .code(ResponseConstant.RESPONSE_CODE_AUTH_ERROR)
                .msg(ResponseConstant.RESPONSE_MESSAGE_AUTH_ERROR)
                .build();
    }

    public static Response buildNoResourceError(String resource) {
        return new Builder()
                .code(ResponseConstant.RESPONSE_CODE_NO_RESOURCE)
                .msg(ResponseConstant.RESPONSE_MESSAGE_NO_RESOURCE + resource)
                .build();
    }

    public static Builder newBuilder() {
        Builder builder = new Builder();
        builder.code = ResponseConstant.RESPONSE_CODE_SUCCESS;
        builder.msg = ResponseConstant.RESPONSE_MESSAGE_SUCCESS;
        return builder;
    }

    public static Builder newBuilder(Integer code, String msg) {
        Builder builder = new Builder();
        builder.code = code;
        builder.msg = msg;
        return builder;
    }


    public static class Builder {

        private Integer code;
        private String msg;
        private Object data;

        public Builder code(Integer code) {
            this.code = code;
            return this;
        }

        public Builder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }


        public Response build() {
            Response response = new Response();
            response.code = this.code;
            response.msg = this.msg;
            response.data = this.data;
            return response;
        }
    }

}
