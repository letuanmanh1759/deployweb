package com.springboot.testbackend.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.springboot.testbackend.entity.Users;
import com.springboot.testbackend.form.UsersForm;
import com.springboot.testbackend.repository.UsersRepository;

@Component
public class UsersValidator implements Validator {

	@Autowired
	private UsersRepository usersRepository;

	private static final String ID_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{3,}$";
	private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,}$";
	private static final String EMAIL_PATTERN = ".+@.+\\..+";
	private static final String PHONE_PATTERN = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{4}[\\s.-]\\d{4}$";

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == UsersForm.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		UsersForm usersForm = (UsersForm) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "NotEmpty.appUserForm.id");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.appUserForm.password");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.appUserForm.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.appUserForm.email");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty.appUserForm.phone");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "NotEmpty.appUserForm.address");

		String id = usersForm.getId();
		String password = usersForm.getPassword();
		String email = usersForm.getEmail();
		String phone = usersForm.getPhone();
		if (id != null && id.length() > 0) {
			if (!id.matches(ID_PATTERN)) {
				errors.rejectValue("id", "Duplicate.appUserForm.id");
			} else {
				Optional<Users> dbUser = usersRepository.findById(id);
				if (dbUser.isPresent()) {
					errors.rejectValue("id", "Duplicate.appUserForm.id");
				}
			}
		}
		if (password.length() > 0 && !password.matches(PASSWORD_PATTERN)) {
			errors.rejectValue("password", "Pattern.appUserForm.password");
		}
		if (email.length() > 0 && !email.matches(EMAIL_PATTERN)) {
			errors.rejectValue("email", "Pattern.appUserForm.email");
		}
		if (phone.length() > 0 && !phone.matches(PHONE_PATTERN)) {
			errors.rejectValue("phone", "Pattern.appUserForm.phone");
		}

	}

}
