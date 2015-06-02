package br.com.actia.dao;

import br.com.actia.model.RSS;
import java.util.List;

public interface RSSDAO {
    /**
     * Faz a inserção ou atualização de um RSS na base de dados.
     * 
     * @param RSS
     * @return  referência atualizada do objeto.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    RSS save(RSS RSS);
    /**
     * Exclui o registro de RSS na base de dados
     *
     * @param RSS
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    void remove(RSS RSS);

    /**
     * @return Lista com todos os RSS cadastrados no banco de dados.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<RSS> getAll();

    /**
     * @param name Filtro da pesquisa de RSS.
     * @return Lista de RSS com filtro em nome.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<RSS> getRSSByName(String name);
    
    
    /**
     * @param id filtro da pesquisa.
     * @return Video com filtro no id, caso nao exista
     * retorna <code>null</code>.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    RSS findById(Integer id);
}
