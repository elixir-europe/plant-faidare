

# TL;DR
For general data loading commands, see [Data Harvesting and Indexing](#Data Harvesting and Indexing).

For **loading test data**, see [Test data](#test-data) below.


## Data Harvesting and Indexing

Before all, take care to get data locally before running any indexing script.

### Indexing commands without Docker

Start an elasticsearch instance using 
```sh
docker-compose up
```
Then, to index the data in the specified directory (`/path/to/local/data/`) into the Elasticsearch instance running on localhost, run the following command:

```sh
./scripts/harvest.sh -jsonDir /path/to/local/data/ -es_host localhost -env dev -v
```
See `./scripts/harvest.sh --help` for more information.

### Indexing commands with Docker per Operating System

The FAIDARE Docker image uses Alpine Linux as the base, which can lead to compatibility issues on certain systems, such as macOS with ARM processors (Apple Silicon). Below are the specific instructions for running the indexing command on Linux, macOS, and Windows:
-    Finding the Container for `--network`
     To determine the name of the container to use with the `--network=container:<container_name>` option, run the following command:
```sh
docker ps

```
This will list all running containers. Look for the container name in the NAMES column. For example, if the container name is `elasticsearch-faidare`, you would use it as follows:
```sh
--network=container:elasticsearch-faidare

```

1.  Linux
    Run the command as is:
```sh
docker run -t --volume /path/to/local/data:/opt/data/ --network=container:elasticsearch-faidare registry.forgemia.inra.fr/urgi-is/docker-rare/faidare-loader:latest -jsonDir /opt/data/

```
2. MacOS:
   On Apple Silicon (ARM64), ensure Rosetta is enabled for Docker Desktop and specify the platform explicitly:
```sh
docker run --platform linux/amd64 -t --volume /path/to/local/data:/opt/data/ --network=container:elasticsearch-faidare registry.forgemia.inra.fr/urgi-is/docker-rare/faidare-loader:latest -jsonDir /opt/data/

```
For Intel-based Macs, no additional flags are needed:
```sh
docker run -t --volume /path/to/local/data:/opt/data/ --network=container:elasticsearch-faidare registry.forgemia.inra.fr/urgi-is/docker-rare/faidare-loader:latest -jsonDir /opt/data/

```
3.  Windows:
    Adapt the volume path to Windows format (e.g., C:/path/to/local/data):
```sh
docker run -t --volume C:/path/to/local/data:/opt/data/ --network=container:elasticsearch-faidare registry.forgemia.inra.fr/urgi-is/docker-rare/faidare-loader:latest -jsonDir /opt/data/

```

To more help add `--help` parameter to the command.

If you depend on committed changes in indexing scripts under a specific branch (the docker image should have been automatically created by the CI), you need to change the tag of the docker image according to the branch name (ie. for branch `epic/merge-faidare-dd`, use tag `epic-merge-faidare-dd`, see `CI_COMMIT_REF_SLUG` [Gitlab predefined variable](https://docs.gitlab.com/ee/ci/variables/predefined_variables.html#predefined-variables-reference)), as following:

```sh
docker run -t --volume /path/to/local/data:/opt/data/ --network=container:elasticsearch-faidare registry.forgemia.inra.fr/urgi-is/docker-rare/faidare-loader:epic-merge-faidare-dd` -jsonDir /opt/data/ --help
```

### Docker container maintenance

[Data Harvesting and Indexing](#Data Harvesting and Indexing) section above expects to have an available docker image on the forgemia docker registry. The Gitlab CI rebuild it when needed, but you can update or push such an image using the following commands:

```sh
# build the image
docker build -t registry.forgemia.inra.fr/urgi-is/docker-rare/faidare-loader:latest .

# Login before pushing the image
TOKEN= # your PAT from  forgeMIA
echo "$TOKEN" | docker login registry.forgemia.inra.fr/urgi-is/docker-rare -u <your ForgeMIA username>  --password-stdin

# push the built image
docker push registry.forgemia.inra.fr/urgi-is/docker-rare/faidare-loader:latest
```

That should ease the indexing of data without having to craft a dedicated environment.



## Test data
`docker compose up` will start an Elasticsearch instance

To load the test data  with the necessary indices and mappings, you can use one of the following methods:
1.  Using the Docker image (recommended for simplicity and consistency).
2.  Running the script locally on your machine.

### Option 1: Using the Docker Image
For detailed instructions on using the FAIDARE Docker image, refer to the relevant section in the  [README.md](https://forgemia.inra.fr/urgi-is/faidare/-/blob/fix/NewReadMeHowToDevelopOnFaidare/README.md#data-harvesting-and-indexing).
  ```sh
docker run -t --volume ./data/test:/opt/data/ --network=container:elasticsearch-faidare registry.forgemia.inra.fr/urgi-is/docker-rare/faidare-loader:latest -jsonDir /opt/data/
  ```
*NB*: adapt the docker command depending on your [operating system](### Indexing commands with Docker per Operating System). 

*NB2*: Ensure you have an up to date access token to the container registry. If not, you can generate one from the [ForgeMIA](https://forgemia.inra.fr/urgi-is/docker-rare/-/settings/access_tokens) website or contact us.

For instance for **MacOS ARM on the new-api branch**, the command would be:
```sh   
docker compose up
```

```sh   
docker run --platform linux/amd64 -t --volume ./data/test:/opt/data/ --network=container:elasticsearch-faidare registry.forgemia.inra.fr/urgi-is/docker-rare/faidare-loader:feat-upgrade-to-new-api -jsonDir /opt/data/
```

### Option 2: Running the Script Locally
If you prefer, you can run the `harvest.sh` script directly on your machine. However, please ensure the following dependencies are installed:
- jq (v1.6+): https://github.com/stedolan/jq/releases/tag/jq-1.6
- GNU parallel: https://www.gnu.org/software/parallel/
- gzip: http://www.gzip.org/

**Instructions by Operating System**

1.  Linux: Run the script as follows:

`./scripts/harvest.sh -jsonDir data/test/ -es_host localhost -env dev -v`

2.  macOS: Ensure GNU utilities like readlink are available. If using a macOS-specific environment, you might need to install them using Homebrew:

`brew install coreutils gnu-parallel jq gzip`

Then run:

`./scripts/harvest.sh -jsonDir data/test/ -es_host localhost -env dev -v`

3.  Windows: You can run the script using a Bash environment like Git Bash, WSL, or Cygwin. Ensure all required dependencies are installed within the environment:

    `./scripts/harvest.sh -jsonDir data/test/ -es_host localhost -env dev -v`

Note for macOS and Windows users: Compatibility issues might arise due to system differences. If you encounter any issues, it is recommended to use the Docker-based method.


