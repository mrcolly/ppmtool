package com.mrcolly.ppmtool.repositories;

import com.mrcolly.ppmtool.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
