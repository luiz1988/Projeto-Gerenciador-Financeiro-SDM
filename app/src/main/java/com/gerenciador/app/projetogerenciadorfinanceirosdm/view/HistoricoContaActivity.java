package com.gerenciador.app.projetogerenciadorfinanceirosdm.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gerenciador.app.projetogerenciadorfinanceirosdm.R;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.model.DTO;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.adapter.TransacaoAdapter;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.dao.ContaDAO;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.dao.TransacaoDAO;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.model.Conta;


import java.util.List;

public class HistoricoContaActivity extends AppCompatActivity {

    private ContaDAO contaDAO;
    TransacaoAdapter historicoAdapter;
    private static final int TRANSACAO_REQUEST_CODE = 0;
    public static final String CONTA = "conta_historico";
    static final int ERRO = -2;
    Integer conta;
    TextView descricao;
    static final String ID = "contaId";
    TextView saldo;
    List<DTO> historico;
    ListView historicoListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_conta);
        conta = (Integer) getIntent().getSerializableExtra(CONTA);
        Conta contaHistorico = new ContaDAO(this).recuperarContaPorId(conta);
        contaDAO = new ContaDAO(this);

        historico = new TransacaoDAO(this).buscarHistorico(conta);
        historicoAdapter = new TransacaoAdapter(this, historico);
        historicoListView = findViewById(R.id.historicoListView);
        historicoListView.setAdapter(historicoAdapter);

        descricao = findViewById(R.id.descricaoHistoricoTextView);
        saldo = findViewById(R.id.saldoHistoricoTextView);
        saldo.setText(FormatarNumero(contaDAO.recuperarContasPorId(conta)));
        descricao.setText(contaHistorico.getDescricao());
    }

    public static String FormatarNumero(Double numero) {
        float epsilon = 0.004f;
        if (Math.abs(Math.round(numero) - numero) < epsilon) {
            return String.format("%10.0f", numero);
        } else {
            return String.format("%10.2f", numero);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TRANSACAO_REQUEST_CODE:

                if(resultCode == ERRO){
                    Toast.makeText(this,"Ocorreu algum erro, por favor, tente novamente", Toast.LENGTH_LONG).show();
                }
                else if(resultCode == RESULT_CANCELED){
                    Toast.makeText(this, "Operação cancelada", Toast.LENGTH_LONG).show();
                }
                else if(resultCode == RESULT_OK) {
                    this.historico.removeAll(historico);
                    this.historico.addAll(new TransacaoDAO(this).buscarHistorico(conta));
                    saldo.setText(""+ contaDAO.recuperarContasPorId(conta));
                    this.historicoAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Operação realizada com sucesso", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    public void efetuarTransacao(View view){
        Intent transacaoIntent = new Intent(this, TransacaoActivity.class);
        transacaoIntent.putExtra(ID, conta);
        startActivityForResult(transacaoIntent, TRANSACAO_REQUEST_CODE);
    }
}