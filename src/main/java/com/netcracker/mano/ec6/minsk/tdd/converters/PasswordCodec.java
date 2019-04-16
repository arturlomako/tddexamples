package com.netcracker.mano.ec6.minsk.tdd.converters;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordCodec {

  public String code(String password) {
    return DigestUtils.md5Hex(password);
  }

  public boolean test(String password, String hash) {
    return DigestUtils.md5Hex(password).equals(hash);
  }

}
