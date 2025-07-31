package com.interguess.autoimpl.beta;


import com.interguess.autoimpl.beta.interfaces.User;
import com.interguess.autoimpl.beta.interfaces.UserImpl;

import java.util.UUID;

public class AutoImplBeta {

    public static void main(String[] args) {
       User user = new UserImpl(UUID.randomUUID());

       // user.setEmail("jonas.s@interguess.de");
    }
}
