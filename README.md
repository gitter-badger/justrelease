
#JustRelease

JustRelease is a release library such that you can automate many release steps easily.
Currently JustRelease supports following automations:

-Version Update
-Create Artifacts
-Publish


##Configuration

To configure your release steps you need to create `justrelease.yml` file in your repo.
If you don't create a `justrelease.yml` file, default configurations will be used.


###Version Update

You need to give file extensions as a config. If you don't provide this step, we automatically detect your project type
and use default configurations. Currently we are supporting maven and npm type projects.

An example is following ( you need to put this code snippet to your `justrelease.yml` file.

```
version.update:
    - json,js
```

###Create Artifacts

In this step, you need to provide commands that create artifacts in your project. If you don't provide this configuration,
we will use default config according to your project. ( i.e.: For maven `mvn clean install` )

Example create artifacts config:

```
create.artifacts:
        - npm install
        - npm test
```

###Publish

In this step you need to provide publish configuration. Currently we are supporting `github` publish.

You can give your releasenotes file, and your attachment file.
An example is following:

```
publish:
        - github:
            - description=releasenotes.md
            - attachment=lib/index.js
```

##How to Use JustRelease Library

###The simple usage is:

java -jar justrelease.jar username/reponame

In this usage we detect your project type, update the version ( we assume it is patch release).


###Giving release type

java -jar justrelease.jar username/reponame (major | minor | patch )


###DryRun Config

If you want to just observe what will happen when releasing you can use `DryRun` config.
In this case, there will be no push or committing. You will see just logs.

java -jar justrelease.jar username/reponame minor -dryRun

