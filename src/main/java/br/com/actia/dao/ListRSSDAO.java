package br.com.actia.dao;

import br.com.actia.model.ListRSS;
import java.util.List;

public interface ListRSSDAO {
/**
     * Faz a inserção ou atualização de um ListRSS na base de dados.
     * 
     * @param listRSS
     * @return  referência atualizada do objeto.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    ListRSS save(ListRSS listRSS);
    /**
     * Exclui o registro de ListRSS na base de dados
     *
     * @param listRSS
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    void remove(ListRSS listRSS);

    /**
     * @return Lista com todos os ListRSS cadastrados no banco de dados.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<ListRSS> getAll();

    /**
     * @param name Filtro da pesquisa de ListRSS.
     * @return Lista de RSS com filtro em nome.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<ListRSS> getListRSSByName(String name);
    
    /**
     * @param id filtro da pesquisa.
     * @return ListRSS com filtro no id, caso nao exista
     * retorna <code>null</code>.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    ListRSS findById(Integer id);
}
