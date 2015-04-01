package br.com.actia.dao;

import br.com.actia.model.Banner;
import java.util.List;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public interface BannerDAO {
    /**
     * Faz a inserção ou atualização de um BusStop na base de dados.
     * 
     * @param Banner
     * @return  referência atualizada do objeto.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    Banner save(Banner banner);
    /**
     * Exclui o registro de Banner na base de dados
     *
     * @param banner
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    void remove(Banner banner);

    /**
     * @return Lista com todos os Banners cadastrados no banco de dados.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<Banner> getAll();

    /**
     * @param name Filtro da pesquisa de Banners.
     * @return Lista de Banners com filtro em nome.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<Banner> getBannerByName(String name);
    
    
    /**
     * @param id filtro da pesquisa.
     * @return Banner com filtro no id, caso nao exista
     * retorna <code>null</code>.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    Banner findById(Integer id);    
}
