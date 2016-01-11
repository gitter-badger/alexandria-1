See the [generated Maven site](http://huygensING.github.io/alexandria) for documentation on Alexandria.
------

[![Join the chat at https://gitter.im/HuygensING/alexandria](https://badges.gitter.im/HuygensING/alexandria.svg)](https://gitter.im/HuygensING/alexandria?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

##### (cheat-sheet to publish the site):

###### Dry-run of publishing the site to github.io via the `gh-pages` branch of the project:
	mvn scm-publish:publish-scm -Dscmpublish.dryRun=true

###### Generating the site and publishing it:
	mvn clean verify site:site scm-publish:publish-scm
