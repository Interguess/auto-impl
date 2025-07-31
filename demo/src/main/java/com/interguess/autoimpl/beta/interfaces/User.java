package com.interguess.autoimpl.beta.interfaces;

import com.interguess.autoimpl.api.annotations.AutoField;
import com.interguess.autoimpl.api.annotations.AutoImpl;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@AutoImpl
public interface User {

    @AutoField("id")
    @NotNull UUID getId();

    @AutoField("name")
    @NotNull String getName();

    @AutoField("name")
    void setName(@NotNull String name);

    @AutoField("email")
    @NotNull String getEmail();

    @AutoField("email")
    void setEmail(@NotNull String email);
}
