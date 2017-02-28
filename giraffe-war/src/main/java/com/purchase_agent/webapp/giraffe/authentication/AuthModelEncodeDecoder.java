package com.purchase_agent.webapp.giraffe.authentication;

/**
 * Created by lukez on 2/27/17.
 */
public interface AuthModelEncodeDecoder<T> {
    String encode(T model);
    T decode(String encodeString);
}
