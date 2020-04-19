package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectLogin(String username, String password);

    int checkUsername(String username);

    int checkEmail(String Email);

    String getQuestion(String username);

    int checkAnswer(@Param(value = "username") String username, @Param(value = "question")String question, @Param(value = "answer")String answer);

    int forget_reset_password(@Param(value = "username")String username,@Param(value = "password") String password);
}