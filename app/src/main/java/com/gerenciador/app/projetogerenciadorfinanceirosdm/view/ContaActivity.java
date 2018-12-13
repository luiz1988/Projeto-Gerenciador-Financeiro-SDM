package com.gerenciador.app.projetogerenciadorfinanceirosdm.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gerenciador.app.projetogerenciadorfinanceirosdm.R;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.adapter.ContaAdapter;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.dao.ContaDAO;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.dao.TransacaoDAO;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.model.Conta;

import java.util.List;

public class ContaActivity extends AppCompatActivity implements  AdapterView.OnItemClickListener{

    TransacaoDAO transacaoDAO;
    private TextView saidaTextView;
    private TextView contaSaldoViewText;
    ContaDAO contaDAO;
    ContaAdapter listaContaAdapter;
    private ListView listaViewText;
    private final int CONTA_NOVA_REQUEST_CODE = 0;
    private List<Conta> lista;
    private TextView entradaTextView;
    public static final String CONTA_DETALHE = "conta_historico";
    public static final int ERRO = -2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);
        contaDAO = new ContaDAO(this);
        transacaoDAO = new TransacaoDAO(this);
        lista = contaDAO.recuperar();

        listaViewText = findViewById(R.id.listaTextView);
        listaViewText.setOnItemClickListener(this);

        contaSaldoViewText = findViewById(R.id.SaldoTextView);
        contaSaldoViewText.setText((FormatarNumero(contaDAO.recuperarContas())));
        listaContaAdapter = new ContaAdapter(this, lista);

        entradaTextView = findViewById(R.id.entradasTextView);
        saidaTextView = findViewById(R.id.saidaTextView);
        entradaTextView.setText((String.valueOf(transacaoDAO.buscarCredito())));
        saidaTextView.setText((String.valueOf(transacaoDAO.buscarTransacaosDebito())));
        listaViewText.setAdapter(listaContaAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CONTA_NOVA_REQUEST_CODE:
                if(resultCode == RESULT_CANCELED){
                    Toast.makeText(this, "Operação cancelada", Toast.LENGTH_LONG).show();
                }
                if(resultCode == ERRO){
                    Toast.makeText(this, "Ocorreu algum erro,por favor faça novamente o cadastro", Toast.LENGTH_LONG).show();
                }
                if(resultCode == RESULT_OK) {
                    this.lista.removeAll(lista);
                    this.lista.addAll(contaDAO.recuperar());
                    contaSaldoViewText.setText((String.valueOf(contaDAO.recuperarContas())));
                    this.listaContaAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Conta cadastrada com sucesso", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    @Override
    protected void onRestart() { super.onRestart();
        contaSaldoViewText.setText((FormatarNumero(contaDAO.recuperarContas())));
        lista = contaDAO.recuperar();
        listaContaAdapter = new ContaAdapter(this, lista);
        listaViewText.setAdapter(listaContaAdapter);
        saidaTextView.setText(String.valueOf(transacaoDAO.buscarTransacaosDebito()));
        entradaTextView.setText(String.valueOf(transacaoDAO.buscarCredito()));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if(item.getItemId() == R.id.menuConta){
            intent = new Intent(getApplicationContext(), ContaNovaActivity.class);
            startActivityForResult(intent, CONTA_NOVA_REQUEST_CODE);
        }

        if(item.getItemId() == R.id.filtrarTransacaoMenuItem){
            intent = new Intent(getApplicationContext(), FormFiltrarTransacaoActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Conta conta = lista.get(position);
        Intent contaIntent = new Intent(this, HistoricoContaActivity.class);
        contaIntent.putExtra(CONTA_DETALHE, conta.getId());
        startActivity(contaIntent);
    }
    public static String FormatarNumero(Double numero) {
        float epsilon = 0.004f;
        if (Math.abs(Math.round(numero) - numero) < epsilon) {
            return String.format("%10.0f", numero);
        } else {
            return String.format("%10.2f", numero);
        }
    }
}