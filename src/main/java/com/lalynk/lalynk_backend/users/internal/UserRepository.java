package com.lalynk.lalynk_backend.users.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>
{
}
