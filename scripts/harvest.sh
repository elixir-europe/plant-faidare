#!/bin/bash


DATA_DIR=""
ES_HOST="localhost"
ES_PORT="9200"
ENV="dev"
DOCUMENT_TYPES="all"

ALL_DOCUMENT_TYPES="germplasm germplasmMcpd germplasmAttribute germplasmPedigree germplasmProgeny location program study trial observationUnit xref"
ALL_ENVS="dev beta staging int prod test"
BASEDIR=$(readlink -f "$(dirname $0)")

RED='\033[0;31m'
GREEN='\033[0;32m'
ORANGE='\033[0;33m'
BOLD='\033[1m'
NC='\033[0m' # No format


help() {
	cat <<EOF
DESCRIPTION:
	Script used to index data in FAIDARE

USAGE:
	${SCRIPT} -jsonDir <JSON directory> -es_host <Elasticsearch node host> -es_port <Elasticsearch node port> -env <application environment name> -type <document type(s) to index> [-v|--verbose] [-h|--help]

PARAMS:
	-jsonDir        The directory where JSON bulk files to index are
	-es_host        The hostname or IP of Elasticsearch node (local, dev or prod cluster). Default: $ES_HOST
	-es_port        The port of Elasticsearch node (local, dev or prod cluster). Default: $ES_PORT
	-env            The environment name of the targeted FAIDARE application ($(echo $ALL_ENVS | tr ' ' ', ')). Default: $ENV
	-type           The document type(s) to index, separated by a comma ($(echo $ALL_DOCUMENT_TYPES | tr ' ' ', ')). Default: $DOCUMENT_TYPES
	-h|--help       Display this message
	-v|--verbose    Verbose mode
	--debug         Debug mode (same as set -x)

DEPENDENCIES:
	- jq 1.6+: https://github.com/stedolan/jq/releases/tag/jq-1.6
	- GNU parallel: https://www.gnu.org/software/parallel/
	- gzip: http://www.gzip.org/

EOF
	exit 1
}

check_acknowledgment() {
	local LOG=$1
	local MSG=$2
	echo $LOG | jq '.acknowledged? == true' | grep 'true' >/dev/null || {
		echo -e "${RED}ERROR: a problem occurred when trying to ${MSG}.${NC}"
		echo -e "${ORANGE}$(echo "${LOG}")${NC}"
		exit 1;
	}
}

# Check programs
MISSING_COUNT=0
PROGRAMS="gzip parallel jq"
for PROGRAM in ${PROGRAMS}; do
	command -v "${PROGRAM}" >/dev/null || {
		echo -e "${ORANGE}Program ${PROGRAM} is missing, cannot continue...${NC}"
		((MISSING_COUNT += 1))
	}
done
if [ $MISSING_COUNT -ne 0 ]; then
	echo -e "${RED}ERROR: You must install the $MISSING_COUNT missing program(s).${NC}"
	exit $MISSING_COUNT
fi

# Get and Check parameters
# any params
[ -z "$1" ] && echo && help
# get params
while [ -n "$1" ]; do
	case "$1" in
		-h|--help) help;;
		-v|--verbose) VERBOSE=1;shift 1;;
		--debug) set -x;shift 1;;
		-jsonDir) DATA_DIR="$(readlink -f $2)"; shift 2;;
		-es_host) ES_HOST="$2"; shift 2;;
		-es_port) ES_PORT="$2"; shift 2;;
		-env) ENV="$2"; shift 2;;
		-type) DOCUMENT_TYPES=$(echo "$2" | tr ',' ' '); shift 2;;
		-*) echo -e "${RED}Unknown option: $1 ${NC}\n" echo && help;;
		*) break;;
    esac
done

if [ -z "$ES_HOST" ] || [ -z "$ES_PORT" ]; then
	echo -e "${RED}ERROR: 'es_host' and 'es_port' parameters are mandatory!${NC}"
	echo && help
fi
if [[ "$ALL_ENVS" != *"$ENV"* ]]; then
	echo -e "${RED}ERROR: The value of parameter 'env' must be one of the following: ${ALL_ENVS}.${NC}"
	echo && help
fi
if [ ! -d "${DATA_DIR}" ]; then
	echo -e "${RED}ERROR: Mandatory parameter 'jsonDir' is missing!${NC}"
	echo && help
fi
if [ $(find "${DATA_DIR}" -name "*.json" | wc -l) -le 0 ] && [ $(find "${DATA_DIR}" -name "*.json.gz" | wc -l) -le 0 ]; then
	echo -e "${RED}ERROR: The JSON directory ${DATA_DIR} contains no JSON files!${NC}"
	echo && help
