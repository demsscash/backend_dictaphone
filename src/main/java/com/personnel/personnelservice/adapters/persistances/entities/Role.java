package com.personnel.personnelservice.adapters.persistances.entities;


import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE roles SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Role extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private boolean deleted = false;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

}
