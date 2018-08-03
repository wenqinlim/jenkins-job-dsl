String basePath = 'upgradeAutomation'

folder(basePath) {
    description 'This example shows how to include script resources from the workspace for CASMA upgrade automation.'
}

c[
    [uPath: 'direct', DEPLOY_CONFIG: 'kl.cas-s500-a1-01'], 
    [uPath: 'incremental', DEPLOY_CONFIG: 'kl.cas-s400-a4-03' ],
].each { Map config ->

    job("$basePath/upgrade-${config.uPath}") {

        description "${config.uPath} upgrade job"

        parameters {
            stringParam('DEPLOY_CONFIG', "${config.DEPLOY_CONFIG}")
            stringParam('RELEASE_VERSION')
        }

        customWorkspace('/home/wenqin1/Jenkins-Work-Directory/')

        steps {
            shell readFileFromWorkspace('src/scripts/runTest.sh')
        }

        publishers {
            archiveJunit("project-${config.DEPLOY_CONFIG}/tests/Reports/*.xml") {
               retainLongStdout()
               healthScaleFactor(1.0)
            }
        }
    }
}
