package com.ruidev.framework.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;

/**
 * 基础数据检验
 *
 * @author	 	锐开科技 
 * @Copyright 	www.ruidev.com All rights reserved. 
 */
public class ValidationUtil {

	/**
     * 使用hibernate的注解来进行验证
     */
    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * 功能描述: <br>
     * 〈注解验证参数〉
     *
     * @param obj
     */
    public static <T> Set<ConstraintViolation<T>> validate(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        return constraintViolations;
    }
}
