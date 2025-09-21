package by.iba.repair_report.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

    /**
     * Перенаправляет все запросы (кроме тех, что начинаются с /api) на index.html.
     * Это необходимо для работы React Router.
     */
    @RequestMapping(value = {"/", "/{path:[^\\.]*}"})
    public String redirect() {
        return "forward:/index.html";
    }
}
