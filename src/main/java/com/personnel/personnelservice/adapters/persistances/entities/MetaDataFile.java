package com.personnel.personnelservice.adapters.persistances.entities;

import com.personnel.personnelservice.core.models.enums.FileType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "meta_data_files")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE meta_data_files SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class MetaDataFile extends BaseEntity{

    private String fileName;

    private String extension;

    private String mimeType;

    @Enumerated(EnumType.STRING)
    private FileType type;

    private String path;

    private boolean deleted = false;

}

