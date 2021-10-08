FROM alpine
LABEL Author="Raphaël FLORES <raphael.flores@inrae.fr>"

COPY scripts/harvest.sh /opt/scripts/
COPY scripts/to_bulk.jq /opt/scripts/

# COPY dao settings
COPY backend/src/test/resources/fr/inra/urgi/faidare/repository/es/setup/index/settings.json /opt/backend/src/test/resources/fr/inra/urgi/faidare/repository/es/setup/index/settings.json

# COPY dao mappings
COPY backend/src/test/resources/fr/inra/urgi/faidare/repository/es/setup/index/*_mapping.json /opt/backend/src/test/resources/fr/inra/urgi/faidare/repository/es/setup/index/

RUN apk add --update --no-cache bash curl jq parallel wget grep gzip sed date coreutils

RUN chmod +x /opt/scripts/harvest.sh
RUN mkdir ~/.parallel && touch ~/.parallel/will-cite

ENTRYPOINT ["/opt/scripts/harvest.sh"]
