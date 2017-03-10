package com.purchase_agent.webapp.giraffe.internal;

import org.glassfish.hk2.api.AnnotationLiteral;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lukez on 3/9/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface RequestTime {
    class Impl extends AnnotationLiteral<RequestTime> implements RequestTime {
    }
}
