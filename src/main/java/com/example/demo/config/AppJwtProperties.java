package com.example.demo.config;
//file nay doc cau hinh JWT tu application.yml va validate cac gia tri do khi khoi dong app

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated //kiem tra tinh hop le
@ConfigurationProperties(prefix = "app.jwt")//gom cac cau hinh app.jwt gan vao lop nay
public record AppJwtProperties(
        @NotBlank
        String issuer,//ten app phat hanh token, khong duoc de trong
        //vd: yo-app, my-company
        @NotBlank String secret,
        //khoa bi mat de ky JWT,khong duoc de trong
        @Min(1) long accessTokenTtlMinutes,
        //thoi gian song cua access token(phut) , toi thieu 1 phut thuong 15 -30 phut
        @Min(1) long refreshTokenTtDays
        //thoi gian song cua refresh token(ngay), toi thieu 1 ngay thuong 7 -30 ngay
) {
}
