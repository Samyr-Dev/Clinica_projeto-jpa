package org.clinica.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.clinica.dao.PacienteDao;
import org.clinica.model.Paciente;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PacienteController implements Initializable {

    @FXML public TextField txtNome;
    @FXML public TextField txtID;
    @FXML public TextField txtCPF;
    @FXML public DatePicker txtDataNascimento;
    @FXML public TextField txtTelefone;

    // CORREÇÃO: Tipo da coluna deve ser LocalDate (não DatePicker)
    @FXML private TableColumn<Paciente, Integer> colunaID;
    @FXML private TableColumn<Paciente, String> colunaNome;
    @FXML private TableColumn<Paciente, String> colunaCPF;
    @FXML private TableColumn<Paciente, LocalDate> colunaDataNascimento; // CORREÇÃO
    @FXML private TableColumn<Paciente, String> colunaTelefone;
    @FXML private TableView<Paciente> tabelaDados;

    // Usaremos estes dois:
    private PacienteDao pacienteDAO;
    private ObservableList<Paciente> observableListPacientes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializa o DAO e a lista observável
        this.pacienteDAO = new PacienteDao();
        this.observableListPacientes = FXCollections.observableArrayList();
        this.tabelaDados.setItems(this.observableListPacientes);

        vinculoComTabela();
        carregarTodosPacientes(); // Carrega os dados do banco ao iniciar
    }

    public void vinculoComTabela() {
        colunaID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        // Coluna de data: precisa do PropertyValueFactory
        colunaDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
    }

    // Novo método para buscar dados do banco
    private void carregarTodosPacientes() {
        try {
            List<Paciente> pacientesDoBanco = pacienteDAO.listarTodos();
            this.observableListPacientes.clear();
            this.observableListPacientes.addAll(pacientesDoBanco);
        } catch (SQLException e) {
            e.printStackTrace();
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Banco de Dados", "Falha ao carregar pacientes: " + e.getMessage());
        }
    }

    // Método para ler dados do formulário e criar um objeto Paciente
    private Paciente getPacienteFromForm() {
        // ID é necessário para atualizar/excluir. 0 se for novo.
        int id = txtID.getText().isEmpty() ? 0 : Integer.parseInt(txtID.getText());

        return new Paciente(
                id,
                txtNome.getText(),
                txtCPF.getText(),
                txtDataNascimento.getValue(),
                txtTelefone.getText()
        );
    }

    @FXML
    protected void onsalvarPacienteClick() {
        Paciente novoPaciente = getPacienteFromForm();

        try {
            // Salva no banco (o método salvar do DAO não precisa de ID pré-definido)
            pacienteDAO.salvar(novoPaciente);

            limparFormulario();
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Paciente salvo no banco com sucesso!");

        } catch (SQLException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro ao Salvar", "Falha ao salvar paciente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // MÉTODOS DE BUSCA E ATUALIZAÇÃO (Adaptações)

    @FXML
    protected void onlistarPacientesClick(){
        String nomeBuscado = txtNome.getText();

        // A lógica de "se vazio, lista todos" está agora dentro do DAO.
        // Assim, podemos usar o mesmo bloco try-catch.
        try {
            List<Paciente> pacientesFiltrados = pacienteDAO.buscarPorNome(nomeBuscado);

            // Limpa a lista visual e adiciona os resultados
            this.observableListPacientes.clear();
            this.observableListPacientes.addAll(pacientesFiltrados);

            // Não é necessário chamar tabelaDados.setItems() novamente, pois observableListPacientes já está ligado à tabela.

            // Opcional: Mostrar alerta se a busca retornar vazio
            if (pacientesFiltrados.isEmpty() && !nomeBuscado.trim().isEmpty()) {
                exibirAlerta(Alert.AlertType.INFORMATION, "Busca Vazia", "Nenhum paciente encontrado com o nome: " + nomeBuscado);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Busca", "Falha ao buscar pacientes: " + e.getMessage());
        }
    }

    @FXML
    protected void onAtualizarPacienteClick(){
        // 1. Validação inicial do ID (se o campo está vazio ou inválido)
        String idText = txtID.getText();
        if (idText.isEmpty()) {
            exibirAlerta(Alert.AlertType.WARNING, "ID Inválido", "Por favor, digite o ID do paciente a ser atualizado.");
            return;
        }

        // Converte o ID e cria o objeto a ser enviado
        int id;
        try {
            id = Integer.parseInt(idText);
            if (id <= 0) {
                exibirAlerta(Alert.AlertType.WARNING, "ID Inválido", "O ID deve ser um número positivo.");
                return;
            }
        } catch (NumberFormatException e) {
            exibirAlerta(Alert.AlertType.ERROR, "ID Inválido", "O ID deve ser um número inteiro.");
            return;
        }

        // Cria o objeto Paciente com os dados do formulário e o ID
        Paciente pacienteAtualizado = getPacienteFromForm();
        // Garante que o ID do objeto é o do formulário
        pacienteAtualizado.setID(id);

        try {
            // 2. Chama o DAO, que agora retorna o número de linhas afetadas (0 se o ID não existir)
            int linhasAfetadas = pacienteDAO.atualizar(pacienteAtualizado);

            if (linhasAfetadas > 0) {
                // Sucesso: A linha existia e foi atualizada
                carregarTodosPacientes(); // Recarrega para refletir a mudança
                limparFormulario();
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Paciente atualizado com sucesso!");
            } else {
                // Falha: 0 linhas afetadas, ou seja, o ID não existe no banco
                exibirAlerta(Alert.AlertType.WARNING, "ID Não Encontrado", "O ID " + id + " não foi encontrado no banco de dados. Nenhuma atualização realizada.");
            }
        } catch (SQLException e) {
            // Erro: Problema na execução do SQL (conexão, sintaxe, etc.)
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Atualização", "Falha ao atualizar paciente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    protected void onExcluirPacienteClick() {
        String idText = txtID.getText();
        if (idText.isEmpty()) {
            exibirAlerta(Alert.AlertType.WARNING, "ID Inválido", "Informe o ID para excluir.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
            if (id <= 0) {
                exibirAlerta(Alert.AlertType.WARNING, "ID Inválido", "O ID deve ser um número positivo.");
                return;
            }
        } catch (NumberFormatException e) {
            exibirAlerta(Alert.AlertType.ERROR, "ID Inválido", "O ID deve ser um número inteiro.");
            return;
        }

        try {
            // Chama o DAO e armazena o número de linhas excluídas
            int linhasAfetadas = pacienteDAO.excluir(id);

            if (linhasAfetadas > 0) {
                // Sucesso: Pelo menos uma linha foi excluída
                carregarTodosPacientes(); // Recarrega a tabela
                limparFormulario();
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Paciente removido do banco com sucesso!");
            } else {
                // Falha: 0 linhas afetadas, o ID não existe
                exibirAlerta(Alert.AlertType.WARNING, "ID Não Encontrado", "O ID " + id + " não foi encontrado. Nenhuma remoção realizada.");
            }
        } catch (SQLException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Exclusão", "Falha ao remover paciente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limparFormulario() {
        txtID.setText("");
        txtNome.setText("");
        txtCPF.setText("");
        txtTelefone.setText("");
        txtDataNascimento.setValue(null);
    }

    private void exibirAlerta(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}