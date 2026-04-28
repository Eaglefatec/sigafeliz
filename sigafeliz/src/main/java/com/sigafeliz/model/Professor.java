package com.sigafeliz.model;

import java.io.Serializable;
import java.util.Objects;

public class Professor implements Serializable {
    // Controle de versão para serialização
    private static final long serialVersionUID = 1L;

    private String nome;
    private String email; // Chave Primária (PK)

    public Professor(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Equals e HashCode baseados no Email (PK) garantem a unicidade no modelo Java
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return Objects.equals(email, professor.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}