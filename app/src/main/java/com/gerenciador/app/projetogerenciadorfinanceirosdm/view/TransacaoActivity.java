package com.gerenciador.app.projetogerenciadorfinanceirosdm.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gerenciador.app.projetogerenciadorfinanceirosdm.R;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.dao.CentroCustoDAO;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.dao.TransacaoDAO;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.model.CentroCusto;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.model.Conta;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.model.Transacao;

import java.util.ArrayList;
import java.util.List;

public class TransacaoActivity extends AppCompatActivity {

    private Integer conta;
    private TextView descricao;
    private List<CentroCusto> listaCentroCusto;
    private RadioButton credito;
    private EditText saldoView;
    private RadioButton Credito;
    Spinner spinner;
    static final String CONTA = "contaId";
    static final int DEBITO = 1;
    static final int CREDITO = 0;
    static final int ERRO = -2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacao);

        List<String> listaDescricoes = new ArrayList<>();
        listaCentroCusto = new CentroCustoDAO(this).recuperCentroDeCustos();
        for(CentroCusto custo : listaCentroCusto){
            listaDescricoes.add(custo.getDescricao());
        }
        ArrayAdapter<String> centroCustoArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, listaDescricoes);
        spinner = findViewById(R.id.centroDeCustoSpinner);
        spinner.setAdapter(centroCustoArrayAdapter);
    }

    private Boolean inserir() {
        Transacao transacao = new Transacao();
        transacao.setDescricao(descricao.getText().toString());
        transacao.setValor(Double.valueOf(saldoView.getText().toString()));
        transacao.setConta(new Conta((Integer) getIntent().getSerializableExtra(CONTA)));

        Integer indexItemClicado = spinner.getSelectedItemPosition();
        transacao.setCentroCusto(listaCentroCusto.get(indexItemClicado));

        if(Credito.isChecked()){
            transacao.setTipoOperacao(DEBITO);
        }else{
            transacao.setTipoOperacao(CREDITO);
        }

        TransacaoDAO transacaoDAO = new TransacaoDAO(this);
        return transacaoDAO.salvarTransacao(transacao);
    }
    private Boolean validarCamposObrigatorios(){

        if((!Credito.isChecked() && !credito.isChecked() || saldoView.getText().toString() == "" || descricao.getText().toString() == "" )){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    public void salvar(View view){

        credito = findViewById(R.id.creditoRadio);
        descricao = findViewById(R.id.tipoTransacaoTextView);
        Credito = findViewById(R.id.debitoRadio);
        saldoView = findViewById(R.id.valorTransacaoTextView);
        if(validarCamposObrigatorios()){
              if(inserir()){
                  setResult(RESULT_OK);
              }else{
                  setResult(ERRO);
              }
              finish();
          }else {
            Toast.makeText(this, R.string.campos_obrigatorios, Toast.LENGTH_LONG).show();
        }
    }
}
