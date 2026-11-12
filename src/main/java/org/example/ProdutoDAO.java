package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    // Parâmetros de conexão direta via JDBC para MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/loja_db";
    private static final String USER = "root";
    private static final String PASS = "josepassword";

    /**
     * 1. Cria a tabela automaticamente via JDBC se ela não existir.
     * 2. Se a tabela não tiver registos, insere dados iniciais de teste.
     */
    public void inicializarBancoSeNecessario() throws SQLException {
        String sqlCriarTabela = """
            CREATE TABLE IF NOT EXISTS produtos (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nome VARCHAR(150) NOT NULL,
                preco DECIMAL(10, 2) NOT NULL,
                quantidade INT NOT NULL
            );
        """;

        String sqlVerificarDados = "SELECT COUNT(*) AS total FROM produtos";

        String sqlInserir = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            // Cria a tabela
            stmt.execute(sqlCriarTabela);

            // Verifica se já existem dados inseridos
            try (ResultSet rs = stmt.executeQuery(sqlVerificarDados)) {
                if (rs.next() && rs.getInt("total") == 0) {
                    System.out.println("Tabela vazia. A inserir registos iniciais via JDBC...");

                    try (PreparedStatement pstmt = conn.prepareStatement(sqlInserir)) {
                        // Registo 1
                        pstmt.setString(1, "Teclado Mecânico RGB");
                        pstmt.setBigDecimal(2, new java.math.BigDecimal("3450.00"));
                        pstmt.setInt(3, 15);
                        pstmt.executeUpdate();

                        // Registo 2
                        pstmt.setString(1, "Rato Sem Fios Ergonómico");
                        pstmt.setBigDecimal(2, new java.math.BigDecimal("1890.50"));
                        pstmt.setInt(3, 28);
                        pstmt.executeUpdate();

                        // Registo 3
                        pstmt.setString(1, "Monitor 27 IPS 144Hz");
                        pstmt.setBigDecimal(2, new java.math.BigDecimal("18500.00"));
                        pstmt.setInt(3, 7);
                        pstmt.executeUpdate();

                        // Registo 4
                        pstmt.setString(1, "Headset Gamer USB-C");
                        pstmt.setBigDecimal(2, new java.math.BigDecimal("4200.00"));
                        pstmt.setInt(3, 12);
                        pstmt.executeUpdate();
                    }
                    System.out.println("Registos inseridos com sucesso!");
                }
            }
        }
    }

    /**
     * Consulta os dados da base de dados via JDBC e mapeia para a lista de POJOs.
     */
    public List<Produto> listarProdutos() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT id, nome, preco, quantidade FROM produtos ORDER BY id ASC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto p = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getBigDecimal("preco"),
                        rs.getInt("quantidade")
                );
                lista.add(p);
            }
        }
        return lista;
    }
}