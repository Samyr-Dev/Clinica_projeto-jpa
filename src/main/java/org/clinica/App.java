package org.clinica;

import org.clinica.model.Paciente;
import org.clinica.utils.DBConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {

        // Dados de teste para inserir
        // Nota: O construtor do Paciente pode precisar ser ajustado se você removeu o ID
        Paciente p = new Paciente(0, "João Silva", "12345678900", java.time.LocalDate.of(1990, 1, 1), "99999-9999");

        // Comando SQL para inserir dados
        String sql = "INSERT INTO pacientes (nome, cpf, dataNascimento, telefone) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); // Abre a conexão
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepara o comando SQL

            // 1. Define os parâmetros
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getCpf());
            // 3. Converte LocalDate do Paciente para java.sql.Date
            stmt.setDate(3, Date.valueOf(p.getDataNascimento()));
            stmt.setString(4, p.getTelefone());

            // 4. Executa a inserção
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Paciente salvo com sucesso via JDBC!");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar paciente (JDBC): " + e.getMessage());
            e.printStackTrace();
        }
    }
}