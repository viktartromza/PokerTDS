package com.tms.controller;

import com.tms.annotation.GetTimeAnnotation;
import com.tms.domain.User;
import com.tms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller // указываем, что контроллер в MVC
@RequestMapping("/user") // указываем, на какой url откликается в HandlerMappinge
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }// конструктор контроллера
@GetTimeAnnotation
    @GetMapping("/{id}")// указываем на какой метод откликается в HandlerMappinge и /доп для значения @PathVariable
    public String getUserById(@PathVariable int id, Model model) {
        //    log.info("doing/user Get method!");
        User user = UserService.getUserById(id);
        model.addAttribute("user", user);
        return "singleUser";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError o : bindingResult.getAllErrors()) {
                System.out.println(o);
            }
            return "unsuccessfully";
        } else if (userService.createUser(user)) {
            return "successfully";
        } else
            return "unsuccessfully";
    }

    @PutMapping("/update")
    public String updateUser(@ModelAttribute User user) {
        if (userService.updateUser(user))
            return "successfully";
        else
            return "unsuccessfully";
    }
}