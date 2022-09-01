package com.cm.admin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdminApp {

    public static void main(String[] args) {
        System.out.println("url:  http://localhost:8889/pages/main.html");
        SpringApplication.run(AdminApp.class,args);
    }
}
