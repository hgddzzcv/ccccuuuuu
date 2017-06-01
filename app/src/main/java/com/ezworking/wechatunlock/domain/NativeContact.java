package com.ezworking.wechatunlock.domain;

import java.util.List;

/**
 * Created by wangchao on 2017/5/14 0014.
 */

public class NativeContact {



        private String identifier;

        private String name;

        private String company;

        private List<String> phones;

        private List<String> emails;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;

    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}
