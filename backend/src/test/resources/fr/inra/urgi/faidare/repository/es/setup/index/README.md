This directory contains document mappings and index settings for FAIDARE Elasticsearch documents.

This directory is a copy of the directory from the GnpIS `database` git repository with the path: `GnpIS/elasticsearch/template/gnpis`.


# Custom analyzers

In the `settings.json` file, a custom analyzer is defined to index completion suggestion.

The `index_suggester` is composed of a custom tokenizer named `special_tokenizer` and which uses a RegExp pattern to list all characters used to split tokens. The split characters includes: spaces, comas, dashes (low and high), parentheses (open and close), brackets (open and close) and curly brackets (open and close). The `index_suggester` is also composed of filters including the standard filter, the lowercase filter, the ascii folding filter and a NGram filter (with minimum of 1 and maximum of 20 characters starting on the front of a token).

The `search_suggester` is the same as `index_suggester` but without the NGram filter.
