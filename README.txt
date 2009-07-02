This document contains a list of FAQs intended to help any developer looking at this project for the first time.

FAQ 1) How do I build the project?

    Make sure you have Ant installed on your machine and use build target in build.xml.

    2) Where is the project build directory?

    If you execute the ant build script (build.xml), the project is built to a directory called "distribution"
    In this directory you will find a zip and tar file containing all installation and user guides, IntraLibrarySearchModule.jar
    and its dependencies.

    For convenience,  an unzipped copy of everything can also be found in the distribution.

    3) How do I install the plugin on Blackboard?

    Be sure to read the installation guide and then the user guide which can be found in distribution/docs once you have built the project.
    Aurigadoc is used to generate the documentation and the document source files can be found in the docs folder.

    4) **NB:  I have made changes to the source code, rebuilt the project, redeployed to webct, restarted the webct server but
    my changes are still not taking effect, WHY?  **

    To find out how to re-deloy/ or upgrade a power link, you should really refer to the WebCT/Blackboard Administrators guide, however
    this is what you need to know:

    There are two ways of getting blackboard to detect that IntraLibrarySearchModule.jar has been changed

    a) Before building IntraLibrarySearchModule.jar and deploying to the Blackboard server, increment the version number in DeployableComponentConfig.xml.
      Blackboard will then detect that a new version has been deployed.

    b) Login to the blackboard application as server admin.  Under admin->utilities->settings->proxy tools, delete the IntraLibrary proxy link.
       This will remove all instances of links in any courses created by the IntraLibary proxy link.  This means you cannot do this
       on a production Blackboard server.  Stop the Blackboard server.  Remove the IntraLibrarySearchModule from deployablecomponets directory
       on the server.  Restart the server.  Login as server admin and under admin->utilities->settings->->deployable components confirm that
       INtraLibrarySearchModule is no longer listed.  Stop the server.  Follow the install instructions in the installation guide.

