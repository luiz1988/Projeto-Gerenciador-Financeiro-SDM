package com.gerenciador.app.projetogerenciadorfinanceirosdm.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gerenciador.app.projetogerenciadorfinanceirosdm.R;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.dao.ContaDAO;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.model.Conta;

public class ContaNovaActivity extends AppCompatActivity {
    private EditText descricao;
    private EditText valor;
    public static final int ERRO = -2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta_nova);
        valor = findViewById(R.id.saldoTextview);
        descricao = findViewById(R.id.descricaoTextView);
    }

    private Boolean conta(){
        Conta conta = new Conta();
        conta.setSaldoConta(Double.valueOf(this.valor.getText().toString()));
        conta.setDescricao(String.valueOf(this.descricao.getText()));
        ContaDAO contaDAO = new ContaDAO(this);
        return contaDAO.salvar(conta);
    }

    public void cancelar(View view ){
        if (R.id.cancelarButton == view.getId()) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
    public void salvar(View view) {
        if (R.id.salvarButton == view.getId()) {
            if (this.valor.getText().toString() == "" || this.descricao.getText().toString() == "") {
                Toast.makeText(this, "Preencha os campos obrigatórios!!", Toast.LENGTH_LONG).show();
                finish();
            }
            if(conta()){
                setResult(RESULT_OK);
            }else{
                setResult(ERRO);
            }
            finish();
        }
    }


}
