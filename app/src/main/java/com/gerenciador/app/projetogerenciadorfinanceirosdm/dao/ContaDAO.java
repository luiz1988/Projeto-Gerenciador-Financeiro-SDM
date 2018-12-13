package com.gerenciador.app.projetogerenciadorfinanceirosdm.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gerenciador.app.projetogerenciadorfinanceirosdm.model.Conta;

import java.util.ArrayList;
import java.util.List;

public class ContaDAO {
    private Helper helper;
    private SQLiteDatabase bancoDados;
    public ContaDAO(Context context) {
        this.helper = Helper.getInstance(context);
    }

    public List<Conta> recuperar() {
        try{
            List<Conta> contas = new ArrayList<>();
            String[] colunas = new String[]{ "id", "descricao", "saldo"};
            bancoDados = helper.getReadableDatabase();
            Cursor cursor = bancoDados.query(DB.TABELA_CONTA,
                    colunas,
                    null, null, null, null, "saldo");

            while (cursor.moveToNext()) {
                Conta conta = new Conta();
                conta.setId(cursor.getInt(0));
                conta.setDescricao(cursor.getString(1));
                conta.setSaldoConta(Double.valueOf(cursor.getString(2)));
                contas.add(conta);
            }
            cursor.close();
            bancoDados.close();
            return contas;
        }catch(Exception e){
            Log.i("ERROR", e.getMessage());
        }
        return null;
    }

    public Conta recuperarContaPorId(Integer id){
        try{
            bancoDados = helper.getReadableDatabase();
            Cursor cursor = bancoDados.rawQuery(DB.RECUPERAR_CONTA, new String[] {String.valueOf(id)});
            cursor.moveToFirst();

            Conta conta = new Conta();
            conta.setId(id);
            conta.setDescricao(cursor.getString(0));
            conta.setSaldoConta(cursor.getDouble(1));

            cursor.close();
            bancoDados.close();
            return conta;
        }catch (Exception e){
            Log.i("ERROR", e.getMessage());
        }
        return null;
    }

    public Boolean salvar(Conta conta) {
        try{
            ContentValues conteudo = new ContentValues();
            conteudo.put("saldo", conta.getSaldoConta());
            conteudo.put("descricao", conta.getDescricao());
            bancoDados = helper.getWritableDatabase();
            bancoDados.insert(DB.TABELA_CONTA, null, conteudo);
            bancoDados.close();
        }catch(Exception e){
            Log.i("ERROR", e.getMessage());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Double recuperarContasPorId(Integer id){
        try{
            bancoDados = helper.getReadableDatabase();
            Cursor cursor = bancoDados.rawQuery(DB.RECUPERAR_SALDO_CONTA_POR_ID, new String[] {String.valueOf(id)});
            cursor.moveToFirst();
            return cursor.getDouble(0);
        }catch(Exception e){
            Log.i("ERROR", e.getMessage());
        }
        return null;
    }

    public Double recuperarContas(){
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(DB.RECUPERAR_SALDO_CONTAS);
            bancoDados = helper.getReadableDatabase();
            Cursor cursor = bancoDados.rawQuery(sql.toString(), null);
            cursor.moveToFirst();
            return cursor.getDouble(0);
        }catch(Exception e){
            Log.i("ERROR", e.getMessage());
        }
        return null;
    }
}