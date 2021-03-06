/**
 * This package provides generic query building and query execution for Elasticsearch
 * <p>
 * - the "document" package provides '@' annotations on document value objects that can be used to add metadata like the
 * identifier field, nested objects and the document type.
 * <p>
 * - the "criteria" package provides '@' annotations on criteria value objects that can be used to map criterion to
 * document field (and thus automatically generate Elasticsearch queries from criteria VO)
 * <p>
 * - the "query" package provides generic Elasticsearch query building from criteria VO based on the above utilities
 * <p>
 * - the "repository" package provides generic Elasticsearch repository interfaces and implementation based on the above
 * utilities
 *
 * @author gcornut
 */
package fr.inra.urgi.faidare.elasticsearch;
