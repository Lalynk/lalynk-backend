package com.lalynk.lalynk_backend.users.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>
{
    Optional<User> findByAuth0Subject(String auth0Subject);
}
