package com.gerenciador.app.projetogerenciadorfinanceirosdm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.gerenciador.app.projetogerenciadorfinanceirosdm.R;
import com.gerenciador.app.projetogerenciadorfinanceirosdm.model.DTO;


import java.util.List;

public class TransacaoAdapter extends ArrayAdapter<DTO> {

    private LayoutInflater layoutInflaterTransacaoAdapter;
    private TextView tipoTransacaoTextView;
    private TextView valorTransacaoTextView;
    private TextView centroCustoTextView;
    private TextView transacaoTextView;

    public TransacaoAdapter(@NonNull Context context, List<DTO> list) {
        super(context, R.layout.layout_transacao_adapter, list);

        layoutInflaterTransacaoAdapter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = layoutInflaterTransacaoAdapter.inflate(R.layout.layout_transacao_adapter, null);
        }
        this.valorTransacaoTextView =  convertView.findViewById(R.id.valorTextView);
        this.centroCustoTextView =  convertView.findViewById(R.id.nomeTransacaoTextView);
        this.tipoTransacaoTextView =  convertView.findViewById(R.id.tipoTransacaoTextView);
        this.transacaoTextView =  convertView.findViewById(R.id.descricaoTextView);

        DTO transacao = getItem(position);
        transacaoTextView.setText(transacao.getDescricao());
        valorTransacaoTextView.setText(transacao.getValor());
        tipoTransacaoTextView.setText(transacao.getTipoOperacao());
        centroCustoTextView.setText("- ".concat(transacao.getCentroCusto()));
        return convertView;
    }
}

