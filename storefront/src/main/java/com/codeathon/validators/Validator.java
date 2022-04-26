package com.codeathon.validators;

import java.util.List;

import com.codeathon.data.ValidationError;

public interface Validator<T> {

	List<ValidationError> validate(T entity);
}
