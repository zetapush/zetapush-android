
# Update the lib

* update the changelog with the last changement and the linked PR
* update `build.gradle` and `property.gradle` with the latest version number
* commit and merge into master
* tag your merge on master with the lateste version number ex `X.Y.Z`
* to upload yourlib into nexus RM of Maven central, launch the following command : `./gradlew uploadArchives`

After this you have done for the code part, all things to do now is online

* go to [Maven nexus repository manager url](https://oss.sonatype.org/#stagingRepositories) to prepare your build to be reviewed. 


# UseFull links

* To manage maven central library publication, please check this [link](https://android.jlelse.eu/the-complete-guide-to-creating-an-android-library-46628b7fc879). And this link for mistake that link before made in gpg key new version [here](https://medium.com/@zubairehman.work/a-complete-guide-to-create-and-publish-an-android-library-to-maven-central-6eef186a42f5).
* [Sonatype Jira ticket url](https://issues.sonatype.org/browse/OSSRH-48829)
* [Maven nexus repository manager url](https://oss.sonatype.org/#stagingRepositories)
* [gradle, please url](http://gradleplease.appspot.com/) (to check if library is up)