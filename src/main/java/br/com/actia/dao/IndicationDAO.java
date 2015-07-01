package br.com.actia.dao;

import br.com.actia.model.Indication;
import java.util.List;

public interface IndicationDAO {
    /**
     * Faz a inserção ou atualização de uma BusStopIndication na base de dados.
     * 
     * @param Indication
     * @return  referência atualizada do objeto.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    Indication save(Indication indication);
    /**
     * Exclui o registro de BusStopIndication na base de dados
     *
     * @param indication
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    void remove(Indication indication);

    /**
     * @return Lista com todos os BusStopIndications cadastrados no banco de dados.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<Indication> getAll();

    /**
     * @param name Filtro da pesquisa de BusStopIndications.
     * @return Lista de BusStopIndications com filtro em nome.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<Indication> getIndicationByName(String name);
    
    
    /**
     * @param id filtro da pesquisa.
     * @return BusStopIndication com filtro no id, caso nao exista
     * retorna <code>null</code>.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    Indication findById(Integer id);    
}
