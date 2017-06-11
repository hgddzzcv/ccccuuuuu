package com.ezworking.wechatunlock.domain;

// FIXME generate failure  field _$PostMsg27

import java.io.Serializable;
import java.util.List;

/**
 * Created by sxj on 2017/6/9.
 */

public class SysInfoBean implements Serializable {


    /**
     * appVersion : 1.0
     * allowUploadContacts : 1
     * postMsg : {"pId":"6","picLink":["http://www.ezworking.net/backend/images/aa84ee01b895489aa3b8122f8e95d18f.jpg"],"body":"大家好 才是真的好\r\n\r\n瑞典77%的国土面积被森林和湖泊所覆盖，100%的国土面积在冬天则会被冰雪覆盖，在这里，SUV和四驱的能见度远高于欧洲其他地区。因此，Volvo会成为\u201c越野旅行车\u201d的发明者便不足为奇了。早在1996年，Volvo便发布了一款850 AWD estate车型，旅行车的实用，结合AWD的设定、比前驱车款稍高的离地间隙，让850 AWD成为名副其实的全能、全路况选手"}
     * timestamp : 2017-06-09 18:27:29
     * success : 1
     * errorCode :
     * errorMsg :
     * error : 0
     */

    private String appVersion;
    private String allowUploadContacts;
    private PostMsgBean postMsg;
    private String timestamp;
    private String success;
    private String errorCode;
    private String errorMsg;
    private int error;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAllowUploadContacts() {
        return allowUploadContacts;
    }

    public void setAllowUploadContacts(String allowUploadContacts) {
        this.allowUploadContacts = allowUploadContacts;
    }

    public PostMsgBean getPostMsg() {
        return postMsg;
    }

    public void setPostMsg(PostMsgBean postMsg) {
        this.postMsg = postMsg;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public static class PostMsgBean {
        /**
         * pId : 6
         * picLink : ["http://www.ezworking.net/backend/images/aa84ee01b895489aa3b8122f8e95d18f.jpg"]
         * body : 大家好 才是真的好

         瑞典77%的国土面积被森林和湖泊所覆盖，100%的国土面积在冬天则会被冰雪覆盖，在这里，SUV和四驱的能见度远高于欧洲其他地区。因此，Volvo会成为“越野旅行车”的发明者便不足为奇了。早在1996年，Volvo便发布了一款850 AWD estate车型，旅行车的实用，结合AWD的设定、比前驱车款稍高的离地间隙，让850 AWD成为名副其实的全能、全路况选手
         */

        private String pId;
        private String body;
        private List<String> picLink;

        public String getPId() {
            return pId;
        }

        public void setPId(String pId) {
            this.pId = pId;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public List<String> getPicLink() {
            return picLink;
        }

        public void setPicLink(List<String> picLink) {
            this.picLink = picLink;
        }

        @Override
        public String toString() {
            return "PostMsgBean{" +
                    "pId='" + pId + '\'' +
                    ", body='" + body + '\'' +
                    ", picLink=" + picLink +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SysInfoBean{" +
                "appVersion='" + appVersion + '\'' +
                ", allowUploadContacts='" + allowUploadContacts + '\'' +
                ", postMsg=" + postMsg +
                ", timestamp='" + timestamp + '\'' +
                ", success='" + success + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", error=" + error +
                '}';
    }
}
