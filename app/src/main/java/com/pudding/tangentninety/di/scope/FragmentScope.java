package com.pudding.tangentninety.di.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Error on 2017/6/22 0022.
 */

@Scope
@Retention(RUNTIME)
public @interface FragmentScope {
}
