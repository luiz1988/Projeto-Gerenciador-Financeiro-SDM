package com.gerenciador.app.projetogerenciadorfinanceirosdm.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.gerenciador.app.projetogerenciadorfinanceirosdm.R;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.adapter.TransacaoAdapter;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.dao.CentroCustoDAO;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.dao.TransacaoDAO;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.model.CentroCusto;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.model.DTO;

import java.util.ArrayList;
import java.util.List;

public class FormFiltrarTransacaoActivity extends AppCompatActivity {

    private List<CentroCusto> listaCentroCusto;

    private ListView resultadoBuscaListView;
    private TransacaoAdapter pesquisaResultadoAdapter;

    private EditText descricaoFiltro;
    private EditText valorInicialFiltro;
    private EditText valorFinalFiltro;
    private RadioButton entradaFiltro;
    private RadioButton saidaFiltro;
    CheckBox checkBoxMostrarTipoCusto;
    private Spinner tipoTransacaoFiltro;

    final int DEBITO = 1;
    final int CREDITO = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_transacao);

        obterElementosTela();
        popularSpinner();
        mostrarCentroCusto();
    }

    public void pesquisarTransacoes(View view) {
        DTO filtro = montarObjetoFiltro();

        TransacaoDAO transacaoDAO = new TransacaoDAO(this);
        List<DTO> resultadoBusca = transacaoDAO.filtrarTransacoes(filtro);

        pesquisaResultadoAdapter = new TransacaoAdapter(this, resultadoBusca);
        resultadoBuscaListView = findViewById(R.id.resultadoBuscaListView);
        resultadoBuscaListView.setAdapter(pesquisaResultadoAdapter);
    }

    public void limparFormulario(View view) {
        this.descricaoFiltro.setText("");
        this.valorInicialFiltro.setText("");
        this.valorFinalFiltro.setText("");
        this.entradaFiltro.setChecked(Boolean.FALSE);
        this.saidaFiltro.setChecked(Boolean.FALSE);
        this.checkBoxMostrarTipoCusto.setChecked(Boolean.FALSE);
        this.tipoTransacaoFiltro.setSelection(0);
    }

    public void mostrarCentroCusto() {

        checkBoxMostrarTipoCusto = (CheckBox) findViewById(R.id.inserirCentroCustoCheckBox);

        checkBoxMostrarTipoCusto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                TextView label = (TextView) findViewById(R.id.tipoTransacaoTextView);
                Spinner tipoTransacaoFiltro = findViewById(R.id.tipoTransacaoFiltroSpinner);

                if (isChecked) {
                    tipoTransacaoFiltro.setVisibility(View.VISIBLE);
                    label.setVisibility(View.VISIBLE);
                } else {
                    tipoTransacaoFiltro.setVisibility(View.GONE);
                    label.setVisibility(View.GONE);
                }
            }
        });
    }

    private void obterElementosTela() {
        this.descricaoFiltro = findViewById(R.id.descricaoFiltroEditText);
        this.valorInicialFiltro = findViewById(R.id.valorInicialFiltroEditText);
        this.valorFinalFiltro = findViewById(R.id.valorFinalFiltroEditText);
        this.entradaFiltro = findViewById(R.id.entradaFiltroRadioButton);
        this.saidaFiltro = findViewById(R.id.saidaFiltroRadioButton);
        this.checkBoxMostrarTipoCusto = (CheckBox) findViewById(R.id.inserirCentroCustoCheckBox);
        this.tipoTransacaoFiltro = findViewById(R.id.tipoTransacaoFiltroSpinner);
    }

    private DTO montarObjetoFiltro() {
        DTO transacaoFiltro = new DTO();
        transacaoFiltro.setDescricao(this.descricaoFiltro.getText().toString());

        if (!this.valorInicialFiltro.getText().toString().equals("")) {
            transacaoFiltro.setValorInicial(Double.valueOf(this.valorInicialFiltro.getText().toString()));
        }

        if (!this.valorFinalFiltro.getText().toString().equals("")) {
            transacaoFiltro.setValorFinal(Double.valueOf(this.valorFinalFiltro.getText().toString()));
        }

        if (entradaFiltro.isChecked()) {
            transacaoFiltro.setTipoOperacao(String.valueOf(CREDITO));
        }

        if (saidaFiltro.isChecked()) {
            transacaoFiltro.setTipoOperacao(String.valueOf(DEBITO));
        }

        if (this.checkBoxMostrarTipoCusto.isChecked()) {
            transacaoFiltro.setCentroCusto(this.tipoTransacaoFiltro.getSelectedItem().toString());
        }


        return transacaoFiltro;
    }

    private void popularSpinner() {
        List<String> listaDescricoes = new ArrayList<>();
        listaCentroCusto = new CentroCustoDAO(this).recuperCentroDeCustos();
        for (CentroCusto custo : listaCentroCusto) {
            listaDescricoes.add(custo.getDescricao());
        }
        ArrayAdapter<String> centroCustoArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, listaDescricoes);
        this.tipoTransacaoFiltro.setAdapter(centroCustoArrayAdapter);
    }

}