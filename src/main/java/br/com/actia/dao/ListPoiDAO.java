package br.com.actia.dao;

import br.com.actia.model.ListPoi;
import java.util.List;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public interface ListPoiDAO {
    /**
     * Faz a inserção ou atualização de um ListPoi na base de dados.
     * 
     * @param listPoi
     * @return  referência atualizada do objeto.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    ListPoi save(ListPoi listPoi);
    /**
     * Exclui o registro de ListPoi na base de dados
     *
     * @param listPoi
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    void remove(ListPoi listPoi);

    /**
     * @return Lista com todos os ListPois cadastrados no banco de dados.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<ListPoi> getAll();

    /**
     * @param name Filtro da pesquisa de POIs.
     * @return Lista de ListPois com filtro em nome.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<ListPoi> getListPoiByName(String name);

    /**
     * @param id filtro da pesquisa.
     * @return ListPoi com filtro no id, caso nao exista
     * retorna <code>null</code>.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    ListPoi findById(Integer id);
}
