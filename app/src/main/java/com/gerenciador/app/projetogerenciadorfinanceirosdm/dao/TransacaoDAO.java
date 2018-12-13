package com.gerenciador.app.projetogerenciadorfinanceirosdm.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.gerenciador.app.projetogerenciadorfinanceirosdm.model.DTO;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.model.Transacao;

import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {
    private SQLiteDatabase database;
    static final String ENTRADA = "Entrada";
    private Helper dbHelper;
    static final int DEBITO = 1;
    static final int CREDITO = 0;
    static final String SAIDA = "Saída";

    public List<DTO> buscarHistorico(Integer contaId){
        String[] colunas = new String[]{"t.descricao", "t.valor", "t.natureza_operacao", "c.descricao"};
        database = dbHelper.getReadableDatabase();
        List<DTO> db = new ArrayList<>();
        Cursor dataBase = database.rawQuery(DB.RECUPERAR_HISTORICO, new String[] {String.valueOf(contaId)});
        while (dataBase.moveToNext()) {
            DTO transacao = new DTO();
            transacao.setDescricao(dataBase.getString(0));
            transacao.setValor(dataBase.getString(1));

            String natureza_operacao = (String.valueOf(dataBase.getInt(2))
                    .equalsIgnoreCase(String.valueOf(DEBITO)) ? SAIDA : ENTRADA);
            transacao.setTipoOperacao(natureza_operacao);
            transacao.setCentroCusto(dataBase.getString(3));
            db.add(transacao);
        }
        dataBase.close();
        database.close();
        return db;
    }

    public Boolean salvarTransacao(Transacao transacao) {
        try{
            database = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("descricao", transacao.getDescricao());
            values.put("valor", transacao.getValor());
            values.put("conta", transacao.getConta().getId());
            values.put("centro_custo", transacao.getCentroCusto().getId());
            values.put("debito", transacao.getTipoOperacao());

            database.insert(DB.TABELA_TRANSACAO, null, values);
            Double saldo = buscarContaPorId(transacao.getConta().getId());

            if(transacao.getTipoOperacao() == DEBITO){
                saldo = saldo - transacao.getValor();
            }else{
                saldo = saldo + transacao.getValor();
            }
            ContentValues valor = new ContentValues();
            valor.put("saldo", saldo);
            database.update(DB.TABELA_CONTA, valor, "id="+transacao.getConta().getId(), null);
            database.close();
        }catch(Exception e){
            Log.i("ERROR", e.getMessage());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Double buscarCredito(){
        database = dbHelper.getReadableDatabase();
        Cursor dataBase = database.rawQuery(DB.RECUPERAR_TRANSACAO_CREDITO, new String[] {String.valueOf(CREDITO)});
        dataBase.moveToFirst();
        return dataBase.getDouble(0);
    }

    public TransacaoDAO(Context context) {
        this.dbHelper = Helper.getInstance(context);
    }

    public List<DTO> buscarDebito(){
        List<DTO> transacoesDebito = new ArrayList<DTO>();
        database = dbHelper.getReadableDatabase();

        Cursor dataBase = database.rawQuery(DB.RECUPERAR_TRANSACAO_DEBITO, new String[] {String.valueOf(DEBITO)});

        while (dataBase.moveToNext()) {
            DTO transacao = new DTO();
            transacao.setValor(String.valueOf(dataBase.getDouble(0)));
            transacao.setCentroCusto(dataBase.getString(1));
            transacoesDebito.add(transacao);
        }
        return transacoesDebito;
    }

    public Double buscarContaPorId(Integer id){
        try{
            database = dbHelper.getReadableDatabase();
            Cursor dataBase = database.rawQuery(DB.RECUPERAR_SALDO_CONTA_POR_ID, new String[] {String.valueOf(id)});
            dataBase.moveToFirst();
            return dataBase.getDouble(0);
        }catch(Exception e){
            Log.i("ERROR", e.getMessage());
        }
        return null;
    }

    public Double buscarTransacaosDebito(){
        database = dbHelper.getReadableDatabase();
        Cursor database = this.database.rawQuery(DB.BUSCAR_VALOR_TRANSACAO_DEBITO, new String[] {String.valueOf(DEBITO)});
        database.moveToFirst();
        return database.getDouble(0);
    }


    public List<DTO> filtrarTransacoes(DTO filtro) {
        database = dbHelper.getReadableDatabase();

        List<DTO> transacaoFiltrada = new ArrayList<>();

        String[] filtros = montarClausulasConsulta(filtro);

        String query = DB.RECUPERAR_TODAS_TRANSACOES;

        if(!(filtros.length == 0)){
            query = montarQueryFiltrada(filtro);
        }

        Cursor cursor = database.rawQuery(query, filtros);

        while (cursor.moveToNext()) {
            DTO transacao = new DTO();
            transacao.setDescricao(cursor.getString(0));
            transacao.setValor(cursor.getString(1));
            String natureza_operacao = (String.valueOf(cursor.getInt(2))
                    .equalsIgnoreCase(String.valueOf(DEBITO)) ? SAIDA : ENTRADA);
            transacao.setTipoOperacao(natureza_operacao);
            transacao.setCentroCusto(cursor.getString(3));
            transacaoFiltrada.add(transacao);
        }
        cursor.close();
        database.close();
        return transacaoFiltrada;
    }

    private String[] montarClausulasConsulta(DTO filtro) {

        List<String> restricoes = new ArrayList<String>();

        if(filtro.getDescricao() != null && !filtro.getDescricao().equals("")){
            restricoes.add(filtro.getDescricao());
        }
        if(filtro.getValorInicial() != null){
            restricoes.add(String.valueOf(filtro.getValorInicial()));
        }
        if(filtro.getValorFinal() != null){
            restricoes.add(String.valueOf(filtro.getValorFinal()));
        }
        if(filtro.getTipoOperacao() != null){
            restricoes.add(String.valueOf(filtro.getTipoOperacao()));
        }
        if(filtro.getCentroCusto() != null){
            restricoes.add(filtro.getCentroCusto());
        }

        return (String[]) restricoes.toArray(new String[0]);
    }

    private String montarQueryFiltrada(DTO filtro) {
        String query = DB.RECUPERAR_TODAS_TRANSACOES.concat(" WHERE 1 == 1");

        if(filtro.getDescricao() != null && !filtro.getDescricao().equals("")){
            query += " AND t.descricao LIKE ?";
        }
        if(filtro.getValorInicial() != null){
            query += " AND t.valor >= ?";
        }
        if(filtro.getValorFinal() != null){
            query += " AND t.valor <= ?";
        }

        if(filtro.getTipoOperacao() != null){
            query += " AND t.debito = ?";
        }
        if(filtro.getCentroCusto() != null){
            query += " AND c.descricao LIKE ?";
        }

        return query;
    }
}
