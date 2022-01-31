package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Entity;

@Entity
public class User extends AbstractEntity {
    private String passwordSalt;
    private String passwordHash;

    public User(){}

    public User(String password) {
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHash = DigestUtils.sha1Hex(password + passwordSalt);

    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
