# How to contribute to GPDS

## Git management

### Branches

* One stable & protected master branch
* Feature branches for development following the pattern `[dev_type]/[dev_name]` (ie. `chore/explaining_how_to_merge`) where [dev_type] can be:
	* fix (bug fixes)
	* feat (new feature)
	* style (style modification)
 	* refactor (code refactoring)
 	* chore (base maintenance such as version bump)

### Commit & branches merge
* Commit name should follow pattern `[dev_type]: [brief description of the commit, lower than 50 characters]. [JIRA-KEY]`
* All branches must be merged via a `merge request` (MR)
* Merge requests should be created at the time of the branch creation in order to allow reviewer to comment and follow the developments, specify the `WIP` tag in the MR name (go further: *[feature highlight WIP](https://about.gitlab.com/2016/01/08/feature-highlight-wip/)*).  
	Example:
	```
	git checkout -b chore/explaining_how_to_merge
	git push --set-upstram origin chore/explaining_how_to_merge
	```
	Returns a link for creating the merge request easily: 
	```
	Total 0 (delta 0), reused 0 (delta 0)
	remote:
	remote: To create a merge request for chore/explaining_how_to_merge, visit:
	remote:   https://forgemia.inra.fr/urgi-is/gpds/merge_requests/new?merge_request%5Bsource_branch%5D=chore/explaining_how_to_merge
	remote:
	To forgemia.inra.fr:urgi-is/gpds.git
	* [new branch]      chore/explaining_how_to_merge -> chore/explaining_how_to_merge
	La branche 'chore/explaining_how_to_merge' est paramétrée pour suivre la branche distante 'chore/explaining_how_to_merge' depuis 'origin'.
	```

* A `git rebase` is strongly recommanded before merging a MR
	* [Git rebase official documentation](https://git-scm.com/book/en/v2/Git-Branching-Rebasing)
	* [How to keep a clean history](https://about.gitlab.com/2018/06/07/keeping-git-commit-history-clean/)
* Merge requests should be reviewed by at least 2 colleagues
* Continuous Integration is launched automatically by Gitlab on each commit push or merge request creation.

## Data management
* Git LFS feature is enable on this project in order to store JSON test data
* Another dedicated Git LFS project (internal only) will be created to handle all private + public JSON files
* TODO: refer to the good Git LFS project in the CI settings for indexing the relevant data into the relevant Elasticsearch indices/instances
* the JSON files generation will be handled by an external ET (extract/transform) tool.

## Development environment
* Look at the [README.md](README.md) for installation and execution instructions.
* Recommanded IDE is [Intellij IDEA](https://www.jetbrains.com/idea/)
* Use linting to apply code standards within the team:
	* Use `ng lint` (for frontend code only)
	* Use [Checkstyle](https://checkstyle.org/) and [PMD](https://pmd.github.io/) (TODO: implement) for backend (+frontend?) code
* All runtime variables should be externalized from the code in order to facilitate the CI management (database host/port, application name, public URL, JSON location...) and the adoption by partners