fi
[ "${DOCUMENT_TYPES}" == "all" ] && DOCUMENT_TYPES="${ALL_DOCUMENT_TYPES}"
for DOCUMENT_TYPE in ${DOCUMENT_TYPES}; do
	if [ $(find "${DATA_DIR}" -name "${DOCUMENT_TYPE}*.json" | wc -l) -le 0 ] && [ $(find ${DATA_DIR} -name "${DOCUMENT_TYPE}*.json.gz" | wc -l) -le 0 ]; then
		echo -e "${ORANGE}WARNING: The JSON directory ${DATA_DIR} contains no ${DOCUMENT_TYPE} document. Type will be ignored!${NC}"
		DOCUMENT_TYPES=$(echo "${DOCUMENT_TYPES}" | sed "s/ *${DOCUMENT_TYPE} */ /g")
	fi
done

LOG_DIR="${DATA_DIR%/}/indexing-log"

if [[ -d ${LOG_DIR} ]]
then
    rm -r "${LOG_DIR}"
fi

TMP_FILE="/tmp/bulk/log.tmp"
[ -f "$TMP_FILE" ] && rm -f "$TMP_FILE"

OUTDIR="/tmp/bulk/${INDEX_NAME}"
[ -d "$OUTDIR" ] && rm -rf "$OUTDIR"
mkdir -p "$OUTDIR"

export ES_HOST ES_PORT INDEX_NAME LOG_DIR OUTDIR BASEDIR


index_resources() {
    SOURCE=$(basename $(dirname "$1"))
    bash -c "set -o pipefail; gunzip -c $1 \
            | jq -c -f ${BASEDIR}/to_bulk.jq 2> ${OUTDIR}/${SOURCE}-$2.jq.err \
            | gzip -c \
            | curl -s -H 'Content-Type: application/x-ndjson' -H 'Content-Encoding: gzip' -H 'Accept-Encoding: gzip' \
               -XPOST \"${ES_HOST}:${ES_PORT}/${INDEX_NAME}/_bulk\"\
               --data-binary '@-' 2> ${OUTDIR}/${SOURCE}-$2.jq.err  > ${OUTDIR}/${SOURCE}-$2-resources.log.gz "
}

export -f index_resources

