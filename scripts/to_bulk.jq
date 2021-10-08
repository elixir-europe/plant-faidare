#!/usr/bin/env jq -Mf

# Transforms a JSON array of documents in Elasticsearch compliant bulk file
# using value of 'ID_FIELD' variable if provided, or using concatenation of
# several fields, as the Elasticsearch documents identifier
# The input data must be of type 'JSON array'.
# Return codes:
#   - 0 if process succeeded
#   - 2 if the input is not a JSON array

def to_string:
    if ((. | type) == "array") then
        .|join("|")|tostring
    else
        .|tostring
    end
    | if(. == "null" or . == null) then
        ""
    else
        .
    end
;

def to_bulk:
    .[] | . |= {"index":  {"_type": "_doc", "_id": ."@id" }}, .
;

to_bulk
