package fr.inrae.urgi.faidare.dao.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmPedigreeV1VO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataElasticsearchTest
public class GermplasmPedigreeV1DaoTest {

    @Autowired
    protected GermplasmPedigreeV1Dao dao;
    String expectedPedigreeFull = """
                {
                          "groupId" : 0,
                          "germplasmDbId" : "aHR0cHM6Ly9kb2kub3JnLzEwLjE1NDU0LzZHRlc4VQ==",
                          "defaultDisplayName" : "661300425",
                          "crossingPlan" : "Factorial",
                          "familyCode" : "0504B",
                          "parent1DbId" : "aHR0cHM6Ly9kb2kub3JnLzEwLjE1NDU0L1NQQTBRSQ==",
                          "parent1Name" : "73028-62",
                          "parent1Type" : "FEMALE",
                          "parent2DbId" : "aHR0cHM6Ly9kb2kub3JnLzEwLjE1NDU0L0cwT1NVQg==",
                          "parent2Name" : "101-74",
                          "parent2Type" : "MALE",
                          "siblings" : [
                            {
                              "germplasmDbId" : "aHR0cHM6Ly9kb2kub3JnLzEwLjE1NDU0L0ZaWDhLTw==",
                              "defaultDisplayName" : "661302619",
                              "germplasmURI" : "https://doi.org/10.15454/FZX8KO"
                            },
                            {
                              "germplasmDbId" : "aHR0cHM6Ly9kb2kub3JnLzEwLjE1NDU0L1hZREs0TA==",
                              "defaultDisplayName" : "661302686",
                              "germplasmURI" : "https://doi.org/10.15454/XYDK4L"
                            }
                          ],
                          "germplasmPedigreeDbId" : "dXJuOlVSR0kvZ2VybXBsYXNtUGVkaWdyZWUvMTk4MzYyNjQxOA==",
                          "germplasmPedigreeURI" : "urn:URGI/germplasmPedigree/1983626418",
                          "@type" : "germplasmPedigree",
                          "@id" : "urn:URGI/germplasmPedigree/1983626418",
                          "schema:includedInDataCatalog" : "https://urgi.versailles.inrae.fr/gnpis",
                          "schema:identifier" : "1983626418",
                          "schema:name" : null,
                          "germplasmURI" : "https://doi.org/10.15454/6GFW8U",
                          "parent1URI" : "https://doi.org/10.15454/SPA0QI",
                          "parent2URI" : "https://doi.org/10.15454/G0OSUB"
                        }
                """;

    String expectedPedigree = """
            {
                             "groupId": 0,
                             "germplasmDbId": "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzQ1MDM3-test",
                             "defaultDisplayName": "661300238",
                             "crossingPlan": "Factorial",
                             "crossingYear": null,
                             "familyCode": "0504B",
                             "pedigree": null,
                             "parent1DbId": "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzQzMTY1",
                             "parent1Name": "73028-62",
                             "parent1Type": "FEMALE",
                             "parent2DbId": "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzQzMTQ4",
                             "parent2Name": "101-74",
                             "parent2Type": "MALE",
                             "siblings": [
                               {
                                 "germplasmDbId": "46452",
                                 "defaultDisplayName": "661303513"
                               },
                               {
                                 "germplasmDbId": "46389",
                                 "defaultDisplayName": "661303449"
                               }
                             ],
                             "germplasmPedigreeDbId": "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtUGVkaWdyZWUvNDE1MTAxODg4-test",
                             "germplasmPedigreeURI": "urn:INRAE-URGI/germplasmPedigree/415101888-test",
                             "germplasmURI": "urn:INRAE-URGI/germplasm/45037-test",
                             "parent1URI": "urn:INRAE-URGI/germplasm/43165",
                             "parent2URI": "urn:INRAE-URGI/germplasm/43148",
                             "@id": "urn:INRAE-URGI/germplasmPedigree/415101888-test",
                             "@type": "germplasmPedigree"
                           }
                """;

    /**
     * To use for GermplasmController{
     *     germplasmRepository.findPedigree(germplasm.getGermplasmDbId());
     * }
     */
    @Test
    public void should_get_one_pedigree_perDbId(){
        GermplasmPedigreeV1VO vo = dao.getByGermplasmDbId("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzQ1MDM3");
        assertThat(vo).isNotNull();
        assertThat(vo.getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzQ1MDM3");
        ObjectMapper jacksonMapper = new ObjectMapper();
//        try {
//            JSONAssert.assertEquals(expectedPedigree, jacksonMapper.writeValueAsString(vo), JSONCompareMode.LENIENT);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        } // too long as a document, find a better test case


    }
}
