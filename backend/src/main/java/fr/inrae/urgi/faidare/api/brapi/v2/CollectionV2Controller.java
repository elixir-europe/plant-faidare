package fr.inrae.urgi.faidare.api.brapi.v2;

import fr.inrae.urgi.faidare.dao.v2.CollectionV2Dao;
import fr.inrae.urgi.faidare.domain.CollPopVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Breeding API latest version (V2)", description = "BrAPI full specifications : https://brapi.org/specification")
@RestController
@RequestMapping("/brapi/v2")
public class CollectionV2Controller {

    private final CollectionV2Dao collectionDao;

    public CollectionV2Controller(CollectionV2Dao collectionDao) {
        this.collectionDao = collectionDao;
    }

    @GetMapping("/collection")
    public BrapiListResponse<CollPopVO> getCollections(){
        return collectionDao.getAllCollections();

    }
}
