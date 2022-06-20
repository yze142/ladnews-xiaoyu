package heima.pojo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloController {

    @GetMapping("/basic")
    public String test(Model model) {


        //1.纯文本形式的参数
        model.addAttribute("name", "freemarker");
        //2.实体类相关的参数
        
        Student student = new Student();
        student.setName("小明");
        student.setAge(18);
        model.addAttribute("stu", student);

        return "01-basic";
    }

    @GetMapping("/list")
    public String test2(Model model) {
        List list=new ArrayList();
        Student student=new Student();
        student.setAge(45);
        student.setName("zhefd");
        list.add(student);
        model.addAttribute(list);

        return "01-basic";
    }



}