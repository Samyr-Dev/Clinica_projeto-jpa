package org.clinica.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class PacienteController {

    //@FXML
    //public TextField txtID;
    @FXML
    public TextField txtNome;
    @FXML
    public TextField txtCPF;
    @FXML
    public TextField txtDataNascimento;
    @FXML
    public TextField txtTelefone;

    @FXML
    protected void onCadastrarPacienteClick(){
        System.out.println(this.txtNome.getText() );
        System.out.println(this.txtCPF.getText() );
        System.out.println(this.txtDataNascimento.getText() );
        System.out.println(this.txtTelefone.getText() );

    }
}
