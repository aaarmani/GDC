package br.com.actia.dao;

import br.com.actia.model.BusStop;
import java.util.List;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public interface BusStopDAO {
    /**
     * Faz a inserção ou atualização de um BusStop na base de dados.
     * 
     * @param BusStop
     * @return  referência atualizada do objeto.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    BusStop save(BusStop busStop);
    /**
     * Exclui o registro de BusStop na base de dados
     *
     * @param busStop
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    void remove(BusStop busStop);

    /**
     * @return Lista com todos os BusStops cadastrados no banco de dados.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<BusStop> getAll();

    /**
     * @param name Filtro da pesquisa de BusStops.
     * @return Lista de BusStops com filtro em nome.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<BusStop> getBusStopByName(String name);
    
    
    /**
     * @param id filtro da pesquisa.
     * @return BusStop com filtro no id, caso nao exista
     * retorna <code>null</code>.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    BusStop findById(Integer id);
    
}
