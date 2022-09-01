package fr.inra.urgi.faidare.web.germplasm;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The fields of a germplasm MCPD that can be exported
 *
 * @author JB Nizet
 */
public enum GermplasmMcpdExportableField {
    PUID,
    INSTCODE,
    ACCENUMB,
    COLLNUMB,
    COLLCODE,
    COLLNAME,
    COLLINSTADDRESS,
    COLLMISSID,
    GENUS,
    SPECIES,
    SPAUTHOR,
    SUBTAXA,
    SUBTAUTHOR,
    CROPNAME,
    ACCENAME,
    ACQDATE,
    ORIGCTY,
    COLLSITE,
    DECLATITUDE,
    LATITUDE,
    DECLONGITUDE,
    LONGITUDE,
    COORDUNCERT,
    COORDDATUM,
    GEOREFMETH,
    ELEVATION,
    COLLDATE,
    BREDCODE,
    BREDNAME,
    SAMPSTAT,
    ANCEST,
    COLLSRC,
    DONORCODE,
    DONORNAME,
    DONORNUMB,
    OTHERNUMB,
    MLSSTAT,
    REMARKS
}
