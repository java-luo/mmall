package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerReponse;
import com.mmall.pojo.User;
import com.mmall.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private UserServer userServer;

    @RequestMapping("login.do")
    public ServerReponse<User> login(String username, String password, HttpSession session) {
        ServerReponse reponse = userServer.login(username, password);
        if(reponse.isSccess()){
            //将用户信息添加到session中
            session.setAttribute(Const.CURRENT_USER,reponse.getData());
        }
        return reponse;
    }
    @RequestMapping("logout.do")
    public ServerReponse logout(HttpSession session){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            session.removeAttribute(Const.CURRENT_USER);
            return ServerReponse.createByMessageSuccess("登出成功");
        }else{
            return ServerReponse.createByMessageError("用户未登录");
        }
    }
    @RequestMapping("getUserInfo.do")
    public ServerReponse<User> getUserInfo(HttpSession session) {
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerReponse.createBySuccess(user);
        }else{
            return ServerReponse.createByMessageError("用户未登录");
        }
    }

    @RequestMapping("register.do")
    public ServerReponse<User> register(User user) {

       return  userServer.register(user);
    }

    @RequestMapping("checkVaild.do")
    public ServerReponse<String> checkVaild(String str, String type) {
        return  userServer.checkVaild(str,type);
    }

    @RequestMapping("forget_get_question.do")
    public ServerReponse<String> forget_get_question(String username) {
        return  userServer.forget_get_question(username);
    }

    @RequestMapping("forget_check_answer.do")
    public ServerReponse<String> forget_check_answer(String username,String question,String answer) {
        return  userServer.check_answer( username, question, answer);
    }

    @RequestMapping("forget_reset_password.do")
    public ServerReponse<String> forget_reset_password(String username,String password,String token) {
        return  userServer.forget_reset_password( username, password, token);
    }

    @RequestMapping("reset_password.do")
    public ServerReponse<String> reset_password(String password,String newPassword,HttpSession session) {
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerReponse.createByMessageError("用户未登录");
        }
        return  userServer.reset_password( user.getUsername(),password,newPassword);
    }

    @RequestMapping("update_information.do")
    public ServerReponse<User> update_information(User userInfo,HttpSession session) {
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerReponse.createByMessageError("用户未登录");
        }
        userInfo.setId(user.getId());
        ServerReponse<User> reponse = userServer.update_information(userInfo);
        if(reponse.isSccess()){
            //将用户信息添加到session中
            reponse.getData().setUsername(user.getUsername());
            session.setAttribute(Const.CURRENT_USER,reponse.getData());
        }
        return  reponse;
    }

}