for DOCUMENT_TYPE in ${DOCUMENT_TYPES}; do
	echo && echo -e "${BOLD}Manage ${DOCUMENT_TYPE} documents...${NC}"
	INDEX_PATTERN=$(echo "faidare_${DOCUMENT_TYPE}_${ENV}" | sed -E "s/([a-z])([A-Z])/\1-\2/" | tr '[:upper:]' '[:lower:]')

	# Create template
	TEMPLATE_NAME="${INDEX_PATTERN}_template"
	echo -e "* Create setting/mapping template ${TEMPLATE_NAME}..."
	LOG=$(curl -s -H 'Content-Type: application/json' -XPUT "${ES_HOST}:${ES_PORT}/_template/${TEMPLATE_NAME}" -d"
{
	\"index_patterns\": [\"${INDEX_PATTERN}-*\"],
	\"order\": 101,
	\"mappings\":
		$(cat "${BASEDIR}"/../backend/src/test/resources/fr/inra/urgi/faidare/repository/es/setup/index/${DOCUMENT_TYPE}_mapping.json),
	\"settings\": $(cat "${BASEDIR}"/../backend/src/test/resources/fr/inra/urgi/faidare/repository/es/setup/index/settings.json)
}")
	check_acknowledgment "${LOG}" "create template"

	# Index JSON Bulk
	INDEX_NAME="${INDEX_PATTERN}-d"$(date +%s)
	echo -e "* Index documents into ${ES_HOST}:${ES_PORT}/${INDEX_NAME} indice..."
	{
		parallel -j 4 --bar index_resources {1} {1/.} ::: "$(find "${DATA_DIR}" -name "${DOCUMENT_TYPE}-*.json.gz")"
	}

	# check all JQ err files...
    FILES_IN_ERROR=$(find "${OUTDIR}" -size "+0" -name "*${DOCUMENT_TYPE}*jq.err")
    [ -n "${FILES_IN_ERROR}" ] && { echo -e "${RED}ERROR: some problems occured with JQ processing, look at files:${ORANGE} ${FILES_IN_ERROR}${NC}" ; exit 4 ; }

    parallel "gunzip -c {} | jq '.errors' | grep -q true && echo -e '${ORANGE}ERROR found in {}${NC}' >> ${TMP_FILE} ;" ::: "$(find "${OUTDIR}" -name "*${DOCUMENT_TYPE}-*.log.gz")"
    parallel "gunzip -c {} | jq '.error' | grep -q true && echo -e '${ORANGE}ERROR related to data found when indexing {}${NC}' >> ${TMP_FILE} ;" ::: ${OUTDIR}/*${DOCUMENT_TYPE}*-resources.log.gz
    parallel "gunzip -c {} | jq '.error? | length == 0' | grep -q false && echo -e '${ORANGE}ERROR related to Elasticsearch API usage found when indexing {}${NC}' >> ${TMP_FILE} ;" ::: ${OUTDIR}/*${DOCUMENT_TYPE}*-resources.log.gz
	if [ -f "${TMP_FILE}" ] && [ -s "${TMP_FILE}" ]; then
		echo -e "${RED}ERROR: a problem occurred when trying to index data into ${ES_HOST}:${ES_PORT}/${INDEX_NAME} indice.${NC}"
		echo -e "${ORANGE}$(cat ${TMP_FILE})${NC}"
		rm "${TMP_FILE}"
		exit 1;
	fi

    # Check indexed data
    echo -e "* Check data indexed from ${DATA_DIR} into ${INDEX_NAME}..."
    	# skip some documents because they contain nested objects that distort the count
    	if [[ "${DOCUMENT_TYPE}" != "germplasmAttribute" && "${DOCUMENT_TYPE}" != "trial" ]]; then
    		COUNT_EXTRACTED_DOCS=0
    		for FILE in $(find ${DATA_DIR} -name "${DOCUMENT_TYPE}-*.json.gz"); do
    			COUNT_FILE_DOCS=$(zcat ${FILE} | grep -o "\"@id\"" | wc -l)
    			COUNT_EXTRACTED_DOCS=$((COUNT_EXTRACTED_DOCS+COUNT_FILE_DOCS))
    		done
    		curl -s -XGET "${ES_HOST}:${ES_PORT}/${INDEX_NAME}/_refresh" >/dev/null
    		COUNT_INDEXED_DOCS=$(curl -s -XGET "${ES_HOST}:${ES_PORT}/_cat/indices/${INDEX_NAME}?h=docs.count")
    	fi
    	if [ "$COUNT_INDEXED_DOCS" != "$COUNT_EXTRACTED_DOCS" ]; then
    		echo -e "${RED}ERROR: a problem occurred when indexing data from ${DATA_DIR} on FAIDARE ${ENV}.${NC}"
    		echo -e "${ORANGE}Expected ${COUNT_EXTRACTED_DOCS} documents but got ${COUNT_INDEXED_DOCS} indexed documents.${NC}"
    		exit 1;
    	fi
    	sleep 5

	# Add aliases
	ALIAS_PATTERN="${INDEX_PATTERN}-group*"
	ALIAS_EXIST=$(curl -s -XGET "${ES_HOST}:${ES_PORT}/_alias/${ALIAS_PATTERN}" | jq '.status' | grep -q -e "404" -e 'null' && echo "false" || echo "true")
	if [ "${ALIAS_EXIST}" == "true" ]; then
		echo -e "* Delete aliases ${ALIAS_PATTERN}..."
		LOG=$(curl -s -XDELETE "${ES_HOST}:${ES_PORT}/*/_aliases/${ALIAS_PATTERN}")
		check_acknowledgment "${LOG}" "delete aliases"
	fi

	echo -e "* List groupId from ${INDEX_NAME} (to create filtered aliases)..."
	GROUP_IDS=$(curl -s -H 'Content-Type: application/json' -XGET "${ES_HOST}:${ES_PORT}/${INDEX_NAME}/_search" -d'
{
	"size":"0",
	"aggs" : {
		"uniq_group" : {
			"terms" : {
				"field" : "groupId",
				"size": 99999
			}
		}
	}
}' | jq -cr '.aggregations.uniq_group.buckets[].key') # Extract ES aggregation bucket keys
	[ -z "$GROUP_IDS" ] && {
		echo -e "${ORANGE}WARNING: could not list 'groupId' values from index. Defaulting to 'groupId' = 0. ${NC}"
		GROUP_IDS="0"
	}
	echo -e "* Create aliases:"
	for GROUP_ID in ${GROUP_IDS}; do
		ALIAS_NAME="${INDEX_PATTERN}-group${GROUP_ID}"
		FILTER=""
		if [[ "$GROUP_ID" = "0" || "${GROUP_ID}" =  "null" ]]; then
			FILTER="{ \"bool\": { \"should\": [ { \"bool\": { \"must_not\": { \"exists\": { \"field\": \"groupId\" } } } }, { \"term\": { \"groupId\": 0 } } ] } }"
		else
			FILTER="{ \"term\" : { \"groupId\" : \"${GROUP_ID}\" } }"
		fi
		echo -e "\t${ALIAS_NAME}..."
		LOG=$(curl -s -H 'Content-Type: application/json' -XPOST "${ES_HOST}:${ES_PORT}/_aliases" -d "
{
	\"actions\" : [
		{
			\"add\" : {
				\"index\" : \"${INDEX_NAME}\",
				\"alias\" : \"${ALIAS_NAME}\",
				\"filter\" : ${FILTER}
			}
		}
	]
}")
		check_acknowledgment "${LOG}" "create aliase"
	done

	# Delete all but last created indices (thanks to the timestamp suffix)
	echo -e "* Delete old indices ${INDEX_PATTERN} (to avoid accumulation over time):"
	OLD_INDICES=$(curl -sf -XGET "${ES_HOST}:${ES_PORT}/_cat/indices/${INDEX_PATTERN}*?h=index" | sort | head -n -1)
	for OLD_INDEX in ${OLD_INDICES}; do
		echo -e "\t${OLD_INDEX}..."
		LOG=$(curl -s -XDELETE "${ES_HOST}:${ES_PORT}/${OLD_INDEX}")
		check_acknowledgment "${LOG}" "delete index ${OLD_INDEX}"
	done
done
