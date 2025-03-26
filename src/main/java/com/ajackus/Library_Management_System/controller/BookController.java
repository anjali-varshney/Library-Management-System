package com.ajackus.Library_Management_System.controller;

import com.ajackus.Library_Management_System.pojo.BookReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("api/v1/books")
@Slf4j
public class BookController {

    @PostMapping("/add")
    public String addBook(@RequestBody BookReq bookReq){
        log.info("successfully invoked the addbook||bookReq: " + bookReq);

        return"Book add successfully";
    }

}
