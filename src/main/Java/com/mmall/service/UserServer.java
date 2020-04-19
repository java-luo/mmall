package com.mmall.service;

import com.mmall.common.ServerReponse;
import com.mmall.pojo.User;

public interface UserServer {

    ServerReponse<User> login(String username,String password);


    ServerReponse<User> register(User user);

    ServerReponse<String> checkVaild(String str, String type);

    ServerReponse<String> forget_get_question(String username);

    ServerReponse<String> check_answer(String username, String question, String answer);

    ServerReponse<String> forget_reset_password(String username, String password, String token);

    ServerReponse<String> reset_password(String username, String password, String newPassword);

    ServerReponse<User> update_information(User userInfo);
}
