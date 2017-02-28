package com.purchase_agent.webapp.giraffe.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;
import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;

import javax.inject.Inject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lukez on 2/27/17.
 */
public class UserAuthModelHandler implements AuthModelEncodeDecoder<UserAuthModel> {
    private static final Logger logger = Logger.getLogger(UserAuthModelHandler.class.getName());
    private final ObjectMapper objectMapper;

    @Inject
    public UserAuthModelHandler(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String encode(final UserAuthModel userAuthModel) {
        Preconditions.checkNotNull(userAuthModel);
        try {
            return BaseEncoding.base64().encode(this.objectMapper.writeValueAsString(userAuthModel).getBytes());
        } catch (final JsonProcessingException exp) {
            logger.log(Level.SEVERE, "failed to encode auth model", exp);
            throw new RuntimeException("failed to encode auth model");
        } catch (final Exception exp) {
            logger.log(Level.SEVERE, "unexpected error when encoding the user auth model!", exp);
            throw new RuntimeException("Unexpected error in auth model encoding!");
        }
    }

    @Override
    public UserAuthModel decode(final String encodedString) throws RuntimeException {
        Preconditions.checkArgument(Strings.isNullOrEmpty(encodedString));
        try {
            return this.objectMapper.readValue(BaseEncoding.base64().encode(encodedString.getBytes()), UserAuthModel.class);
        } catch (final IOException exp) {
            logger.log(Level.SEVERE, "failed to deserialize the user auth model");
            throw new RuntimeException("failed to deserialize the user auth model");
        } catch (final Exception exp) {
            logger.log(Level.SEVERE, "unexpected error when decode the user auth string!", exp);
            throw new RuntimeException("unexpected error when decode the user auth string");
        }
    }
}
