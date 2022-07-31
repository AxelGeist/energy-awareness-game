package server.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class TestController {

    /**
     * Controller mapping to nothing
     * @return a string saying hello world
     */
    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Hello world!";
    }
}