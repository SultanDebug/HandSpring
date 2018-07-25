package mvc.action;

import mvc.action.service.DemoService;
import webMvc.annotation.MyAutowired;
import webMvc.annotation.MyController;
import webMvc.annotation.MyRequestMapping;
import webMvc.annotation.MyRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sultan on 2018/7/20.
 */
@MyController
@MyRequestMapping("/demo")
public class DemoAction {
    @MyAutowired
    private DemoService demoService;

    @MyRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse  response, @MyRequestParam String name){
        String rep = demoService.get(name);

        try {
            response.getWriter().write(rep);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
