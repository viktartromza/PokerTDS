package com.tromza.pokertds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // указываем, что контроллер в MVC
@RequestMapping("/") // указываем, на какой url откликается в HandlerMappinge
public class StartController {
    @GetMapping("/")// указываем на какой метод откликается в HandlerMappinge и /доп для значения @PathVariable
    public String getStart() {

        return "index";
    }
}
