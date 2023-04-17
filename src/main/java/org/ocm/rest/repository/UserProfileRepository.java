package org.ocm.rest.repository;

import org.ocm.dto.UserProfileDTO;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfileDTO, String> {
}
