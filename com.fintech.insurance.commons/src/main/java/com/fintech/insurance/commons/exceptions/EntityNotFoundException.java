package com.fintech.insurance.commons.exceptions;

/**
 * @author Yong Li
 * @version 1.0.0
 * @since 2017/06/06
 */
public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String msg) {
        super(msg);
    }

}
