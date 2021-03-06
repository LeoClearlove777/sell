package com.qcl.userwechat.service.impl;

import com.qcl.dataobject.User;
import com.qcl.userwechat.repository.UserRepository;
import com.qcl.userwechat.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类目
 * Created by qcl on 2017/12/16.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;


    @Override
    public User findOne(String openid) {
        return repository.findByopenid(openid);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }


}
