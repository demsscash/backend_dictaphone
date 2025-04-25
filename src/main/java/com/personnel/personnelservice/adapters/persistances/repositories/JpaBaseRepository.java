package com.personnel.personnelservice.adapters.persistances.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface JpaBaseRepository <T extends BaseEntity> extends JpaRepository<T, UUID> {

}