package com.mrcolly.ppmtool.services;

import com.mrcolly.ppmtool.domain.User;
import com.mrcolly.ppmtool.exceptions.CustomException;
import com.mrcolly.ppmtool.repositories.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

	private UserRepository userRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private ValidationErrorService validationErrorService;
	
	public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ValidationErrorService validationErrorService) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.validationErrorService = validationErrorService;
	}
	
	public User saveUser(User newUser, BindingResult result){
		try {
			
			Map errorMap = validationErrorService.MapValidation(result);
			if (errorMap != null) {
				throw new CustomException("object validation error", errorMap);
			}
			
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			return userRepository.save(newUser);
		}catch (DataAccessException e){
			Map<String,String> errorMap = new HashMap<>();
			errorMap.put("error", e.getRootCause().getMessage());
			throw new CustomException("SQL exception",errorMap);
		}
	}
}
