package com.gerenciador.app.projetogerenciadorfinanceirosdm.dao;

public final class DB {

    private DB(){

    }
    public static final String TABELA_CENTRO_CUSTO = "centro_custo";
    public static final String TABELA_TRANSACAO = "transacao";
    public static final String TABELA_CONTA = "conta";

    public static final String RECUPERAR_CONTA = "SELECT descricao, saldo FROM conta WHERE id = ? ";
    public static final String RECUPERAR_TODAS_TRANSACOES = "SELECT t.descricao, t.valor, t.debito, c.descricao FROM transacao t INNER JOIN centro_custo c ON c.id = t.centro_custo";
    public static final String RECUPERAR_HISTORICO = "SELECT t.descricao, t.valor, t.debito, c.descricao FROM transacao t " +
            "INNER JOIN centro_custo c ON c.id = t.centro_custo WHERE t.conta = ?";
    public static final String RECUPERAR_SALDO_CONTAS = "SELECT SUM(saldo) as total FROM conta";
    public static final String BUSCAR_VALOR_TRANSACAO_DEBITO = "SELECT SUM(valor) as total FROM transacao WHERE debito = ?";
    public static final String RECUPERAR_SALDO_CONTA_POR_ID = "SELECT SUM(saldo) as total FROM conta WHERE id = ?";
    public static final String RECUPERAR_TRANSACAO_CREDITO = "SELECT SUM(valor) as total FROM transacao WHERE debito = ?";
    public static final String RECUPERAR_TRANSACAO_DEBITO = "SELECT SUM(t.valor) as total, c.descricao, c.id " +
            "FROM transacao t INNER JOIN centro_custo c ON t.centro_custo = c.id WHERE " +
            "t.debito = ? GROUP BY c.id";


    public static final String TB_CENTRO_CUSTO = "CREATE TABLE IF NOT EXISTS centro_custo( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "descricao TEXT NOT NULL); ";

    public static final String TB_CONTA = "CREATE TABLE IF NOT EXISTS conta( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "descricao TEXT NOT NULL, " +
            "saldo DECIMAL (10,2));";

    public static final String TB_TRANSACAO = "CREATE TABLE IF NOT EXISTS transacao( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "descricao TEXT NOT NULL, " +
            "valor DECIMAL (10,2), " +
            "conta INTEGER, " +
            "centro_custo INTEGER, " +
            "debito INTEGER, " +
            "dias_periodicidade INTEGER, " +
            "FOREIGN KEY(conta) REFERENCES conta(id), " +
            "FOREIGN KEY(centro_custo) REFERENCES centro_cuto(id) " +
            ");";
}
