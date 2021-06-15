package com.kinesysApp.kinesys.Entity;

import java.io.Serializable;
import java.util.List;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Data;

@Data
@Entity
public class ObraSocial implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String idObraSocial;
    private String nombre;
    private Long telefono;
    private String email;

    @ManyToMany
    private List<Profesional> listaProfesionales;

}
