
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
import com.gerenciador.app.projetogerenciadorfinanceirosdm.model.Conta;

import java.util.List;

public class ContaAdapter extends ArrayAdapter<Conta> {

    private LayoutInflater layoutInflaterConta;
    public ContaAdapter(@NonNull Context context, List<Conta> list) {
        super(context, R.layout.layout_conta_adapter, list);

        layoutInflaterConta = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = layoutInflaterConta.inflate(R.layout.layout_conta_adapter, null);
        }
        Conta conta = getItem(position);
        TextView descricaoTextView =  convertView.findViewById(R.id.descricaoTextView);
        TextView saldoTextView =  convertView.findViewById(R.id.saldoContatoTextView);
        descricaoTextView.setText(conta.getDescricao());
        saldoTextView.setText(String.valueOf(conta.getSaldoConta()));
        return convertView;
    }
}