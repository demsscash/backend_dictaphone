package com.personnel.personnelservice.adapters.persistances.entities;

import com.personnel.personnelservice.core.models.enums.PermissionEnum;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE permissions SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Permission extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String name;
    private String description;

    private boolean deleted = false;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();

    public Permission(PermissionEnum permissionEnum) {
        this.name = permissionEnum.name();
        this.description = permissionEnum.getDescription();
    }
}