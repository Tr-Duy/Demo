package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

//cau hinh model mapper - 1 thu vien dung de chuyen doi object sang object
@Configuration//danh dau day la file config
public class AppConfig {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)// Tự động copy dữ liệu bằng tên biến, không cần thông qua hàm get/set.
                .setMatchingStrategy(MatchingStrategies.STRICT)//: Ép tên biến phải giống nhau 100% mới cho copy, tránh đoán mò dẫn đến map nhầm.
                .setAmbiguityIgnored(false);//Nếu có tên biến nào mập mờ, trùng lặp là báo lỗi ngay chứ không tự ý bỏ qua.
        return modelMapper;
    }
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
