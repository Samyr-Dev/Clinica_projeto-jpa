# 🏥 Gerenciamento de Pacientes para Clínica Médica

[![NPM](https://img.shields.io/npm/l/react)](https://github.com/Samyr-Dev/Clinica_projeto-jpa/blob/main/LICENSE)

## Sobre o Projeto

Este sistema foi desenvolvido para atender à necessidade de uma clínica médica de **gerenciar as informações de seus pacientes**. O aplicativo permite realizar as operações básicas de **Cadastro, Busca, Atualização e Remoção** de registros.

Embora o objetivo inicial fosse a persistência em arquivo CSV, a solução final foi implementada utilizando o **JDBC** para persistir os dados no banco **PostgreSQL**, garantindo maior robustez e integridade de dados.

## 🎯 Objetivos do Desenvolvimento

* **Modelar** uma classe `Paciente` com os atributos: `id` (int), `nome` (String), `cpf` (String), `dataNascimento` (LocalDate) e `telefone` (String).
* **Implementar** construtores e métodos Get/Set para a classe `Paciente`.
* **Criar** uma interface gráfica JavaFX para o gerenciamento de pacientes.
* **Implementar** as operações **CRUD** (Criar, Ler/Listar, Atualizar, Excluir) utilizando acesso direto ao banco de dados (DAO).

---

## 💻 Tecnologias Utilizadas

Esta seção reflete as tecnologias utilizadas no projeto final (JDBC/PostgreSQL) e as ferramentas de build.

### Back-end & Persistência

| Componente | Tecnologia | Versão Principal | Notas |
| :--- | :--- | :--- | :--- |
| **Linguagem Principal** | **Java** | 17 (Target/Release) | Linguagem de implementação e plataforma de execução. |
| **Persistência** | **JDBC Puro** | - | Utilizado para conexão direta e execução de comandos SQL, substituindo frameworks ORM. |
| **Banco de Dados** | **PostgreSQL** | 42.7.2 (Driver) | SGBD utilizado para armazenamento persistente dos dados de `pacientes`. |
| **Build & Dependências** | **Maven** | 3.x | Gerenciador de build. |

### Front-end & Interface

| Componente | Tecnologia | Notas |
| :--- | :--- | :--- |
| **Interface Gráfica** | **JavaFX** | Framework para a interface desktop, com `TableView` para exibir resultados. |
| **Utilidades** | **Lombok** | Utilizado para reduzir código boilerplate. |

---

## 🚀 Como Executar o Projeto

Para rodar o projeto e se conectar ao banco de dados PostgreSQL:

### Pré-requisitos

* **Java Development Kit (JDK) 17**
* **Apache Maven** (Configurado no PATH)
* **Servidor PostgreSQL** (com PgAdmin4 ou similar para gerenciar o banco)

### Passos de Configuração

1.  **Clonar o Repositório:**
    ```bash
    git clone https://github.com/Samyr-Dev/Clinica_projeto-jpa
    ```

2.  **Configurar a Tabela no Banco de Dados:**
    * Garanta que o seu servidor PostgreSQL esteja rodando.
    * No seu cliente SQL (PgAdmin), crie o banco de dados `MEUBANCO` (ou ajuste o nome no código `DBConnection.java`).
    * Crie a tabela `pacientes` para corresponder ao modelo:

    ```sql
    -- Comando para criar a tabela esperada pelo código JDBC
    CREATE TABLE pacientes (
        id SERIAL PRIMARY KEY,
        nome VARCHAR(255) NOT NULL,
        cpf VARCHAR(14) UNIQUE NOT NULL,
        dataNascimento DATE,
        telefone VARCHAR(20)
    );
    ```

3.  **Configurar a Conexão JDBC:**
    * Verifique o arquivo **`DBConnection.java`** para confirmar se `URL`, `USER`, e `PASS` correspondem às suas credenciais do PostgreSQL.

4.  **Executar na IDE:**
    * Abra o projeto no IntelliJ IDEA.
    * Execute o comando `mvn clean install` no terminal para garantir que todas as dependências estejam resolvidas.
    * Execute a classe principal **`org.clinica.Main`**.

---

## 🧑‍💻 Autor

**Samyr Silva Tertuliano Deusdará**

🔗 [Linkedin](https://www.linkedin.com/in/samyrtertuliano)
