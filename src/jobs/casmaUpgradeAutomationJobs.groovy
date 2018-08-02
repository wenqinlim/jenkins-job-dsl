String basePath = 'upgradeAutomation'

folder(basePath) {
    description 'This example shows how to include script resources from the workspace for CASMA upgrade automation.'
}

[
    [uPath: 'direct'], 
    [uPath: 'incremental']
].each { Map config ->

    echo ${config.uPath}

    job("$basePath/upgrade-${config.uPath}") {

        description "${config.uPath} upgrade job"

        parameters {
            readFileFromWorkspace('src/scripts/uParameters_${config.uPath}.txt')
        }

        customWorkspace('/home/wenqin1/Jenkins-Work-Directory/')

        steps {
            shell readFileFromWorkspace('src/scripts/runTest.sh')
        }

        publishers {
            archiveJunit('project-${DEPLOY_CONFIG}/tests/Reports/*.xml') {
               retainLongStdout()
               healthScaleFactor(1.0)
            }
        }
    }
}

