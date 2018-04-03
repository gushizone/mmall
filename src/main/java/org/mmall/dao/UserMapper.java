package org.mmall.dao;

import org.apache.ibatis.annotations.Param;
import org.mmall.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    /**
     * 什么属性不为空更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * 全更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(User record);

    /**
     * 验证用户是否存在
     * @param username
     * @return
     */
    int checkUsername(String username);

    int checkEmail(String email);

    /**
     * 检查用户登陆
     * @param username
     * @param password
     * @return
     */
    User selectLogin(@Param("username") String username, @Param("password") String password);

    /**
     * 获取密保问题
     * @param username
     * @return
     */
    String selectQuestionByUsername(String username);

    /**
     * 检查密保问题
     * @param username
     * @param question
     * @param answer
     * @return
     */
    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    /**
     * 重置密码
     * @param username
     * @param passwordNew
     * @return
     */
    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    /**
     * 检查用户名/密码
     * @param password
     * @param userId
     * @return
     */
    int checkPassword(@Param("password") String password, @Param("userId") Integer userId);

    /**
     * 检查email是否为他人占用
     * @param email
     * @param userId
     * @return
     */
    int checkEmailByUserId(@Param("email") String email, @Param("userId") Integer userId);
}