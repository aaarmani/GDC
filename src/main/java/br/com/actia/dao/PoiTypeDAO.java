package br.com.actia.dao;

import br.com.actia.model.PoiType;
import java.util.List;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public interface PoiTypeDAO {
        /**
     * Faz a inserção ou atualização de um PoiType na base de dados.
     * 
     * @param poiType
     * @return  referência atualizada do objeto.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    PoiType save(PoiType poiType);
    /**
     * Exclui o registro de PoiType na base de dados
     *
     * @param poiType
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    void remove(PoiType poiType);

    /**
     * @return Lista com todos os PoiTypes cadastrados no banco de dados.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<PoiType> getAll();

    /**
     * @param name Filtro da pesquisa de PoiTypes.
     * @return Lista de PoiTypes com filtro em nome.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<PoiType> getPoiTypeByName(String name);
    
    /**
     * @param id filtro da pesquisa.
     * @return PoiType com filtro no id, caso nao exista
     * retorna <code>null</code>.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    PoiType findById(Integer id);


}
