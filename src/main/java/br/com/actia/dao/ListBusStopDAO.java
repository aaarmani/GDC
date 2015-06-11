package br.com.actia.dao;

import br.com.actia.model.ListBusStop;
import java.util.List;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public interface ListBusStopDAO {
    /**
     * Faz a inserção ou atualização de um ListBusStop na base de dados.
     * 
     * @param ListBusStop
     * @return  referência atualizada do objeto.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    ListBusStop save(ListBusStop listBusStop);
    /**
     * Exclui o registro de ListBusStop na base de dados
     *
     * @param listBusStop
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    void remove(ListBusStop listBusStop);

    /**
     * @return Lista com todos os ListBusStops cadastrados no banco de dados.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<ListBusStop> getAll();

    /**
     * @param name Filtro da pesquisa de ListBusStops.
     * @return Lista de ListBusStops com filtro em nome.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<ListBusStop> getListBusStopByName(String name);

    /**
     * @param id filtro da pesquisa.
     * @return ListBusStop com filtro no id, caso nao exista
     * retorna <code>null</code>.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    ListBusStop findById(Integer id);
}
