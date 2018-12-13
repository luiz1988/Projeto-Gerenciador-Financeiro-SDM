
package com.gerenciador.app.projetogerenciadorfinanceirosdm.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.gerenciador.app.projetogerenciadorfinanceirosdm.model.CentroCusto;

import java.util.ArrayList;
import java.util.List;

public class CentroCustoDAO {
    private Helper helper;
    private SQLiteDatabase bancoDados;

    private void preenchertabela(){
        try {
            bancoDados = helper.getWritableDatabase();
            List<String> descricao = new ArrayList<>();
            descricao.add("Educação");
            descricao.add("Corporativo");
            descricao.add("Alimentação");
            descricao.add("Transporte");
            descricao.add("Saúde");
            descricao.add("Lazer");
            descricao.add("Moradia");
            descricao.add("Tarifas bancárias");
            descricao.add("Combustível");

            List<ContentValues> conteudo = new ArrayList<ContentValues>();

            for(int i = 0; i < descricao.size(); i++){
                ContentValues values = new ContentValues();
                values.put("descricao", descricao.get(i));
                conteudo.add(values);
            }
            for(ContentValues values : conteudo){
                bancoDados.insert(DB.TABELA_CENTRO_CUSTO, null, values);
            }
        }catch(Exception e){
            Log.i("ERROR", e.getMessage());
        }
    }

    public List<CentroCusto> recuperCentroDeCustos() {
        List<CentroCusto> listaCentroCustoDB = new ArrayList<>();

        try {
            String[] colunas = new String[]{"id", "descricao"};
            bancoDados = helper.getReadableDatabase();
            Cursor dataBase = bancoDados.query(DB.TABELA_CENTRO_CUSTO,
                    colunas,
                    null, null, null, null, "descricao");

            while (dataBase.moveToNext()) {
                CentroCusto objeto = new CentroCusto();
                objeto.setId(dataBase.getInt(0));
                objeto.setDescricao(dataBase.getString(1));
                listaCentroCustoDB.add(objeto);
            }
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
        }

        return listaCentroCustoDB;
    }

    public CentroCustoDAO(Context context) {
        this.helper = Helper.getInstance(context);
        List<CentroCusto> lista = recuperCentroDeCustos();
        if(lista.isEmpty()){
            preenchertabela();
        }
    }
}
