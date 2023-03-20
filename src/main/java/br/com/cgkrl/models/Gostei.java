package br.com.cgkrl.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gostei")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gostei {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String uid; // Like UID combines user and music uids
    @OneToOne
    private Usuario usuario;
    @OneToOne
    private Musica musica;
}