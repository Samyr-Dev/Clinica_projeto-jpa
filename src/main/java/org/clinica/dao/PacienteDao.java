package org.clinica.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.clinica.model.Paciente;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PacienteDao implements Initializable {


    @FXML
    public TextField txtNome;
    @FXML
    public TextField txtID;
    @FXML
    public TextField txtCPF;
    @FXML
    public DatePicker txtDataNascimento;
    @FXML
    public TextField txtTelefone;
    @FXML
    private TableColumn<Paciente, Integer> colunaID;
    @FXML
    private TableColumn<Paciente, String> colunaNome;
    @FXML
    private TableColumn<Paciente, String> colunaCPF;
    @FXML
    private TableColumn<Paciente, DatePicker> colunaDataNascimento;
    @FXML
    private TableColumn<Paciente, String> colunaTelefone;
    @FXML
    private TableView<Paciente> tabelaDados;
    private int proximoID = 0;

    Paciente paciente;
    List<Paciente> listaPacientes;
    ObservableList<Paciente> observableListPacientes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.paciente = new Paciente();
        this.listaPacientes = new ArrayList<>();
        vinculoComTabela();
    }

    public void vinculoComTabela() {
        colunaID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colunaDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
    }

    @FXML
    protected void lerFormulario(){

        this.paciente.setNome(txtNome.getText() );
        this.paciente.setCpf(txtCPF.getText() );
        this.paciente.setDataNascimento(txtDataNascimento.getValue());
        this.paciente.setTelefone(txtTelefone.getText() );
    }

    public void atualizarTableView() {
        this.listaPacientes.forEach(obj -> System.out.printf(obj.getNome() + ", " + obj.getCpf() + ", " + obj.getDataNascimento() + ", " + obj.getTelefone() +"\n"));
        this.observableListPacientes = FXCollections.observableList(this.listaPacientes);
        this.tabelaDados.setItems(this.observableListPacientes);
    }

    @FXML
    protected void onsalvarPacienteClick() {

        //Salva o paciente cadastrado
        lerFormulario();
        int novoID = ++proximoID;
        this.paciente.setID(novoID);
        this.listaPacientes.add(paciente);

        //Limpa o preenchimento para um novo preenchimento de paciente
        this.paciente = new Paciente();
        txtNome.setText("");
        txtCPF.setText("");
        txtTelefone.setText("");
        txtDataNascimento.setValue(null);
    }

    @FXML
    protected void onlistarPacientesClick(){

        String nomeBuscado = txtNome.getText();

        if (nomeBuscado == null || nomeBuscado.trim().isEmpty()){
            atualizarTableView();  //FUNÇÂO PARA LISTAR TODOS PACIENTES CADASTRADOS
            return;
        }else {List<Paciente> filtrados = new ArrayList<>();

        for (Paciente p : listaPacientes) {
            if (p.getNome().equalsIgnoreCase(nomeBuscado.trim())) {
                filtrados.add(p);
            }
        }

        // Converte p/ ObservableList e exibe na tabela
        ObservableList<Paciente> listaFiltrada =
                FXCollections.observableArrayList(filtrados);

        tabelaDados.setItems(listaFiltrada);
    }

    }

    @FXML
    protected void onAtualizarPacienteClick(){

        // ID digitado no campo
        int id = Integer.parseInt(txtID.getText());

        // Procura o paciente na lista
        Paciente pacienteExistente = null;
        for (Paciente p : listaPacientes) {
            if (p.getID() == id) {
                pacienteExistente = p;
                break;
            }
        }

        // Se não encontrou
        if (pacienteExistente == null) {
            System.out.println("Paciente não encontrado!");
            return;
        }

        // Lê dados do formulário
        lerFormulario();

        // Atualiza os dados do paciente existente
        pacienteExistente.setNome(paciente.getNome());
        pacienteExistente.setCpf(paciente.getCpf());
        pacienteExistente.setTelefone(paciente.getTelefone());
        pacienteExistente.setDataNascimento(paciente.getDataNascimento());

        System.out.println("Paciente atualizado com sucesso!");

        // Limpa os campos
        txtID.setText("");
        txtNome.setText("");
        txtCPF.setText("");
        txtTelefone.setText("");
        txtDataNascimento.setValue(null);

        // Cria novo objeto paciente limpo
        this.paciente = new Paciente();

        tabelaDados.refresh();


    }
    @FXML
    protected void onExcluirPacienteClick() {
        // Verifica se o campo ID está preenchido
        if (txtID.getText().isEmpty()) {
            System.out.println("Informe o ID para excluir.");
            return;
        }

        int id = Integer.parseInt(txtID.getText());

        Paciente pacienteParaExcluir = null;

        // Procura na lista
        for (Paciente p : listaPacientes) {
            if (p.getID() == id) {
                pacienteParaExcluir = p;
                break;
            }
        }

        // Se não encontrou
        if (pacienteParaExcluir == null) {
            System.out.println("Paciente não encontrado!");
            return;
        }

        // Remove da lista
        listaPacientes.remove(pacienteParaExcluir);

        // Atualiza tabela
        tabelaDados.refresh();

        System.out.println("Paciente removido com sucesso!");

        // Limpa os campos
        txtID.setText("");
        txtNome.setText("");
        txtCPF.setText("");
        txtTelefone.setText("");
        txtDataNascimento.setValue(null);

        // Cria novo paciente vazio para evitar reaproveitar dados
        this.paciente = new Paciente();

    }

}
