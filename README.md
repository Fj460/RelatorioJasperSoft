# Integração Java (NetBeans) + Jaspersoft Studio via JDBC

Este projeto demonstra como integrar relatórios desenvolvidos no **Jaspersoft Studio** numa aplicação **Java (Maven)** sem acoplar o ficheiro do relatório diretamente à base de dados.

---

## Como Funciona (Arquitetura)

Ao contrário da abordagem tradicional em que o relatório executa a sua própria query SQL, neste projeto a **aplicação Java é a única responsável por comunicar com a base de dados**:

```text
     [MySQL]
       │
       ▼ (1. Consulta via JDBC puro)
[ProdutoDAO.java] ──> Mapeia ResultSet para List<Produto>
       │
       ▼ (2. Empacota os dados em memória)
[JRBeanCollectionDataSource] + [Parâmetros (Map)]
       │
       ▼ (3. Compila e preenche o layout .jrxml)
[JasperReports Engine] ──> [Janela JasperViewer (Swing)]


```
### 2. Configurar a Conexão com a Base de Dados (`ProdutoDAO.java`)

Antes de executar o projeto, abre o ficheiro `src/main/java/com/exemplo/dao/ProdutoDAO.java` e verifica as três constantes de conexão no topo da classe:

```java
private static final String URL = "jdbc:mysql://localhost:3306/loja_db";
private static final String USER = "root";
private static final String PASS = "josepassword";