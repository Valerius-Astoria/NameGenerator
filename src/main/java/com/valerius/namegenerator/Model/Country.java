package com.valerius.namegenerator.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Country {

    @Id
    private String id;
    private String code;
    private String name;
}
