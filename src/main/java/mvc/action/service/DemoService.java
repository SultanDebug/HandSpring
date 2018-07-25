package mvc.action.service;

import webMvc.annotation.MyService;

/**
 * Created by sultan on 2018/7/22.
 */
@MyService
public class DemoService {
    public String get(String str){
        return "Demo Test Name : "+str;
    }
}
