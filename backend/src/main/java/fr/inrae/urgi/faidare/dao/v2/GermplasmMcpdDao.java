package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.dao.DocumentDao;
import fr.inrae.urgi.faidare.domain.GermplasmMcpdVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.stream.Stream;

public interface GermplasmMcpdDao extends DocumentDao<GermplasmMcpdVO> {

    Page<GermplasmMcpdVO> findByGermplasmDbId(String germplasmDbId, Pageable pageable);

    GermplasmMcpdVO getByGermplasmDbId(String germplasmDbId);

    GermplasmMcpdVO getByPUID(String germplasmPUI);

    Stream<GermplasmMcpdVO> findByGermplasmDbIdIn(Set<String> ids);
}
