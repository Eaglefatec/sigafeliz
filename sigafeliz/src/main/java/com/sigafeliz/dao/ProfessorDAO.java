    package com.sigafeliz.dao;
    
    import com.sigafeliz.infra.ConexaoDB;
    import com.sigafeliz.model.Professor;
    
    import java.sql.*;
    import java.util.ArrayList;
    import java.util.List;
    
    
    public class ProfessorDAO {
    
        public List<Professor> listarTodos() throws SQLException {
            List<Professor> lista = new ArrayList<>();
            String sql = "SELECT nome, email FROM professor";
    
            try (Connection con = ConexaoDB.getConexao();
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
    
                while (rs.next()) {
                    lista.add(new Professor(rs.getString("nome"), rs.getString("email")));
                }
            }
            return lista;
        }
    
        public Professor buscarPorEmail(String email) throws SQLException {
            String sql = "SELECT nome, email FROM professor WHERE email = ?";
    
            try (Connection con = ConexaoDB.getConexao();
                 PreparedStatement ps = con.prepareStatement(sql)) {
    
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Professor(rs.getString("nome"), rs.getString("email"));
                    }
                }
            }
            return null;
        }
    
        public Professor buscarPorNome(String nome) throws SQLException {
            String sql = "SELECT nome, email FROM professor WHERE LOWER(nome) = LOWER(?)";
    
            try (Connection con = ConexaoDB.getConexao();
                 PreparedStatement ps = con.prepareStatement(sql)) {
    
                ps.setString(1, nome);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Professor(rs.getString("nome"), rs.getString("email"));
                    }
                }
            }
            return null;
        }
    
        public void salvar(Professor p) throws SQLException {
            String sql = "INSERT INTO professor (nome, email) VALUES (?, ?)";
    
            try (Connection con = ConexaoDB.getConexao();
                 PreparedStatement ps = con.prepareStatement(sql)) {
    
                ps.setString(1, p.getNome());
                ps.setString(2, p.getEmail());
                ps.executeUpdate();
            }
        }

        public void atualizar(Professor p) throws SQLException {
            String sql = "UPDATE professor SET nome = ? WHERE email = ?";
            try (Connection con = ConexaoDB.getConexao();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getNome());
                ps.setString(2, p.getEmail());
                ps.executeUpdate();
            }
        }

        public void excluir(String email) throws SQLException {
            String sql = "DELETE FROM professor WHERE email = ?";
            try (Connection con = ConexaoDB.getConexao();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.executeUpdate();
            }
        }
    }