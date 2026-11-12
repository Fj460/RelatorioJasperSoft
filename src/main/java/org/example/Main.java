package org.example;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            ProdutoDAO dao = new ProdutoDAO();

            // 1. Garante que a tabela e os dados existem no MySQL via JDBC
            dao.inicializarBancoSeNecessario();

            // 2. Procura os dados na BD
            List<Produto> produtos = dao.listarProdutos();

            // 3. Encapsula os dados em memória para o Jasper
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(produtos);

            // 4. Parâmetros
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("TITULO_RELATORIO", "Relatório de Inventário Geral");

            // 5. Compila e preenche o relatório
            InputStream reportStream = Main.class.getResourceAsStream("/relatorios/relatorio_produtos.jrxml");
            if (reportStream == null) {
                System.err.println("Erro: Ficheiro .jrxml não encontrado em src/main/resources/relatorios/");
                return;
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

            // 6. Exibe a janela com o relatório pronto
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setTitle("Visualização do Relatório");
            viewer.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}