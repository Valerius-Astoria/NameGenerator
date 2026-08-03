package com.valerius.namegenerator.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Faith {

    @Id
    private String id;
    private String code;
    private String name;
    @ManyToOne
    private Faith parent;
}
