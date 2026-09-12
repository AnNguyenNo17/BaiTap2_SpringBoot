package com.example.demo.dto;

import jakarta.validation.constraints.Size;

public class ProfileForm {

    @Size(max = 150, message = "Họ tên tối đa 150 ký tự")
    private String fullname;

    @Size(max = 20, message = "Số điện thoại tối đa 20 ký tự")
    private String phone;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
