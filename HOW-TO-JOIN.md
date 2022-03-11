# How to join FAIDARE federation?

If you want your information system to be part of the FAIDARE federation, we invite you to [contact us](mailto:urgi-contact@inra.fr?subject=%5BFAIDARE%5D) as soon as possible so that we can follow your progress and assist you if needed.

You will need to create a new `source` corresponding to your information system.
For this, you will have to set up your own [BrAPI](https://brapi.org/) endpoint and reference it in the sources of [Elixir's FAIDARE harvester](https://github.com/elixir-europe/plant-brapi-etl-data-lookup-gnpis).

General information and considerations can be found on the [Elixir Plant](https://elixir-europe.org/communities/plant-sciences) web page.

## Creation of a BrAPI endpoint
To create a new endpoint, you should implement the BrAPI calls needed to give access to the data of your information system.
Those calls are documented in various formats on the [Developer resources](https://www.brapi.org/developers#) section of the BrAPI web site.

The BrAPI calls currently used by FAIDARE are:
* germplasm
* location
* ontology
* program
* study
* study/{studyDbId}/observationVariable
* study/{studyDbId}/germplasm
* study/{studyDbId}/observationUnit (can be resource intensive and therefore not implemented)
* trial

> Note that since the tool makes a backlink to your information system, we need a URL in the `DocumentationURL` field of the BrAPI for researchers to get more detailed information about the indexed entry directly in your information system.

To ensure the quality of your BrAPI endpoint, you can use the validation tools provided by the BrAPI community, especially [Brava](http://webapps.ipk-gatersleben.de/brapivalidator/).

If you have any question or need help implementing BrAPI calls, you can [contact the BrAPI community](https://brapi.org/).

## Referencement of a BrAPI endpoint
For your endpoint to be visible on FAIDARE, you have to declare it in the sources of [Elixir's FAIDARE harvester](https://github.com/elixir-europe/plant-brapi-etl-data-lookup-gnpis).
To do this, you only have to create your own configuration file, according to the following template, and add it to the [`sources`](https://github.com/elixir-europe/plant-brapi-etl-data-lookup-gnpis/tree/master/sources) directory of the harvester:
```
{
  "@context": {
    "schema": "http://schema.org/",
    "brapi": "https://brapi.org/"
  },
  "@type": "schema:DataCatalog",
  "@id": "[information system URL]",
  "schema:identifier": "[BrAPI endpoint name]",
  "schema:name": "[information system name]",
  "brapi:endpointUrl": "[BrAPI endpoint URL]"
}
```
Example: [URGI.json](https://github.com/elixir-europe/plant-brapi-etl-data-lookup-gnpis/blob/master/sources/URGI.json)
```
{
  "@context": {
    "schema": "http://schema.org/",
    "brapi": "https://brapi.org/"
  },
  "@type": "schema:DataCatalog",
  "@id": "https://urgi.versailles.inra.fr/gnpis",
  "schema:identifier": "URGI",
  "schema:name": "URGI GnpIS",
  "brapi:endpointUrl": "https://urgi.versailles.inra.fr/faidare/brapi/v1/"
}
```

If you have any question or need help referencing your endpoint, you can [contact us](mailto:urgi-contact@inra.fr?subject=%5BFAIDARE%5D).

## Data availability & update
[Elixir's FAIDARE harvester](https://github.com/elixir-europe/plant-brapi-etl-data-lookup-gnpis) extract the metadata available from all declared sources (_i.e._ BrAPI endpoint) and index it into a centralised Elasticsearch cache.
The sources are reindexed regularly (once a month maximum) but if you want a reindexation following a major update on your side, please [inform us](mailto:urgi-contact@inra.fr?subject=%5BFAIDARE%5D).
