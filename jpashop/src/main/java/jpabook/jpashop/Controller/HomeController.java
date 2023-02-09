package jpabook.jpashop.Controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {
/*
    @Slf4j를 사용하여 생략할 수 있따
    Logger log = LoggerFactory.getLogger(getClass());
*/

    @RequestMapping("/")
    public String home(){
        log.info("home controller");
        return "home";
    }



}
