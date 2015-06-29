package MediaExporterTest;

import JsonGeneratorTest.RouteJsonGeneratorController;
import br.com.actia.dao.RouteDAO;
import br.com.actia.dao.RouteDAOJPA;
import br.com.actia.model.Route;
import service.MediaExporter;

public class MediaExporterTest {
    public static void main(String[] args) {
        RouteJsonGeneratorController routeJsonGeneratorController = new RouteJsonGeneratorController();
        
        RouteDAO routeDao = new RouteDAOJPA(routeJsonGeneratorController.getPersistenceContext());
        Route route = null;
        
        route = routeDao.findById(133);
        
        MediaExporter mediaExporter = new MediaExporter(route);
        mediaExporter.setDestinationFolder("C:\\Users\\Actia\\Desktop\\generation");
        mediaExporter.export();
    }
}
