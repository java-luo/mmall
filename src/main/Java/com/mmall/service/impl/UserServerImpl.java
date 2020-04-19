package com.mmall.service.impl;
import com.mmall.common.Const;
import com.mmall.common.ServerReponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.UserServer;
import com.mmall.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServerImpl implements UserServer {

    @Autowired
    private UserMapper userMapper;

    public ServerReponse<User> login(String username,String password) {
        User user=userMapper.selectLogin(username,MD5Util.MD5EncodeUtf8(password));
        if (user != null) {
            user.setPassword(StringUtils.EMPTY);
            return ServerReponse.createBySuccess("登陆成功",user);
        }else{
            return ServerReponse.createByMessageError("密码输入错误");
        }

    }
    public ServerReponse<User> register(User user) {
        if (userMapper.checkUsername(user.getUsername())>0){
            return ServerReponse.createByMessageError("用户名已存在");
        }else if(userMapper.checkEmail(user.getEmail())>0){
            return ServerReponse.createByMessageError("邮箱已存在");
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int count=userMapper.insert(user);
        if(count>0){
            return ServerReponse.createByMessageSuccess("注册成功");
        }
        return ServerReponse.createByMessageError("注册失败 请重新注册");
    }
    /* if(userMapper.checkEmail(str)>0){
        return ServerReponse.createByMessageError("邮箱已存在");
    }else{
        return ServerReponse.createByError();
    }*/
    public ServerReponse<String> checkVaild(String str, String type) {
        if ( "username".equals(type)) {
            if(userMapper.checkUsername(str)>0){
                return ServerReponse.createByMessageError("用户名已存在");
            }else{
                return ServerReponse.createByMessageSuccess("可以使用");
            }
        }
        if("email".equals(type)){
            if(userMapper.checkEmail(str)>0){
                return ServerReponse.createByMessageError("邮箱已存在");
            }else{
                return ServerReponse.createByMessageSuccess("可以使用");
            }
        }
            return ServerReponse.createByMessageError("参数错误");
    }

    public ServerReponse<String> forget_get_question(String username) {
        String question= userMapper.getQuestion(username);
        if(StringUtils.isBlank(question)){
            return ServerReponse.createByMessageError("用户还未设置问题");
        }
        return ServerReponse.createBySuccess(question);
    }

    public ServerReponse<String> check_answer(String username, String question, String answer) {
        int count = userMapper.checkAnswer(username, question, answer);
        if(count==0){
            return ServerReponse.createByMessageError("问题的答案错误");
        }
        String token = UUID.randomUUID().toString();
        TokenCache.setKey("token_"+username,token);
        return ServerReponse.createBySuccess(token);
    }
    public ServerReponse<String> forget_reset_password(String username, String password, String token) {
      String cacheToken=TokenCache.getValue("token_"+username);
      if(StringUtils.isEmpty(cacheToken)|| !token.equals(cacheToken)){
          return ServerReponse.createByMessageError("token错误 请重新去回答问题");
      }
      String  newPssword = MD5Util.MD5EncodeUtf8(password);
     int count= userMapper.forget_reset_password( username,newPssword);
      if(count>0){
          return ServerReponse.createByMessageSuccess("重置密码成功");
      }
      return ServerReponse.createByMessageError("服务器繁忙 请重新尝试");
    }

    public ServerReponse<String> reset_password(String username, String password, String newPassword) {
        User user = userMapper.selectLogin(username, MD5Util.MD5EncodeUtf8(password));
        if(user==null){
            return ServerReponse.createByMessageError("旧密码输入错误");
        }
        int count = userMapper.forget_reset_password(username, MD5Util.MD5EncodeUtf8(newPassword));
        if(count>0){
            return ServerReponse.createByMessageSuccess("密码修改成功");
        }
        return ServerReponse.createByMessageError("未知错误,请重新尝试");
    }

    public ServerReponse<User> update_information(User userInfo) {
        userInfo.setPassword(null);
        userInfo.setRole(null);
        userInfo.setUsername(null);
        int count = userMapper.updateByPrimaryKeySelective(userInfo);
        if(count>0){
            return ServerReponse.createBySuccess(userInfo);
        }
        return ServerReponse.createByMessageError("未知错误,请重新尝试");
    }


}
