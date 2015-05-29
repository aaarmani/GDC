package br.com.actia.dao;

import br.com.actia.model.ListBanner;
import java.util.List;

public interface ListBannerDAO {
/**
     * Faz a inserção ou atualização de um ListBanner na base de dados.
     * 
     * @param listBanner
     * @return  referência atualizada do objeto.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    ListBanner save(ListBanner listBanner);
    /**
     * Exclui o registro de ListBanner na base de dados
     *
     * @param listBanner
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    void remove(ListBanner listBanner);

    /**
     * @return Lista com todos os ListBanners cadastrados no banco de dados.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<ListBanner> getAll();

    /**
     * @param name Filtro da pesquisa de ListBanners.
     * @return Lista de Banners com filtro em nome.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    List<ListBanner> getListBannerByName(String name);
    
    /**
     * @param id filtro da pesquisa.
     * @return ListBanner com filtro no id, caso nao exista
     * retorna <code>null</code>.
     * @throws <code>RuntimeException</code> se algum problema ocorrer.
     */
    ListBanner findById(Integer id);
}
